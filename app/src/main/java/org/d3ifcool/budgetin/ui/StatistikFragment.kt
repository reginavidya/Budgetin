package org.d3ifcool.budgetin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.d3ifcool.budgetin.R
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.util.Pair
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.d3ifcool.budgetin.databinding.FragmentStatistikBinding
import org.d3ifcool.budgetin.model.TransactionModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StatistikFragment : Fragment() {

    private lateinit var binding: FragmentStatistikBinding
    var amountExpense: Int = 0
    var amountIncome: Int = 0
    var allTimeExpense: Int = 0
    var allTimeIncome: Int = 0
    private var dateStart: Long = 0
    private var dateEnd: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatistikBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitDate()

        chartMenu()

        dateRangePicker()

        swipeRefresh()

        setOnBackButtonClickListener()
    }

    private fun swipeRefresh() {
        val swipeRefreshLayout: SwipeRefreshLayout = requireView().findViewById(R.id.swipeRefresh)
        swipeRefreshLayout.setOnRefreshListener {
            showAllTimeRecap()
            setupPieChart()
            setupBarChart()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun chartMenu() {
        val chartMenuRadio: RadioGroup = requireView().findViewById(R.id.RadioGroup)
        val pieChart: PieChart = requireView().findViewById(R.id.pieChart)
        val barChart: BarChart = requireView().findViewById(R.id.barChart)

        chartMenuRadio.setOnCheckedChangeListener { _, checkedID ->
            if (checkedID == R.id.rbBarChart){
                barChart.visibility = View.VISIBLE
                pieChart.visibility = View.GONE
            }
            if (checkedID == R.id.rbPieChart){
                barChart.visibility = View.GONE
                pieChart.visibility = View.VISIBLE
            }
        }
    }

    private fun setInitDate() {
        val dateRangeButton: Button = requireView().findViewById(R.id.buttonDate)

        val currentDate = Date()
        val cal: Calendar = Calendar.getInstance(TimeZone.getDefault())
        cal.time = currentDate

        val startDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH)
        cal.set(Calendar.DAY_OF_MONTH, startDay)
        val startDate = cal.time
        dateStart= startDate.time

        val endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        cal.set(Calendar.DAY_OF_MONTH, endDay)
        val endDate = cal.time
        dateEnd= endDate.time

        fetchAmount(dateStart, dateEnd)
        dateRangeButton.text = "Bulan Ini"
    }

    private fun dateRangePicker() {
        val dateRangeButton: Button = requireView().findViewById(R.id.buttonDate)
        dateRangeButton.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Pilih Tanggal")
                .setSelection(
                    Pair(
                        dateStart,
                        dateEnd
                    )
                ).build()
            datePicker.show(parentFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {

                val dateString = datePicker.selection.toString()
                val date: String = dateString.filter { it.isDigit() }

                val pickedDateStart = date.substring(0,13).toLong()
                val pickedDateEnd  = date.substring(13).toLong()
                dateRangeButton.text = convertDate(pickedDateStart, pickedDateEnd)
                fetchAmount(pickedDateStart, pickedDateEnd)

                @Suppress("DEPRECATION")
                Handler().postDelayed({
                    setupPieChart()
                    setupBarChart()
                }, 200)
            }
        }
    }

    private fun showAllTimeRecap() {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        val tvNetAmount: TextView = requireView().findViewById(R.id.netAmount)
        val tvAmountExpense: TextView = requireView().findViewById(R.id.expenseAmount)
        val tvAmountIncome: TextView = requireView().findViewById(R.id.incomeAmount)
        tvNetAmount.text = formatRupiah.format("${allTimeIncome-allTimeExpense}".toDouble())
        tvAmountExpense.text = formatRupiah.format("$allTimeExpense".toDouble())
        tvAmountIncome.text = formatRupiah.format("$allTimeIncome".toDouble())
    }

    private fun setupBarChart() {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        val netAmountRangeDate: TextView = requireView().findViewById(R.id.netAmountRange)
        netAmountRangeDate.text = formatRupiah.format("${amountIncome-amountExpense}".toDouble())
        val barChart: BarChart = requireView().findViewById(R.id.barChart)
        val barEntries = arrayListOf<BarEntry>()
        barEntries.add(BarEntry(1f, amountExpense.toFloat()))
        barEntries.add(BarEntry(2f, amountIncome.toFloat()))

        val xAxisValue= arrayListOf("","Pengeluaran", "Pemasukan")
        barChart.animateXY(500, 500)
        barChart.description.isEnabled = false
        barChart.setDrawValueAboveBar(true)
        barChart.setDrawBarShadow(false)
        barChart.setPinchZoom(false)
        barChart.isDoubleTapToZoomEnabled = false
        barChart.setScaleEnabled(false)
        barChart.setFitBars(true)
        barChart.legend.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.axisLeft.isEnabled = false
        barChart.axisLeft.axisMinimum = 0f

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValue)

        val barDataSet = BarDataSet(barEntries, "")
        @Suppress("DEPRECATION")
        barDataSet.setColors(
            resources.getColor(R.color.yellow),
            resources.getColor(R.color.green)
        )
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 15f
        barDataSet.isHighlightEnabled = false

        val barData = BarData(barDataSet)
        barData.barWidth = 0.5f
        barChart.data = barData
    }


    private fun setupPieChart(){
        val pieChart: PieChart = requireView().findViewById(R.id.pieChart)
        val pieEntries = arrayListOf<PieEntry>()
        pieEntries.add(PieEntry(amountExpense.toFloat(), "Pengeluaran"))
        pieEntries.add(PieEntry(amountIncome.toFloat(), "Pemasukan"))
        pieChart.animateXY(500, 500)
        val pieDataSet = PieDataSet(pieEntries,"")
        @Suppress("DEPRECATION")
        pieDataSet.setColors(
            resources.getColor(R.color.yellow),
            resources.getColor(R.color.green)
        )

        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.holeRadius = 46f

        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(true)
        pieData.setValueFormatter(PercentFormatter(pieChart))
        pieData.setValueTextSize(12f)
        pieData.setValueTextColor(Color.WHITE)
        pieChart.data = pieData
        pieChart.invalidate()
    }

    private fun fetchAmount(dateStart: Long, dateEnd: Long) {
        var amountExpenseTemp = 0
        var amountIncomeTemp = 0
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!.uid
        val transactionList: ArrayList<TransactionModel> = arrayListOf()
        val dbRef  = FirebaseDatabase.getInstance().reference.child("Transaction")

        dbRef.orderByChild("userID").equalTo(firebaseUser).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transactionList.clear()
                if (snapshot.exists()) {
                    for (transactionSnap in snapshot.children) {
                        val transactionData =
                            transactionSnap.getValue(TransactionModel::class.java) //reference data class
                        transactionList.add(transactionData!!)
                    }
                }

                for ((i) in transactionList.withIndex()){
                    if (transactionList[i].type == 1 &&
                        transactionList[i].date!! > dateStart-86400000 &&
                        transactionList[i].date!! <= dateEnd){
                        amountExpenseTemp += transactionList[i].amount!!
                    }else if (transactionList[i].type == 2 &&
                        transactionList[i].date!! > dateStart-86400000 &&
                        transactionList[i].date!! <= dateEnd){
                        amountIncomeTemp += transactionList[i].amount!!
                    }
                }
                amountExpense= amountExpenseTemp
                amountIncome = amountIncomeTemp

                @Suppress("NAME_SHADOWING") var amountExpenseTemp = 0
                @Suppress("NAME_SHADOWING") var amountIncomeTemp = 0

                for ((i) in transactionList.withIndex()){
                    if (transactionList[i].type == 1 ){
                        amountExpenseTemp += transactionList[i].amount!!
                    }else if (transactionList[i].type == 2){
                        amountIncomeTemp += transactionList[i].amount!!
                    }
                }
                allTimeExpense = amountExpenseTemp
                allTimeIncome = amountIncomeTemp

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setOnBackButtonClickListener() {
        val backPressedTime = System.currentTimeMillis()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    requireActivity().finish()
                } else {
                    Toast.makeText(context, "Harap tunggu 2 detik",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun convertDate(dateStart: Long, dateEnd: Long): String {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val date1 = Date(dateStart)
        val date2 = Date(dateEnd)
        val result1 = simpleDateFormat.format(date1)
        val result2 = simpleDateFormat.format(date2)
        return "$result1 - $result2"
    }

    override fun onStart() {
        super.onStart()
        Handler(Looper.getMainLooper()).postDelayed({
            showAllTimeRecap()
            setupPieChart()
            setupBarChart()
        },20)


    }
}