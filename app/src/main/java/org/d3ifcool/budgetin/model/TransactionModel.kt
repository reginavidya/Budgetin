package org.d3ifcool.budgetin.model

data class TransactionModel(
    var userID: String? = null,
    var transactionID: String? =null,
    var type: Int? =null,
    var title: String? =null,
    var category: String? =null,
    var amount: Int? =null,
    var date: Long? =null,
    var invertedDate: Long?=null
) {
    companion object{
        var sortByAscRadius = Comparator<TransactionModel> { o1, o2 -> o1.date!!.compareTo(o2.date!!) }
        var sortByDescRadius = Comparator<TransactionModel> { o1, o2 -> o2.date!!.compareTo(o1.date!!) }

    }
}
