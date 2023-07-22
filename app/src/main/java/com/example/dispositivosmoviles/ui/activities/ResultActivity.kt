package com.example.dispositivosmoviles.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dispositivosmoviles.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.btnResultOk.setOnClickListener {
            val i = Intent()
            i.putExtra("result", "Resultado Exitoso")
            setResult(RESULT_OK, i)
            finish()
        }

        binding.btnResultFalse.setOnClickListener {
            val i = Intent()
            i.putExtra("result", "Resultado Fallido")
            setResult(RESULT_CANCELED, i)
            finish()
        }

    }
}