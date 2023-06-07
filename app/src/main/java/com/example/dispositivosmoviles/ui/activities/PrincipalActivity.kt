package com.example.dispositivosmoviles.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.dispositivosmoviles.R

import com.example.dispositivosmoviles.databinding.ActivityPrincipalBinding
import com.google.android.material.snackbar.Snackbar

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        var name: String = " "
        /*
        intent.extras.let {
            name= it?.getString("var1")!!
        */
        Log.d("UCE", "Hola ${name}")
        binding.txtTitle.text = "Bienvenido " + name.toString()

        binding.btnRetorno.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_item_wifi -> {
                    var suma :Int = 0
                    for (i in 1..10){
                        suma = suma+i;
                    }
                    // Respond to navigation item 1 click
                    var s = Snackbar.make(binding.txtTitle,
                        "La suma es ${suma}",
                        Snackbar.LENGTH_LONG)

                    s.setBackgroundTint(ContextCompat.getColor(binding.root.context, R.color.principal_color_dm))

                       s.show()
                    true
                }

                R.id.menu_item_bluetooth -> {
                    // Respond to navigation item 2 click
                    var suma : Int = 0;
                    for (i in listOf(8,12,13)){
                        suma = suma + i;
                    }
                    var s = Snackbar.make(binding.txtTitle,
                        "La suma es ${suma}",
                        Snackbar.LENGTH_LONG)

                    s.setBackgroundTint(ContextCompat.getColor(binding.root.context, R.color.principal_color_dm))

                    s.show()
                    true
                }
                R.id.menu_item_settings -> {
                    // Respond to navigation item 2 click
                    true
                }

                else -> false
            }
        }

    }


}