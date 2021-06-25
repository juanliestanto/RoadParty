package com.example.roadparty.Transaksi

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roadparty.Adapter.PesanAdapter
import com.example.roadparty.Database.JenisPakaian
import com.example.roadparty.Database.User
import com.example.roadparty.Home.MainActivity
import com.example.roadparty.R
import com.example.roadparty.Register.MasukActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import java.time.LocalDate
import java.time.LocalTime
import kotlin.collections.ArrayList



class PesanActivity : AppCompatActivity(), View.OnClickListener {
    private var datalist = ArrayList<JenisPakaian>()
    private var total: Int = 0
    private var ambil: String = ""
    private lateinit var harga: TextView
    private lateinit var name: TextView
    private lateinit var alamat: TextView
    private lateinit var day: TextView
    private lateinit var waktu: TextView
    private lateinit var judul: TextView
    private lateinit var pesan: Button

    var id: String = ""
    val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth get() = MasukActivity.user
    var userlist = ArrayList<User>()
    private lateinit var rvPesan: RecyclerView

    @RequiresApi(Build.VERSION_CODES.O)
    val currentDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    val currentTime = LocalTime.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesan)

        rvPesan = findViewById(R.id.rvPesan)
        name = findViewById(R.id.psname)
        alamat = findViewById(R.id.psalamat)
        day = findViewById(R.id.psday)
        waktu = findViewById(R.id.pswaktu)
        harga = findViewById(R.id.harga)
        judul = findViewById(R.id.judul)
        pesan = findViewById(R.id.btnpesan)
        pesan.setOnClickListener(this)

        day.setText(currentDate.toString())
        waktu.setText(currentTime.toString())

        datalist = intent.getSerializableExtra("data") as ArrayList<JenisPakaian>
        ambil = intent.getSerializableExtra("judul") as String
        judul.setText(ambil)
        for (a in datalist.indices) {
            total += datalist[a].harga!!.toInt()
        }
        harga.setText(total.toString())

        rvPesan.layoutManager = LinearLayoutManager(this)
        val pesanAdapter = PesanAdapter(datalist)
        rvPesan.adapter = pesanAdapter
        this.getDataFromFirestore()

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun getDataFromFirestore() {
        db.collection("user")
                .whereEqualTo("uid", auth.currentUser?.uid)
                .get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "berhasil bos", Toast.LENGTH_SHORT).show()
                        userlist.clear()
                        for (document in task.result!!) {
                            val id = document.id
                            val pengguna = document.data.get("pengguna") as String
                            val alamat = document.data.get("alamat") as String
                            val kontak = document.data.get("kontak") as String
                            userlist.add(User(id, pengguna, alamat, kontak))
//                            when(type){
//                                "Income" ->{
//                                    dataIncome.add(Transaction(id,title,amount,type,tag,date,note,time))
//                                }
//                                "Expense" ->{
//                                    dataExpense.add(Transaction(id,title,amount,type,tag,date,note,time))
//                                }
//                            }
                        }
                        getdataprofile()
                    } else {
                        Log.w(ContentValues.TAG, "Error getting documents.", task.exception)
                        Toast.makeText(
                                this, "Gagal bos $task.exception",
                                Toast.LENGTH_SHORT
                        ).show()

                    }
                })
    }

    private fun getdataprofile() {
        name.setText(userlist[0].pengguna)
        alamat.setText(userlist[0].alamat)

    }

    private fun getIdTransactionFromFirestore() {
        db.collection("jumlah")
                .get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "berhasil bos", Toast.LENGTH_SHORT).show()
                        for (document in task.result!!) {
                            id = document.data.get("jumlah").toString()
                        }
                        addNewTransactionToFirestore()
                    } else {
                        Log.w(ContentValues.TAG, "Error getting documents.", task.exception)
                        Toast.makeText(
                                this, "Gagal bos $task.exception",
                                Toast.LENGTH_SHORT
                        ).show()

                    }
                })
    }

    private fun addNewTransactionToFirestore() {
        var uid = ""
        uid = auth.currentUser?.uid.toString()
        for (a in datalist.indices) {
            val transaksi: MutableMap<String, Any> = HashMap()
            transaksi["uid"] = uid
            transaksi["id"] = id
            transaksi["jenis"] = ambil
            transaksi["item"] = datalist[a].jenis.toString()
            transaksi["tanggal"] = day.text
            transaksi["waktu"] = waktu.text
            transaksi["total"] = harga.text
            transaksi["status"] = "dikirim"

            db.collection("transaksi")
                    .add(transaksi)
        }
        id = (id.toInt() + 1).toString()
        updateJumlah()
    }

    private fun updateJumlah() {
        val data = hashMapOf("jumlah" to id)
        db.collection("jumlah").document("123")
                .set(data, SetOptions.merge())
        Intent(this@PesanActivity, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btnpesan -> {
                    getIdTransactionFromFirestore()
                }
            }
        }
    }