package org.d3ifcool.budgetin.adapter

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import org.d3ifcool.budgetin.R
import org.d3ifcool.budgetin.menu.CategoryOptions
import org.d3ifcool.budgetin.model.TransactionModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NAME_SHADOWING")
class TransaksiAdapter(mContext: Context, mTransaksi: List<TransactionModel>) : RecyclerView.Adapter<TransaksiAdapter.ViewHolder>() {
    private val mContext: Context
    private val mTransaksi: List<TransactionModel>
    private var transaksiId = ""
    private var type: Int = 1
    private var dates: Long = 0
    private var invertedDate: Long = 0
    private var listener: RecyclerViewClickListener? = null

    init {
        this.mContext = mContext
        this.mTransaksi = mTransaksi
    }

    interface RecyclerViewClickListener {
        fun onItemClicked(view: View, transaksi: TransactionModel)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.transaction_list_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        val localeID = Locale("in", "ID")
        val date = SimpleDateFormat("dd", localeID)
        val month = SimpleDateFormat("MMM", localeID)
        val year = SimpleDateFormat("yyyy", localeID)
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        val transaksi: TransactionModel = mTransaksi[position]

        holder.hapusBtn.setOnClickListener { it ->
            AlertDialog.Builder(mContext).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Hapus Data").setMessage("Apakah Anda Yakin?")
                .setPositiveButton("Ya") { _, _ ->
                    transaksi.transactionID?.let {
                        FirebaseDatabase.getInstance().getReference("Transaction")
                            .child(transaksiId).child(it).removeValue()
                    }
                    (mTransaksi as MutableList<TransactionModel>).remove(transaksi)
                    Toast.makeText(mContext, "Data Terhapus", Toast.LENGTH_LONG).show()
                }.setNegativeButton("Tidak", null).show()
            listener?.onItemClicked(it, transaksi)
        }
        holder.editBtn.setOnClickListener {
            val builder = AlertDialog.Builder(mContext)
            builder.setTitle("Update")
            val inflater = LayoutInflater.from(mContext)
            val view = inflater.inflate(R.layout.update_dialog, null)
            val textAmount = view.findViewById<EditText>(R.id.amountUpdate)
            val textTitle = view.findViewById<EditText>(R.id.title)
            val textCategory = view.findViewById<AutoCompleteTextView>(R.id.category)
            val textDate = view.findViewById<EditText>(R.id.dateUpdate)
            val buttonUpdateData = view.findViewById<Button>(R.id.updateButton)

            textAmount.setText(transaksi.amount.toString())
            textTitle.setText(transaksi.title)
            textCategory.setText(transaksi.category)

            val listExpense = CategoryOptions.expenseCategory()
            val expenseAdapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_dropdown_item, listExpense)
            textCategory.setAdapter(expenseAdapter)

            if (type == 1) {
                textCategory.setAdapter(expenseAdapter)
            }
            if (type == 2){
                val listIncome = CategoryOptions.incomeCategory()
                val incomeAdapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_dropdown_item, listIncome)
                textCategory.setAdapter(incomeAdapter)
            }

            val currentTime = System.currentTimeMillis()
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val tanggal = transaksi.date?.let { it1 -> Date(it1) }
            val time = tanggal?.let { it1 -> simpleDateFormat.format(it1) }
            dates += currentTime
            (textDate as TextView).text = time
            textDate.setOnClickListener {
                val myCalendar = Calendar.getInstance()
                val years = myCalendar.get(Calendar.YEAR)
                val months = myCalendar.get(Calendar.MONTH)
                val day = myCalendar.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(mContext,
                    { _, selectedYear, selectedMonth, selectedDayOfMonth ->

                        val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                        textDate.text = null
                        textDate.hint = selectedDate

                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                        val theDate = sdf.parse(selectedDate)
                        dates = theDate!!.time
                        invertedDate = dates * -1
                    },
                    years,
                    months,
                    day
                )
                dpd.show()
            }


            builder.setView(view)
            val alertDialog = builder.create()
            alertDialog.show()
            buttonUpdateData.setOnClickListener {
                if (uid != null) {
                    transaksi.transactionID?.let { it1 ->
                        updateTransactionData(
                            uid,
                            it1,
                            type,
                            textTitle.text.toString(),
                            textCategory.text.toString(),
                            textAmount.text.toString().toInt(),
                            dates,
                            invertedDate
                        )
                    }
                }
                Toast.makeText(mContext, "Transaction Data Updated", Toast.LENGTH_LONG).show()

                holder.categoryText.text = textCategory.text.toString()
                holder.titleText.text = textTitle.text.toString()
                holder.amountText.text = textAmount.text.toString()
                holder.dateText.text = transaksi.date.let { date.format(it) }
                holder.monthText.text = transaksi.date.let { month.format(it) }
                holder.yearText.text = transaksi.date.let { year.format(it) }
                alertDialog.dismiss()

            }
        }
        holder.hapusBtn.setBackgroundColor(Color.TRANSPARENT)
        holder.editBtn.setBackgroundColor(Color.TRANSPARENT)
        holder.categoryText.text = transaksi.category
        holder.categoryText.setTextColor(Color.parseColor("#000000"))
        holder.titleText.text = transaksi.title
        holder.amountText.text = transaksi.amount?.let { formatRupiah.format(it) }
        holder.dateText.text = transaksi.date.let { date.format(it) }
        holder.monthText.text = transaksi.date.let { month.format(it) }
        holder.yearText.text = transaksi.date.let { year.format(it) }
        if (transaksi.type == 1){
            holder.typeText.text = "Pengeluaran"
            holder.typeText.setTextColor(Color.parseColor("#ff9f1c"))
        } else {
            holder.typeText.text = "Pemasukan"
            holder.typeText.setTextColor(Color.parseColor("#2ec4b6"))
        }
    }

    override fun getItemCount(): Int {
        return mTransaksi.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var categoryText: TextView
        var titleText: TextView
        var amountText: TextView
        var dateText: TextView
        var monthText: TextView
        var yearText: TextView
        var typeText: TextView
        var hapusBtn: ImageButton
        var editBtn: ImageButton

        init {
            categoryText = itemView.findViewById(R.id.tvCategory)
            titleText = itemView.findViewById(R.id.tvTransactionTitle)
            amountText = itemView.findViewById(R.id.tvAmount)
            dateText = itemView.findViewById(R.id.tvDate)
            monthText = itemView.findViewById(R.id.tvMonth)
            yearText = itemView.findViewById(R.id.tvYear)
            typeText = itemView.findViewById(R.id.tvType)
            hapusBtn = itemView.findViewById(R.id.hapusBtn)
            editBtn = itemView.findViewById(R.id.editBtn)
        }
    }

    private fun updateTransactionData(
        userID: String,
        transactionID:String,
        type: Int,
        title: String,
        category: String,
        amount: Int,
        date: Long,
        invertedDate: Long
    ){
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            val dbRef = FirebaseDatabase.getInstance().getReference("Transaction") //initialize database with uid as the parent
            val transactionInfo = TransactionModel(userID, transactionID, type, title, category, amount, date, invertedDate)
            dbRef.child(transactionID).setValue(transactionInfo)
        }
    }
}
