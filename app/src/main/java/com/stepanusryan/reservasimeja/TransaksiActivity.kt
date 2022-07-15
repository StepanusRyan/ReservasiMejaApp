package com.stepanusryan.reservasimeja

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.database.*
import com.stepanusryan.reservasimeja.databinding.ActivityTransaksiBinding
import com.stepanusryan.reservasimeja.model.Pesanan
import java.text.SimpleDateFormat
import java.util.*

class TransaksiActivity : AppCompatActivity() {
    private var binding: ActivityTransaksiBinding? = null
    private lateinit var idPesanan:String
    private lateinit var nomor:String
    private lateinit var harga:String
    private lateinit var nama:String
    private lateinit var tanggals:String
    private lateinit var telepon:String
    private lateinit var jumlah:String
    private lateinit var keperluan:String
    private lateinit var pesanan:Pesanan
    private lateinit var mFirebaseDatabase:FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransaksiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        mFirebaseDatabase = FirebaseDatabase.getInstance("https://reservasimeja-f35f8-default-rtdb.asia-southeast1.firebasedatabase.app")
        databaseReference = mFirebaseDatabase.getReference("meja")

        val random  = (1..100000).random()
        idPesanan = random.toString()

        nomor = intent.getStringExtra(NOMOR).toString()
        harga = intent.getStringExtra(HARGA).toString()
        binding?.txtNomorMeja?.text = "Meja nomor $nomor"
        binding?.txtHarga?.text = "Rp.$harga"


        val calendar = Calendar.getInstance()
        val tahun = calendar.get(Calendar.YEAR)
        val bulan = calendar.get(Calendar.MONTH)
        val tanggal = calendar.get(Calendar.DAY_OF_MONTH)

        val dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)

            val format = "dd/MM/yyyy"
            val localId = Locale("in","ID")
            val simpleFormat = SimpleDateFormat(format,localId)
            val months = month + 1

            tanggals = "$dayOfMonth / $months / $year"

            binding?.datePicker?.text = simpleFormat.format(calendar.time)
        }

        binding?.datePicker?.setOnClickListener {
            DatePickerDialog(this@TransaksiActivity,dateListener,
                tahun,bulan,tanggal).show()
        }

        binding?.btnKirim?.setOnClickListener {
            nama = binding?.editTextTextNama?.text.toString()
            keperluan = binding?.editTextTextKeperluan?.text.toString()
            telepon = binding?.editTextTextTelepon?.text.toString()
            jumlah = binding?.editTextTextJumlah?.text.toString()
            when{
                tanggals == "" ->{
                    binding?.datePicker?.error = "Silahkan pilih tanggal"
                    binding?.datePicker?.requestFocus()
                }
                nama == "" ->{
                    binding?.editTextTextNama?.error = "Silahkan mengisi nama pemesan"
                    binding?.editTextTextNama?.requestFocus()
                }
                telepon == "" ->{
                    binding?.editTextTextTelepon?.error = "Silahkan mengisi nomor telepon"
                    binding?.editTextTextTelepon?.requestFocus()
                }
                jumlah == "" ->{
                    binding?.editTextTextJumlah?.error = "Silahkan mengisi jumlah orang"
                    binding?.editTextTextJumlah?.requestFocus()
                }
                keperluan == "" ->{
                    binding?.editTextTextKeperluan?.error = "Silahkan mengisi keperluannya"
                    binding?.editTextTextKeperluan?.requestFocus()
                }
                else ->{
                    sendTransaksi(idPesanan,nomor,harga,tanggals,nama,telepon,jumlah,keperluan)
                }
            }
        }
    }
    private fun sendTransaksi(idPesan:String,idMeja:String,harga:String,tanggal:String,nama:String,telepon:String,jumlah:String,keperluan:String){
        pesanan = Pesanan(idPesan,idMeja,harga,tanggal,nama,telepon,jumlah, keperluan)
        checkTransaksi(tanggal,idMeja)
    }
    private fun checkTransaksi(tanggal: String,nomor: String){
        databaseReference.orderByChild("nomorMeja").equalTo(nomor).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    databaseReference.orderByChild("tanggal").equalTo(tanggal).addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()){
                                binding?.datePicker?.text = ""
                                tanggals = ""
                                Toast.makeText(this@TransaksiActivity,"Tanggal untuk meja $nomor sudah ada yang memesan",Toast.LENGTH_SHORT).show()
                            }
                            else{
                                databaseReference.child(idPesanan).setValue(pesanan)
                                startActivity(Intent(this@TransaksiActivity,UiActivity::class.java))
                                showNotifTransaksiBerhasil(nomor)
                                binding?.datePicker?.text = ""
                                tanggals = ""
                                Toast.makeText(this@TransaksiActivity,"Anda berhasil reservasi meja nomor $nomor di tanggal $tanggal",Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@TransaksiActivity,error.message,Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                else{
                    databaseReference.child(idPesanan).setValue(pesanan)
                    startActivity(Intent(this@TransaksiActivity,UiActivity::class.java))
                    showNotifTransaksiBerhasil(nomor)
                    binding?.datePicker?.text = ""
                    tanggals = ""
                    Toast.makeText(this@TransaksiActivity,"Anda berhasil reservasi meja nomor $nomor di tanggal $tanggal",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TransaksiActivity,error.message,Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun showNotifTransaksiBerhasil(nomors: String?)
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
            .setContentTitle("Reservasi Meja Berhasil")
            .setContentText("Anda reservasi meja nomor $nomors. Silahkan cek detailnya di menu pesanan saya. Terima kasih")
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(115,builder.build())
    }
    companion object{
        const val NOMOR = "nomor"
        const val HARGA = "harga"
    }
}