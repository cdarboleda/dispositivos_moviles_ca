package com.example.dispositivosmoviles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //R nos da acceso a todos los recursos que tenemos en la carpeta res
        setContentView(R.layout.activity_main)

        var button1 = findViewById<Button>(R.id.btnLogin)
        var txtBuscar = findViewById(R.id.txt_name) as TextView
        button1.text = "Ingresar"
        button1.setOnClickListener{
            txtBuscar.text = "El evento se ha ejecutado!!!!"

            /*
            //El contexto las tienen las activity y la aplicación (applicationContext)
            Toast.makeText(this,
                "Ingresado con éxito",
                Toast.LENGTH_SHORT
            ).show()
            */
            var f = Snackbar.make(button1, "Otro mensaje", Snackbar.LENGTH_SHORT)


            f.setBackgroundTint(getResources().getColor(android.R.color.holo_blue_light))
            f.show()
        }


    }

    override fun onStart() {
        super.onStart()


    }
}