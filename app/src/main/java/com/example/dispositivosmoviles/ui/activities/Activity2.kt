package com.example.dispositivosmoviles.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.Activity2Binding
import com.google.android.material.snackbar.Snackbar

class Activity2 : AppCompatActivity() {

    private lateinit var binding: Activity2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= Activity2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initClass(){
        binding.button1.setOnClickListener{

            var f = Snackbar.make(binding.button1
                , "hola soy un texto de prueba",
                Snackbar.LENGTH_LONG)

            f.setBackgroundTint(getResources().getColor(android.R.color.holo_blue_light))
            f.show()
        }
    }

    override fun onStart() {
        super.onStart()
        initClass()

    }
}