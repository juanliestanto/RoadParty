package com.example.roadparty.Home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.roadparty.Database.JenisPakaian
import com.example.roadparty.Transaksi.PesanActivity
import com.example.roadparty.R

class MencuciActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var back : ImageView
    private lateinit var pesan : Button
    var jenispakaianlist = ArrayList<JenisPakaian>()

    lateinit var switch1: Switch
    lateinit var switch2: Switch
    lateinit var switch3: Switch
    lateinit var switch4: Switch
    lateinit var switch5: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mencuci)

        switch1 = findViewById(R.id.switch1)
        switch1.setOnCheckedChangeListener{Switch: CompoundButton?, onSwitch: Boolean ->
            if(onSwitch){
                val data = JenisPakaian("Pakaian", "4000")
                jenispakaianlist.add(data)
            }
        }

        switch2 = findViewById(R.id.switch2)
        switch2.setOnCheckedChangeListener{Switch: CompoundButton?, onSwitch: Boolean ->
            if(onSwitch){
                val data = JenisPakaian("Jas", "16000")
                jenispakaianlist.add(data)
            }
        }

        switch3 = findViewById(R.id.switch3)
        switch3.setOnCheckedChangeListener{Switch: CompoundButton?, onSwitch: Boolean ->
            if(onSwitch){
                val data = JenisPakaian("Selimut", "5000")
                jenispakaianlist.add(data)
            }
        }

        switch4 = findViewById(R.id.switch4)
        switch4.setOnCheckedChangeListener{Switch: CompoundButton?, onSwitch: Boolean ->
            if(onSwitch){
                val data = JenisPakaian("Tas", "15000")
                jenispakaianlist.add(data)
            }
        }

        switch5 = findViewById(R.id.switch5)
        switch5.setOnCheckedChangeListener{Switch: CompoundButton?, onSwitch: Boolean ->
            if(onSwitch){
                val data = JenisPakaian("Sepatu", "17000")
                jenispakaianlist.add(data)
            }
        }

        back = findViewById(R.id.back)
        back.setOnClickListener(this)

        pesan = findViewById(R.id.pesan)
        pesan.setOnClickListener(this)
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back ->{
                val intent =  Intent(this@MencuciActivity, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.pesan ->{
                val intent = Intent (this@MencuciActivity, PesanActivity::class.java)
                        .putExtra("data", jenispakaianlist).putExtra("judul", "Mencuci")
                startActivity(intent)
            }
        }
    }
}