package org.d3ifcool.budgetin.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.d3ifcool.budgetin.ui.profile.AboutActivity
import org.d3ifcool.budgetin.LoginActivity
import org.d3ifcool.budgetin.model.Users
import org.d3ifcool.budgetin.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        userInfo()
        about()
        logout()
        setOnBackButtonClickListener()
        return binding.root
    }

    private fun userInfo() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val userRef =
            FirebaseDatabase.getInstance().reference.child("USERS").child(firebaseUser!!.uid)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(Users::class.java)
                    binding.namaTextView.text = user!!.name
                    binding.emailText.text = user.email
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun about() {
        binding.aboutBtn.setOnClickListener {
            activity?.let {
                val intent = Intent(it, AboutActivity::class.java)
                it.startActivity(intent)
            }
        }
    }
    private fun logout() {
        val auth = FirebaseAuth.getInstance()
        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            val mBuilder = AlertDialog.Builder(activity)
                .setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin keluar?")
                .setPositiveButton("Ya", null)
                .setNegativeButton("Tidak", null)
                .show()

            val mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE)
            mPositiveButton.setOnClickListener {
                Intent(this.activity, LoginActivity::class.java).also {
                    it.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    activity?.startActivity(it)
                }
            }
        }
    }

    private fun setOnBackButtonClickListener() {
        val backPressedTime = System.currentTimeMillis()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if ((backPressedTime + 2000) > System.currentTimeMillis()) {
                    requireActivity().finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }
}
