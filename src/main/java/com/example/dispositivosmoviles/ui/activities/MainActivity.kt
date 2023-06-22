package com.example.dispositivosmoviles.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.dispositivosmoviles.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    //Para enalzar y utilizar el Binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Los botones con binding
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Los button con findViewById
        /*
        setContentView(R.layout.activity_main)
        var button1 = findViewById<Button>(R.id.button1)

        var txtBuscar = findViewById(R.id.txt_buscar) as TextView
        button1.text = "Ingresar"
        button1.editableText.clear()
        button1.setOnClickListener{
            txtBuscar.text = "El evento se ha ejecutado!!!!"

            /*
            //El contexto las tienen las activity y la aplicación (applicationContext)
            Toast.makeText(this,
                "Ingresado con éxito",
                Toast.LENGTH_SHORT
            ).show()
            */
            var f = Snackbar.make(button1, "Otro mensaje", Snackbar.LENGTH_LONG)


            f.setBackgroundTint(getResources().getColor(android.R.color.holo_blue_light))
            f.show()
        }
    */

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun initClass(){
        binding.button1.setOnClickListener{
            binding.txtBuscar.text = "El codigo ejecuta  correctamente"
            var f = Snackbar.make(binding.button1
                , "Otro mensaje",
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