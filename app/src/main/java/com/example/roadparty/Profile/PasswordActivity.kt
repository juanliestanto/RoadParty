package com.example.roadparty.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.roadparty.Home.MainActivity
import com.example.roadparty.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class PasswordActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var passLama: EditText
    private lateinit var passBaru: EditText
    private lateinit var konfirmasi: EditText
    private lateinit var backmenu: ImageView
    private lateinit var reset: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        auth = FirebaseAuth.getInstance()
        passLama = findViewById(R.id.etPasswordLama)
        passBaru = findViewById(R.id.etPasswordBaru)
        konfirmasi = findViewById(R.id.konfirmasi)
        backmenu = findViewById(R.id.backmenu)
        backmenu.setOnClickListener(this)
        reset = findViewById(R.id.resetPass)
        reset.setOnClickListener(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backmenu -> {
                Intent(this@PasswordActivity, ProfileActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
            R.id.reset-> {
                if (passLama.text.isNotEmpty() && passBaru.text.isNotEmpty() && konfirmasi.text.isNotEmpty()) {
                    if (passBaru.text.toString().equals(konfirmasi.text.toString())) {
                        val user = auth.currentUser
                        if (user != null && user.email != null) {
                            val credential = EmailAuthProvider.getCredential(user.email!!, passLama.text.toString())
                            user?.reauthenticate(credential)
                                    ?.addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            Toast.makeText(this, "Re-Authentication success.", Toast.LENGTH_SHORT
                                            ).show()
                                            user?.updatePassword(passBaru.text.toString())
                                                    ?.addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            Toast.makeText(this, "Password changed successfully.", Toast.LENGTH_SHORT).show()
                                                            startActivity(Intent(this, ProfileActivity::class.java))
                                                            finish()
                                                        }
                                                    }

                                        } else {
                                            Toast.makeText(this, "Re-Authentication failed.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                        } else {
                            startActivity(Intent(this, ProfileActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }
    }
}