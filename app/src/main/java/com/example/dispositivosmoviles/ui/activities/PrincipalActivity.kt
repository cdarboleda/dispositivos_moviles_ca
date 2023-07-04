package com.example.dispositivosmoviles.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityPrincipalBinding


import com.example.dispositivosmoviles.ui.fragments.FirstFragment
import com.example.dispositivosmoviles.ui.fragments.SecondFragment
import com.example.dispositivosmoviles.ui.fragments.ThirdFragment
import com.example.dispositivosmoviles.ui.utilities.FragmentsManager

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
                Intent(this, MainActivity::class.java))
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_item_wifi -> {
                    FragmentsManager().replaceFragment(supportFragmentManager, binding.frmContainter.id, FirstFragment())
                    true
                }

/*                R.id.menu_item_bluetooth -> {
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
                }*/
                R.id.menu_item_bluetooth -> {
                    FragmentsManager().replaceFragment(supportFragmentManager, binding.frmContainter.id, SecondFragment())
                    true
                }
                R.id.menu_item_settings -> { FragmentsManager().replaceFragment(supportFragmentManager, binding.frmContainter.id, ThirdFragment())
                    true
                }

                else -> false
            }
        }

    }
    override fun onBackPressed(){
        super.onBackPressed();
    }

}