package org.d3ifcool.budgetin.ui.catatan.transaksi

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle

import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.d3ifcool.budgetin.R
import org.d3ifcool.budgetin.menu.CategoryOptions
import org.d3ifcool.budgetin.model.TransactionModel
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FormActivity : AppCompatActivity() {
    private lateinit var etTitle: EditText
    private lateinit var etCategory: AutoCompleteTextView
    private lateinit var etAmount: EditText
    private lateinit var etDate: EditText
    private lateinit var btnSaveData: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var rbExpense: RadioButton
    private lateinit var rbIncome: RadioButton
    private lateinit var toolbarLinear: LinearLayout
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var auth: FirebaseAuth
    private var type: Int = 1
    private var amount: Int = 0
    private var date: Long = 0
    private var invertedDate: Long = 0
    private var isSubmitted: Boolean = false
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form)

        dbRef = FirebaseDatabase.getInstance().getReference("Transaction")

        initItem()

        etCategory = findViewById(R.id.category)
        val listExpense = CategoryOptions.expenseCategory()
        val expenseAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listExpense)
        etCategory.setAdapter(expenseAdapter)

        radioGroup.setOnCheckedChangeListener { _, checkedID ->
            etCategory.text.clear()
            if (checkedID == R.id.rbExpense) {
                type = 1
                setBackgroundColor()
                etCategory.setAdapter(expenseAdapter)
            }
            if (checkedID == R.id.rbIncome){
                type = 2
                setBackgroundColor()

                val listIncome = CategoryOptions.incomeCategory()
                val incomeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listIncome)
                etCategory.setAdapter(incomeAdapter)
            }
        }
        val currentTime = System.currentTimeMillis()
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val tanggal = Date(currentTime)
        val time = simpleDateFormat.format(tanggal)
        date += currentTime
        (etDate as TextView).text = time

        etDate.setOnClickListener {
            clickDatePicker()
        }

        btnSaveData.setOnClickListener {
            if (!isSubmitted){
                saveTransactionData()
            }else{
                Snackbar.make(findViewById(android.R.id.content), "Data Anda Sudah Ada", Snackbar.LENGTH_LONG).show()
            }

        }
    }
    private fun setBackgroundColor() {
        if (type == 1){
            rbExpense.setBackgroundResource(R.drawable.radio_select_pengeluaran)
            rbIncome.setBackgroundResource(R.drawable.radio_not_select)
            btnSaveData.backgroundTintList = getColorStateList(R.color.green)
        }else{
            rbIncome.setBackgroundResource(R.drawable.radio_select_pemasukan)
            rbExpense.setBackgroundResource(R.drawable.radio_not_select)
            btnSaveData.backgroundTintList = getColorStateList(R.color.green)
        }
    }

    private fun initItem() {
        etTitle = findViewById(R.id.title)
        etCategory = findViewById(R.id.category)
        etAmount = findViewById(R.id.amount)
        etDate = findViewById(R.id.date)
        btnSaveData = findViewById(R.id.saveButton)
        radioGroup = findViewById(R.id.typeRadioGroup)
        rbExpense = findViewById(R.id.rbExpense)
        rbIncome = findViewById(R.id.rbIncome)
        toolbarLinear = findViewById(R.id.toolbarLinear)
    }

    private fun clickDatePicker() {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->

                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                etDate.text = null
                etDate.hint = selectedDate

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                val theDate = sdf.parse(selectedDate)
                date = theDate!!.time

            },
            year,
            month,
            day
        )
        dpd.show()
    }

    private fun saveTransactionData() {
        auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser!!

        val userID = firebaseUser.uid
        val regex = Regex("[^A-Za-z]")
        var title = etTitle.text.toString()
        title = regex.replace(title, "")
        val category = etCategory.text.toString()
        val amountEt = etAmount.text.toString()

        try {
            amount = DecimalFormat.getNumberInstance().parse(amountEt)?.toInt()!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if(amountEt.isEmpty()){
            etAmount.error = "Harap Isi Jumlah"
        } else if(title.isEmpty()) {
            etTitle.error = "Harap Isi Judul"
        } else if(category.isEmpty()&& category.equals(Regex)){
            etCategory.error = "Harap Isi Kategori"
        } else if (title >= 'a'.toString() && title <= 'z'.toString() || title >= 'A'.toString() && title <= 'Z'.toString()) {
            val transactionID = dbRef.push().key!!
            invertedDate = date * -1
            val transaction = TransactionModel(userID,transactionID, type, title, category, amount, date, invertedDate) //object of data class


            dbRef.child(transactionID).setValue(transaction)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data Berhasil Tersimpan", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                .addOnFailureListener{ err->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            isSubmitted = true
        } else {
            etTitle.error = "Judul transaksi tidak boleh simbol"
        }
    }
}