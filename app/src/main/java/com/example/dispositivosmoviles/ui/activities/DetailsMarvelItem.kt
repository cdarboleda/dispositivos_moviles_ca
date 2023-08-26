package com.example.dispositivosmoviles.ui.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.lifecycle.lifecycleScope
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.example.dispositivosmoviles.databinding.ActivityDetailsMarvelItemBinding
import com.example.dispositivosmoviles.logic.marvelLogic.MarvelLogic
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

//AQUI SE PASA LOS DATOS DE EL ELEMTNO QUE SE SELECCIONA EN LA LISTA DE animes
class DetailsMarvelItem : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsMarvelItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsMarvelItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()

        //aqui recibimos los items de MarvelChars, pero ahora los tomamos como si fueran metadata Jikan
        val item = intent.getParcelableExtra<MarvelChars>("item")

        if (item !== null){
            binding.txtName.text = item.name
            binding.marveltitle.text = item.comic
            Picasso.get().load(item.image).into(binding.imgImage)
            binding.txtDescription.text = item.synopsis

        }

        binding.btnGuardar.setOnClickListener{
            lifecycleScope.launch(Dispatchers.Main){
                withContext(Dispatchers.IO){
                    MarvelLogic().insertMarvelFavChartoDB(item!!, applicationContext)

                }
            }

        }

    }
}