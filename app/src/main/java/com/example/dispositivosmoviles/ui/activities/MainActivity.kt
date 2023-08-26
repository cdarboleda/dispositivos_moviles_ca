package com.example.dispositivosmoviles.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityMainBinding
import com.example.dispositivosmoviles.ui.utilities.DispositivosMoviles


import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    //Para enalzar y utilizar el Binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Los botones con binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun initClass() {
    }


    override fun onStart() {
        super.onStart()
        initClass()
//Cuando instancio accedo a lo que esta dentro de la clase
        //Cuando no instancio accedo a lo que tiene dentro del companion object
    val db = DispositivosMoviles.getDbInstance()

    }
}