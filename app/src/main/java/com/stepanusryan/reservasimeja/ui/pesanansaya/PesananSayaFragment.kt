package com.stepanusryan.reservasimeja.ui.pesanansaya

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.stepanusryan.reservasimeja.databinding.FragmentPesananSayaBinding
import com.stepanusryan.reservasimeja.model.Pesanan
import com.stepanusryan.reservasimeja.ui.detailpesanan.DetailPesananActivity

class PesananSayaFragment : Fragment() {
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var pesanan:Pesanan
    private val dataPesanan = ArrayList<Pesanan>()

    private lateinit var binding: FragmentPesananSayaBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPesananSayaBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://reservasimeja-f35f8-default-rtdb.asia-southeast1.firebasedatabase.app")
        databaseReference = mFirebaseDatabase.getReference("meja")

        binding.rvPesanan.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.rvPesanan.setHasFixedSize(true)
        binding.rvPesanan.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
        getPesanan()

    }
    fun getPesanan(){
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                dataPesanan.clear()
                for (value in snapshot.children){
                    pesanan = value.getValue(Pesanan::class.java)!!
                    if (pesanan != null){
                        dataPesanan.add(pesanan)
                    }
                }
                binding.rvPesanan.adapter = PesananAdapter(dataPesanan){
                    val intent = Intent(requireActivity(), DetailPesananActivity::class.java)
                    intent.putExtra(DetailPesananActivity.ID_PESANAN,it.idPesanan)
                    intent.putExtra(DetailPesananActivity.NOMOR,it.nomorMeja)
                    intent.putExtra(DetailPesananActivity.HARGA,it.harga)
                    intent.putExtra(DetailPesananActivity.TANGGAL,it.tanggal)
                    intent.putExtra(DetailPesananActivity.NAMA,it.nama)
                    intent.putExtra(DetailPesananActivity.TELEPON,it.telepon)
                    intent.putExtra(DetailPesananActivity.JUMLAHORANG,it.jumlahOrang)
                    intent.putExtra(DetailPesananActivity.KEPERLUAN,it.keperluan)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show()
            }
        })
    }
}