@file:Suppress("DEPRECATION")

package org.d3ifcool.budgetin

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.d3ifcool.budgetin.databinding.ActivityRegisterBinding
import org.d3ifcool.budgetin.notify.AlarmUtils

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AlarmUtils.setAlarmOff(this@RegisterActivity)

        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("USERS")

        binding.btnRegister.setOnClickListener{
            val name = binding.edtNamaLengkapRegister.text.toString()
            val email = binding.edtEmailRegister.text.toString()
            val password = binding.edtPasswordRegister.text.toString()

            if(name.isEmpty()){
                binding.edtNamaLengkapRegister.error = "Nama harus diisi"
                binding.edtNamaLengkapRegister.requestFocus()
                return@setOnClickListener
            }

            if(email.isEmpty()){
                binding.edtEmailRegister.error = "Email harus diisi"
                binding.edtEmailRegister.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()){
                binding.edtPasswordRegister.error = "Sandi harus diisi"
                binding.edtPasswordRegister.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.edtEmailRegister.error = "Email tidak valid"
                binding.edtEmailRegister.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6){
                binding.edtPasswordRegister.error = "Sandi minimal 6 karakter"
                binding.edtPasswordRegister.requestFocus()
                return@setOnClickListener
            }
            registerFirebase(name,email, password)
        }
        checkUser()
    }

    private fun checkUser(){
        if(auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun registerFirebase(name: String, email: String, password: String){
        val progressDialog = ProgressDialog(this@RegisterActivity)
        progressDialog.setTitle("Registrasi Pengguna")
        progressDialog.setMessage("Harap ditunggu")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                    saveUser(name, email,progressDialog)
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
    }
    private fun saveUser(name: String,
                         email: String,
                         progressDialog: ProgressDialog){
        val currentUserId = auth.currentUser!!.uid
        ref = FirebaseDatabase.getInstance().reference.child("USERS")
        val userMap = HashMap<String,Any>()
        userMap["id"] = currentUserId
        userMap["email"] = email
        userMap["name"] = name

        ref.child(currentUserId).setValue(userMap).addOnCompleteListener {
            if(it.isSuccessful){
                progressDialog.dismiss()
                Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }else{
                val message = it.exception!!.toString()
                Toast.makeText(this, "Gagal : $message", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
    }
}