package com.example.roadparty.Profile

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.roadparty.Database.User
import com.example.roadparty.Home.MainActivity
import com.example.roadparty.R
import com.example.roadparty.Register.BerandaActivity
import com.example.roadparty.Register.MasukActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    private val auth: FirebaseAuth get() = MasukActivity.user
    val db = FirebaseFirestore.getInstance()
    private lateinit var tvPengguna: EditText
    private lateinit var tvAlamat: EditText
    private lateinit var tvKontak: EditText
    private lateinit var tvEmail: TextView
    private lateinit var logout: TextView
    private lateinit var sandi: TextView
    private lateinit var email: TextView
    private lateinit var back: ImageView
    private lateinit var simpan: Button
    var userlist = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        back = findViewById(R.id.backhome)
        tvPengguna = findViewById(R.id.tvPengguna)
        tvKontak = findViewById(R.id.tvKontak)
        tvAlamat = findViewById(R.id.tvAlamat)
        tvEmail = findViewById(R.id.tvEmail)
        logout = findViewById(R.id.logout)
        sandi = findViewById(R.id.tvsandi)
        email = findViewById(R.id.tvemail)
        simpan = findViewById(R.id.simpan)

        simpan.setOnClickListener(this)
        logout.setOnClickListener(this)
        sandi.setOnClickListener(this)
        email.setOnClickListener(this)
        back.setOnClickListener(this)

        this.getDataFromFirestore()
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
                    Log.w(TAG, "Error getting documents.", task.exception)
                    Toast.makeText(
                        this, "Gagal bos $task.exception",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            })
    }

    private fun getdataprofile() {
        tvPengguna.setText(userlist[0].pengguna)
        tvAlamat.setText(userlist[0].alamat)
        tvKontak.setText(userlist[0].kontak)
        tvEmail.setText(auth.currentUser?.email)
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

    private fun updateUser() {
        val newTran = hashMapOf(
            "pengguna" to tvPengguna.text.toString(),
            "alamat" to tvAlamat.text.toString(),
            "kontak" to tvKontak.text.toString()
        )

        db.collection("user").document(userlist[0].id)
            .set(newTran, SetOptions.merge())
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.logout -> {
                auth.signOut()
                Intent(this@ProfileActivity, BerandaActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
            R.id.simpan -> {
                updateUser()
                Intent(this@ProfileActivity, BerandaActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
            R.id.tvsandi -> {
                Intent(this@ProfileActivity, PasswordActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
            R.id.tvemail -> {
                Intent(this@ProfileActivity, EmailActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }

            }
            R.id.backhome -> {
                Intent(this@ProfileActivity, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        }
    }
}