package com.example.roadparty.Home

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeTransform
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.roadparty.Database.User
import com.example.roadparty.Profile.ProfileActivity
import com.example.roadparty.R
import com.example.roadparty.Register.MasukActivity
import com.example.roadparty.Transaksi.StatusActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var profile: ImageView
    private lateinit var wash: CardView
    private lateinit var iron: CardView
    private lateinit var washiron: CardView
    private lateinit var voucher: CardView
    private lateinit var status: ImageView
    private lateinit var chat : ImageView
    private lateinit var tvuser: TextView

    val db = FirebaseFirestore.getInstance()
    var userlist = ArrayList<User>()
    private val auth : FirebaseAuth get() = MasukActivity.user


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvuser = findViewById(R.id.tvuser)

        profile = findViewById(R.id.profile)
        profile.setOnClickListener(this)

        wash = findViewById(R.id.wash)
        wash.setOnClickListener(this)

        voucher = findViewById(R.id.voucher)
        voucher.setOnClickListener(this)

        iron = findViewById(R.id.iron)
        iron.setOnClickListener(this)

        washiron = findViewById(R.id.washiron)
        washiron.setOnClickListener(this)

        status = findViewById(R.id.status)
        status.setOnClickListener(this)

        chat = findViewById(R.id.chat)
        chat.setOnClickListener(this)

        this.getDataFromFirestore()

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.profile ->{
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.wash ->{
                val intent = Intent(this@MainActivity,
                    MencuciActivity::class.java)
                startActivity(intent)
            }
            R.id.iron ->{
                val intent = Intent(this@MainActivity, SeterikaActivity::class.java)
                startActivity(intent)
            }
            R.id.washiron ->{
                val intent = Intent(this@MainActivity, CuciSeterikaActivity::class.java)
                startActivity(intent)
            }
            R.id.voucher ->{
                val intent = Intent(this@MainActivity, VoucherActivity::class.java)
                startActivity(intent)
            }
            R.id.status ->{
                val intent = Intent(this@MainActivity, StatusActivity::class.java)
                startActivity(intent)
            }
            R.id.chat ->{

                try {
                    val url = "https://api.whatsapp.com/send?phone=" + "+6287865922730"
                    val packageManager = this.packageManager
                    val i = Intent(Intent.ACTION_VIEW)
                    i.setPackage("com.whatsapp")
                    i.data = Uri.parse(url)
                    if(i.resolveActivity(packageManager) != null){
                        startActivity(i)
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    Toast.makeText(this, "Whatsapp is not installed in your phone.", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
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
                        userlist.add(User(id,pengguna,alamat,kontak))
                    }
                    getdataprofile()
                } else {
                    Log.w(ContentValues.TAG, "Error getting documents.", task.exception)
                    Toast.makeText(this, "Gagal bos $task.exception",
                        Toast.LENGTH_SHORT).show()

                }
            })
    }
    private fun getdataprofile() {
        tvuser.setText(userlist[0].pengguna)
    }

}