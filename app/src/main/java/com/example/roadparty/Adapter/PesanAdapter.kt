package com.example.roadparty.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roadparty.Database.JenisPakaian
import com.example.roadparty.R

class PesanAdapter(private val list: ArrayList<JenisPakaian>) : RecyclerView.Adapter<PesanAdapter.PesanViewHolder>() {

    inner class PesanViewHolder (itemView: View) :RecyclerView.ViewHolder(itemView){

        fun bind(jenispakaian: JenisPakaian){
            with(itemView){
                val tv_jenis : TextView = findViewById(R.id.tv_jenis)
                val tv_harga : TextView = findViewById(R.id.tv_harga)
                tv_jenis.text = jenispakaian.jenis
                tv_harga.text = jenispakaian.harga
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesanViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_pesan, parent, false)
        return PesanViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PesanViewHolder, position: Int) {
        holder.bind(list[position])
    }

}