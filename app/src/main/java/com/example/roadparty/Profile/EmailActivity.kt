package com.example.roadparty.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.roadparty.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class EmailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var pass: EditText
    private lateinit var email: EditText
    private lateinit var reset: Button
    private lateinit var backhome: ImageView
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        auth = FirebaseAuth.getInstance()

        pass = findViewById(R.id.etPassword)
        email = findViewById(R.id.etEmail)
        backhome = findViewById(R.id.backhome)
        backhome.setOnClickListener(this)
        reset = findViewById(R.id.reset)
        reset.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backhome -> {
                Intent(this@EmailActivity, ProfileActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
            R.id.backhome -> {
                val user = auth.currentUser
                val password : String = pass.text.toString()
                if(password.isEmpty()){
                    pass.error = "masukkan password"
                    pass.requestFocus()
                    return
                }
                user?.let{
                    val userCredential = EmailAuthProvider.getCredential(it.email!!, password)
                    it.reauthenticate(userCredential).addOnCompleteListener {
                        if(it.isSuccessful){
                            updateEmail()
                        }else if(it.exception is FirebaseAuthInvalidCredentialsException){
                            pass.error = "password salah"
                            pass.requestFocus()
                        }else{

                        }
                    }
                }
            }
        }
    }

    private fun updateEmail() {
        val user = auth.currentUser
        val mail: String = email.text.toString()
        if(mail.isEmpty()){
            email.error = "email harus diisi"
            email.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.error = "email tidak valid"
            email.requestFocus()
            return
        }
        user?.let{
            user.updateEmail(mail).addOnCompleteListener {
                if (it.isSuccessful){
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                }else{

                }
            }
        }
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
}
