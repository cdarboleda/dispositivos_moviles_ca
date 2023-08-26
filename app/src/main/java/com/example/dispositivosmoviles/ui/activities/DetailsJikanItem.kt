package com.example.dispositivosmoviles.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityDetailsJikanItemBinding
import com.example.dispositivosmoviles.logic.data.JikanChars
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class DetailsJikanItem : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsJikanItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsJikanItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    override fun onStart() {
        super.onStart()
        val item = intent.getParcelableExtra<JikanChars>("item")

        if (item !== null){
            binding.txtName.text = item.name
            binding.marveltitle.text = item.comic
            Picasso.get().load(item.image).into(binding.imgImage)
            binding.txtDescription.text = item.synopsis

        }
    }
}