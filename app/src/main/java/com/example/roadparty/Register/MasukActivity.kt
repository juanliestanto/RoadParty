package com.example.roadparty.Register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.roadparty.Home.MainActivity
import com.example.roadparty.R
import com.google.firebase.auth.FirebaseAuth

class MasukActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    private lateinit var auth: FirebaseAuth
    private lateinit var masuk: Button
    private lateinit var daftar: TextView
    private lateinit var reset: TextView

    companion object {
        lateinit var user: FirebaseAuth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masuk)

        daftar = findViewById(R.id.daftar)
        daftar.setOnClickListener(this)

        masuk = findViewById(R.id.masuk)
        masuk.setOnClickListener(this)

        reset = findViewById(R.id.reset)
        reset.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()

        user = auth


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.daftar -> {
                val intent = Intent(this@MasukActivity, DaftarActivity::class.java)
                startActivity(intent)
            }
            R.id.masuk -> {
                etEmail = findViewById(R.id.etEmail)
                etPassword = findViewById(R.id.etPassword)
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (email.isEmpty()) {
                    etEmail.error = "Email harus diisi !"
                    etEmail.requestFocus()
                    return
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.error = "Email Tidak valid !"
                    etEmail.requestFocus()
                    return
                }

                if (password.isEmpty() || password.length < 6) {
                    etPassword.error = "password kurang dari 6 karakter "
                    etPassword.requestFocus()
                    return
                }
                loginUser(email, password)
            }
            R.id.reset -> {
                val intent = Intent(this@MasukActivity, ResetActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        user = auth
                        Intent(this@MasukActivity, MainActivity::class.java).also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                        }
                    } else {
                        Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
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