package com.example.dispositivosmoviles.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityMainBinding
import com.example.dispositivosmoviles.ui.validator.LoginValidator


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
        binding.btnLogin.setOnClickListener {

            val check = LoginValidator().checkLogin(
                binding.txtName.text.toString(), binding.txtPass.text.toString()
            )

            if(check){
                var intent = Intent(
                    this,
                    PrincipalActivity::class.java
                )
                intent.putExtra(
                    "var1",
                    // binding.txtBuscar.text.toString())
                    binding.txtName.text.toString()
                )
                intent.putExtra("var2", 2)
                startActivity(intent)
            }else{
                var snackbar = Snackbar.make(binding.txtTitle,
                    "Usuario o contraseña inválidos",
                    Snackbar.LENGTH_LONG)
                snackbar.setBackgroundTint(ContextCompat.getColor(binding.root.context, R.color.principal_color_dm))

                snackbar.show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        initClass()

    }
}