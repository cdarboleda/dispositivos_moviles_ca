package com.example.dispositivosmoviles.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.data.entities.Funcionalidad
import com.example.dispositivosmoviles.databinding.ActivityMenuBinding
import com.example.dispositivosmoviles.logic.lists.ListFuncionalidad
import com.example.dispositivosmoviles.ui.adapters.BotonesAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private var rvAdapter: BotonesAdapter = BotonesAdapter()

    //Backpressed
    private var backPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        binding.lblLytUser.setHint(email)
        binding.lblUser.isEnabled = false
        initClass()

        // Guardado de datos aunque se detenga la app
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.apply()
    }

    private fun initClass(){
        binding.logOutButton.setOnClickListener{
            //Borrado de datos
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
            FirebaseAuth.getInstance().signOut()
            super.onBackPressed()
        }

        chargeDataRV()
    }
    override fun onStart() {

        super.onStart()
    }


    private fun chargeDataRV() {
        lifecycleScope.launch(Dispatchers.IO) {
            rvAdapter.items = ListFuncionalidad().getData()

            withContext(Dispatchers.Main) {
                with(binding.rvBotones) {
                    this.adapter = rvAdapter
                    this.layoutManager = LinearLayoutManager(
                        this@MenuActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            finishAffinity();
            System.exit(0);
        }else{
            Toast.makeText(applicationContext, "Presiona una vez más para salir de la app", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

}