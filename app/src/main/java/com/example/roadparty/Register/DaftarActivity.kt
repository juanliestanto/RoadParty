package com.example.roadparty.Register

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.roadparty.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime


class DaftarActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference : DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var db: FirebaseFirestore
    @RequiresApi(Build.VERSION_CODES.O)
    val currentDateTime = LocalDateTime.now()


    private lateinit var masuk: TextView
    private lateinit var daftar: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPengguna: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etKontak: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        daftar = findViewById(R.id.daftar)
        daftar.setOnClickListener(this)

        masuk = findViewById(R.id.masuk)
        masuk.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.daftar ->{
                etEmail = findViewById(R.id.etEmail)
                etPassword = findViewById(R.id.etPassword)

                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (email.isEmpty()){
                    etEmail.error = "Email harus diisi !"
                    etEmail.requestFocus()
                    return
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmail.error = "Email Tidak valid !"
                    etEmail.requestFocus()
                    return
                }

                if (password.isEmpty() || password.length < 6){
                    etPassword.error = "password kurang dari 6 karakter "
                    etPassword.requestFocus()
                    return
                }

                registerUser(email,password)
            }
            R.id.masuk ->{
                val intent = Intent(this@DaftarActivity, MasukActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    addNewDataToFirestore()
                    Log.d(TAG, "berhasil")
                    Intent(this@DaftarActivity, MasukActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                } else {
                    Log.d(TAG, "gagal")
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addNewDataToFirestore() {
        etPengguna = findViewById(R.id.etPengguna)
        etAlamat = findViewById(R.id.etAlamat)
        etKontak = findViewById(R.id.etKontak)

        var uid = ""
        uid = auth.currentUser?.uid.toString()
        val user: MutableMap<String, Any> = HashMap()
        user["uid"] = uid
        user["time"] = currentDateTime.toString()
        user["pengguna"] = etPengguna.text.toString()
        user["alamat"] = etAlamat.text.toString()
        user["kontak"] = etKontak.text.toString()

        db.collection("user")
                .add(user)
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

}