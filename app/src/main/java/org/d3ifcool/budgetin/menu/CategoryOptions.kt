package org.d3ifcool.budgetin.menu

object CategoryOptions {
    fun expenseCategory(): ArrayList<String> {
        val listExpense = ArrayList<String>()
        listExpense.add("Makanan/Minuman")
        listExpense.add("Transportasi")
        listExpense.add("Pendidikan")
        listExpense.add("Belanja")
        listExpense.add("Investasi")
        listExpense.add("Kesehatan")
        listExpense.add("Pengeluaran lainnya")
        return listExpense
    }

    fun incomeCategory(): ArrayList<String> {
        val listIncome = ArrayList<String>()
        listIncome.add("Gaji")
        listIncome.add("Uang Saku")
        listIncome.add("Hadiah")
        listIncome.add("Investasi")
        listIncome.add("Pemasukan Lainnya")
        return listIncome
    }
}