package com.example.dispositivosmoviles.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.dispositivosmoviles.R

import com.example.dispositivosmoviles.databinding.ActivityPrincipalBinding

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)    }

    private fun initClass() {
        binding.btnRetorno.setOnClickListener {
            startActivity(Intent(this,
                MainActivity::class.java))
        }
    }
    override fun onStart() {
        super.onStart()
        var name : String =" "
        intent.extras.let {
            name= it?.getString("var1")!!
        }
        Log.d("UCE","Hola ${name}")
        binding.txtTitle.text = "Bienvenido "+name.toString()
        initClass()

    }





}