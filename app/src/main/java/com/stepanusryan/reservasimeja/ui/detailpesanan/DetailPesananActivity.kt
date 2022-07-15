package com.stepanusryan.reservasimeja.ui.detailpesanan

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import com.google.firebase.database.*
import com.stepanusryan.reservasimeja.UiActivity
import com.stepanusryan.reservasimeja.databinding.ActivityDetailPesananBinding
import com.stepanusryan.reservasimeja.R

class DetailPesananActivity : AppCompatActivity() {
    private lateinit var idPesanan:String
    private lateinit var nomor:String
    private lateinit var harga:String
    private lateinit var nama:String
    private lateinit var tanggal:String
    private lateinit var telepon:String
    private lateinit var jumlah:String
    private lateinit var keperluan:String
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityDetailPesananBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://reservasimeja-f35f8-default-rtdb.asia-southeast1.firebasedatabase.app")
        databaseReference = mFirebaseDatabase.getReference("meja")

        idPesanan = intent.getStringExtra(ID_PESANAN).toString()
        nomor = intent.getStringExtra(NOMOR).toString()
        harga = intent.getStringExtra(HARGA).toString()
        nama = intent.getStringExtra(NAMA).toString()
        tanggal = intent.getStringExtra(TANGGAL).toString()
        telepon = intent.getStringExtra(TELEPON).toString()
        jumlah = intent.getStringExtra(JUMLAHORANG).toString()
        keperluan = intent.getStringExtra(KEPERLUAN).toString()

        binding.txtNomor.text = nomor
        binding.txtPrice.text = harga
        binding.txtNama.text = nama
        binding.txtTanggal.text = tanggal
        binding.txtNoTelp.text = telepon
        binding.txtJumlah.text = jumlah
        binding.txtKeperluan.text = keperluan


        binding.btnBatal.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Apakah Anda yakin ingin membatalkan pesanan ?")
                .setCancelable(true)
                .setPositiveButton("YA", DialogInterface.OnClickListener { dialog, id ->
                    databaseReference.child(idPesanan).removeValue()
                    startActivity(Intent(this@DetailPesananActivity,UiActivity::class.java))
                    showNotifBatalkanPesanan(nomor)
                    Toast.makeText(this@DetailPesananActivity, "Anda berhasil menghapus pesanan", Toast.LENGTH_SHORT).show()
                    finish()
                })
                .setNegativeButton("TIDAK", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })
            val alertDialog = dialogBuilder.create()
            alertDialog.setTitle("Batal Pesanan")
            alertDialog.show()
        }
    }
    private fun showNotifBatalkanPesanan(nomors: String?)
    {
        val notificationChannelID = "channel_reservasi_meja"
        val context = this.applicationContext
        var notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            val channelName = "Reservasi Meja Notif Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val mChannel = NotificationChannel(notificationChannelID,channelName,importance)
            notificationManager.createNotificationChannel(mChannel)
        }

        val mIntent = Intent(this, UiActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this,0,mIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this,notificationChannelID)
        builder.setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.table)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources,R.drawable.table
                )
            )
            .setTicker("notif starting")
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000,1000))
            .setLights(Color.RED,3000,3000)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setContentTitle("Pembatalan Pesanan Berhasil")
            .setContentText("Reservasi meja nomor $nomors berhasil dibatalkan. Terima kasih")
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(115,builder.build())
    }

    companion object{
        const val ID_PESANAN = "pesanan"
        const val NOMOR = "nomor"
        const val HARGA = "harga"
        const val TANGGAL = "tanggal"
        const val NAMA = "nama"
        const val TELEPON = "telepon"
        const val JUMLAHORANG = "jumlah"
        const val KEPERLUAN = "keperluan"
    }
}