package com.example.roadparty.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.roadparty.Home.MainActivity
import com.example.roadparty.Home.VoucherActivity
import com.example.roadparty.R
import com.example.roadparty.VoucherSayaActivity


class VmencuciFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vmencuci, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vmencuciseterika = VmencuciseterikaFragment()
        val cuci: ImageView = view.findViewById(R.id.mencuciseterika)
        val voucher: TextView = view.findViewById(R.id.vcsaya)
        voucher.setOnClickListener {
            val move = Intent(activity, VoucherSayaActivity::class.java)
            startActivity(move)
        }

        val vseterika = VseterikaFragment()
        val seterika: ImageView = view.findViewById(R.id.seterika)

        val kembali: ImageView = view.findViewById(R.id.back)

        kembali.setOnClickListener {
            val moveIntent = Intent(activity, MainActivity::class.java)
            startActivity(moveIntent)
        }

        cuci.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.voucherr, vmencuciseterika, VmencuciseterikaFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
        }
        seterika.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.voucherr, vseterika, VseterikaFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

}