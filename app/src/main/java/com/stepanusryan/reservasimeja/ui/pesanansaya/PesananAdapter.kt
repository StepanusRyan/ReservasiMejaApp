package com.stepanusryan.reservasimeja.ui.pesanansaya

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stepanusryan.reservasimeja.R
import com.stepanusryan.reservasimeja.model.Pesanan

class PesananAdapter(private var data:List<Pesanan>,
                   private var listener:(Pesanan) -> Unit)
    : RecyclerView.Adapter<PesananAdapter.ViewHolder>(){
    private lateinit var contextAdapter: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView = layoutInflater.inflate(R.layout.item_pesanan,parent,false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position],listener)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        private val nomor:TextView = view.findViewById(R.id.txtNomor)
        private val tanggals:TextView = view.findViewById(R.id.txtTanggal)
        private val nama:TextView = view.findViewById(R.id.txtNama)
        private val jumlah:TextView = view.findViewById(R.id.txtJumlah)
       fun bind(data:Pesanan, listener: (Pesanan) -> Unit){
           nomor.text = data.nomorMeja
           tanggals.text = data.tanggal
           nama.text = data.nama
           jumlah.text = data.jumlahOrang
           itemView.setOnClickListener { listener(data) }
       }
    }
}