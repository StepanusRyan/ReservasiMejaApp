package com.stepanusryan.reservasimeja.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stepanusryan.reservasimeja.TransaksiActivity
import com.stepanusryan.reservasimeja.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var nomor1 = 1
    private var nomor2 = 2
    private var nomor3 = 3
    private var nomor4 = 4
    private var nomor5 = 5
    private var harga1 = 50000
    private var harga2 = 50000
    private var harga3 = 75000
    private var harga4 = 100000
    private var harga5 = 125000
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtNomor1.text = nomor1.toString()
        binding.txtNomor2.text = nomor2.toString()
        binding.txtNomor3.text = nomor3.toString()
        binding.txtNomor4.text = nomor4.toString()
        binding.txtNomor5.text = nomor5.toString()

        binding.btnMeja1.setOnClickListener {
            val intent = Intent(requireActivity(),TransaksiActivity::class.java)
            intent.putExtra(TransaksiActivity.NOMOR,nomor1.toString())
            intent.putExtra(TransaksiActivity.HARGA,harga1.toString())
            startActivity(intent)
        }
        binding.btnMeja2.setOnClickListener {
            val intent = Intent(requireActivity(),TransaksiActivity::class.java)
            intent.putExtra(TransaksiActivity.NOMOR,nomor2.toString())
            intent.putExtra(TransaksiActivity.HARGA,harga2.toString())
            startActivity(intent)
        }
        binding.btnMeja3.setOnClickListener {
            val intent = Intent(requireActivity(),TransaksiActivity::class.java)
            intent.putExtra(TransaksiActivity.NOMOR,nomor3.toString())
            intent.putExtra(TransaksiActivity.HARGA,harga3.toString())
            startActivity(intent)

        }
        binding.btnMeja4.setOnClickListener {
            val intent = Intent(requireActivity(),TransaksiActivity::class.java)
            intent.putExtra(TransaksiActivity.NOMOR,nomor4.toString())
            intent.putExtra(TransaksiActivity.HARGA,harga4.toString())
            startActivity(intent)

        }
        binding.btnMeja5.setOnClickListener {
            val intent = Intent(requireActivity(),TransaksiActivity::class.java)
            intent.putExtra(TransaksiActivity.NOMOR,nomor5.toString())
            intent.putExtra(TransaksiActivity.HARGA,harga5.toString())
            startActivity(intent)

        }
    }
}