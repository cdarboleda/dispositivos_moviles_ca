package com.example.dispositivosmoviles.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityPrincipalBinding


import com.example.dispositivosmoviles.ui.fragments.FirstFragment
import com.example.dispositivosmoviles.ui.fragments.SecondFragment
import com.example.dispositivosmoviles.ui.fragments.ThirdFragment
import com.example.dispositivosmoviles.ui.utilities.FragmentsManager

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding
    private var currentFragment: Fragment? = null
    private var firstFragment: FirstFragment = FirstFragment()
    private var secondFragment: SecondFragment? = null
    private var thirdFragment: ThirdFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FragmentsManager().replaceFragment(
            supportFragmentManager,
            binding.frmContainter.id,
            firstFragment
        )
    }

    override fun onStart() {
        super.onStart()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.marvel -> {
                    currentFragment = firstFragment
                    Log.d("UCE", "Dentro del firstFragment")
                }

                R.id.marvel2 -> {
                    Log.d("UCE", "Dentro del secondFragment")
                    if (secondFragment==null) {
                        Log.d("UCE", "second == null")
                        secondFragment = SecondFragment()
                    }
                    currentFragment = secondFragment
                }

                R.id.anime -> {
                    Log.d("UCE", "Dentro del thirdFragment")
                    if (thirdFragment==null) {
                        Log.d("UCE", "third == null")
                        thirdFragment = ThirdFragment()

                    }
                    currentFragment = thirdFragment
                }

                else -> currentFragment ?: FirstFragment()
            }

            FragmentsManager().replaceFragment(
                supportFragmentManager,
                binding.frmContainter.id,
                currentFragment!!
            )
            true
        }
    }

    override fun onBackPressed() {
        finish()
    }

}