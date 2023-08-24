package org.d3ifcool.budgetin.ui.catatan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.d3ifcool.budgetin.R
import org.d3ifcool.budgetin.adapter.TransaksiAdapter
import org.d3ifcool.budgetin.databinding.FragmentTransaksiBinding
import org.d3ifcool.budgetin.model.TransactionModel
import org.d3ifcool.budgetin.ui.catatan.transaksi.FormActivity
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class TransaksiFragment : Fragment(), TransaksiAdapter.RecyclerViewClickListener {
    private lateinit var binding: FragmentTransaksiBinding

    private var backPressedTime by Delegates.notNull<Long>()
    private lateinit var cbGroup: RadioGroup
    private lateinit var cbTerjauh: RadioButton
    private lateinit var cbTerdekat: RadioButton
    private lateinit var cbPemasukan: RadioButton
    private lateinit var cbPengeluaran: RadioButton
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var tempArrayList : ArrayList<TransactionModel>
    private var transaksiAdapter: TransaksiAdapter? = null
    private var mTransaksi: List<TransactionModel>? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransaksiBinding.inflate(layoutInflater, container, false)

        binding.btnAdd.setOnClickListener {
            activity?.let {
                val intent = Intent(it, FormActivity::class.java)
                it.startActivity(intent)
            }
        }

        recyclerView = binding.rvSearchList
        recyclerView!!.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        mTransaksi = ArrayList()
        tempArrayList = ArrayList()
        retrieveAllTransactions()
        setOnBackButtonClickListener()
        return binding.root
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "inflater.inflate(R.menu.menu_home, menu)",
        "org.d3ifcool.budgetin.R"
    ))

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)

    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        @Suppress("DEPRECATION")
        return when (item.itemId) {
            R.id.search_data -> {
                val searchView = item.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        searchForTitle(newText.toString().lowercase(Locale.ROOT))
                        return false
                    }
                })
                true
            }
            R.id.sort_data -> {
                filterData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setOnBackButtonClickListener() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if ((backPressedTime + 0) > System.currentTimeMillis()) {
                    requireActivity().finish()
                } else {
                    Toast.makeText(context, "Tekan sekali lagi untuk keluar aplikasi", Toast.LENGTH_SHORT).show();
                }
                backPressedTime = System.currentTimeMillis()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    override fun onItemClicked(view: View, transaksi: TransactionModel) {
        Toast.makeText(context,
            "${transaksi.title} berhasil di klik",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun retrieveAllTransactions(){
        val firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val refTransaksi = FirebaseDatabase.getInstance().reference.child("Transaction")
        refTransaksi.orderByChild("userID").equalTo(firebaseUserID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (mTransaksi as ArrayList<TransactionModel>).clear()
                for (data in snapshot.children){
                    val transaksi: TransactionModel? = data.getValue(TransactionModel::class.java)
                    if (transaksi != null) {
                        (mTransaksi as ArrayList<TransactionModel>).add(transaksi)
                    }
                }
                transaksiAdapter = context?.let {
                    TransaksiAdapter(
                        it,
                        mTransaksi as ArrayList<TransactionModel>
                    )
                }
                recyclerView!!.adapter = transaksiAdapter

            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun transaksiPemasukan(){
        val refTransaksi = FirebaseDatabase.getInstance().reference.child("Transaction")
        val firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        refTransaksi.orderByChild("type").equalTo(2.0).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (mTransaksi as ArrayList<TransactionModel>).clear()
                mTransaksi?.sortedBy { it.userID }
                for (data in snapshot.children){
                    val transaksi: TransactionModel? = data.getValue(TransactionModel::class.java)
                    if (transaksi != null && firebaseUserID == transaksi.userID) {
                        (mTransaksi as ArrayList<TransactionModel>).add(transaksi)
                    }
                }
                transaksiAdapter = context?.let {
                    TransaksiAdapter(
                        it,
                        mTransaksi as ArrayList<TransactionModel>
                    )
                }
                recyclerView!!.adapter = transaksiAdapter

            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun transaksiPengeluaran(){
        val refTransaksi = FirebaseDatabase.getInstance().reference.child("Transaction")
        val firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        refTransaksi.orderByChild("type").equalTo(1.0).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (mTransaksi as ArrayList<TransactionModel>).clear()
                mTransaksi?.sortedBy { it.userID }
                for (data in snapshot.children){
                    val transaksi: TransactionModel? = data.getValue(TransactionModel::class.java)
                    if (transaksi != null && firebaseUserID == transaksi.userID) {
                        (mTransaksi as ArrayList<TransactionModel>).add(transaksi)
                    }
                }
                transaksiAdapter = context?.let {
                    TransaksiAdapter(
                        it,
                        mTransaksi as ArrayList<TransactionModel>
                    )
                }
                recyclerView!!.adapter = transaksiAdapter

            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun searchForTitle(str: String){
        val firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val queryTransaksi = FirebaseDatabase.getInstance().reference
            .child("Transaction").orderByChild("title")
            .startAt(str)
            .endAt(str + "\uf8ff")

        queryTransaksi.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (mTransaksi as ArrayList<TransactionModel>).clear()

                for (data in snapshot.children){
                    val transaksi: TransactionModel? = data.getValue(TransactionModel::class.java)
                    if ((transaksi!!.userID).equals(firebaseUserID)){
                        (mTransaksi as ArrayList<TransactionModel>).add(transaksi)
                    }
                }
                transaksiAdapter = context?.let {
                    TransaksiAdapter(
                        it,
                        mTransaksi as ArrayList<TransactionModel>
                    )
                }
                recyclerView!!.adapter = transaksiAdapter
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    @SuppressLint("InflateParams", "NotifyDataSetChanged")
    private fun filterData(){
        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_filter, null)
        cbTerdekat = dialogView.findViewById(R.id.cbTerdekat)
        cbTerjauh = dialogView.findViewById(R.id.cbTerjauh)
        cbPemasukan = dialogView.findViewById(R.id.cbPemasukan)
        cbPengeluaran = dialogView.findViewById(R.id.cbPengeluaran)
        bottomSheetDialog = context?.let { BottomSheetDialog(it) }!!
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()

        cbTerdekat.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked){
                mTransaksi?.let { Collections.sort(it, TransactionModel.sortByAscRadius) }
                transaksiAdapter?.notifyDataSetChanged()
            }
        }

        cbTerjauh.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked){
                mTransaksi?.let { Collections.sort(it, TransactionModel.sortByDescRadius) }
                transaksiAdapter?.notifyDataSetChanged()
            }
        }

        cbPemasukan.setOnCheckedChangeListener{_: CompoundButton?, isChecked: Boolean ->
            if (isChecked){
                transaksiPemasukan()
                transaksiAdapter?.notifyDataSetChanged()
            }
        }

        cbPengeluaran.setOnCheckedChangeListener{_: CompoundButton?, isChecked: Boolean ->
            if (isChecked){
                transaksiPengeluaran()
                transaksiAdapter?.notifyDataSetChanged()
            }
        }
    }
}