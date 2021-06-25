package com.example.roadparty.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.roadparty.Home.MainActivity
import com.example.roadparty.R

class VmencuciseterikaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vmencuciseterika, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vmencuci = VmencuciFragment()
        val mencuci: ImageView = view.findViewById(R.id.mencuci)

        val vseterika = VseterikaFragment()
        val seterika: ImageView = view.findViewById(R.id.seterika)

        val kembali: ImageView = view.findViewById(R.id.back)

        kembali.setOnClickListener {
            val moveIntent = Intent(activity, MainActivity::class.java)
            startActivity(moveIntent)
        }

        mencuci.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.voucherr, vmencuci, VmencuciFragment::class.java.simpleName)
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