package com.example.dispositivosmoviles.ui.activities

import android.content.Intent
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt

import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.dispositivosmoviles.databinding.ActivityBiometricBinding
import com.example.dispositivosmoviles.ui.viewmodels.BiometricViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class BiometricActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBiometricBinding

    private val biometricViewModel by viewModels<BiometricViewModel>()
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBiometricBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.boton.setOnClickListener {
            autenticateBiometric()
        }

        biometricViewModel.isLoading.observe(this){
            isLoading ->

            if(isLoading){
                binding.lytMain.visibility = View.GONE
                binding.lytMainCopia.visibility = View.VISIBLE
            }else{
                binding.lytMain.visibility = View.VISIBLE
                binding.lytMainCopia.visibility = View.GONE
            }


        }

        lifecycleScope.launch {
            biometricViewModel.chargingData()
        }

    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun autenticateBiometric() {

        if (checkBiometric()) {
            val executor = ContextCompat.getMainExecutor(this)

            val biometricPrompt = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticacion requerida")
                .setSubtitle("Ingrese su huella digital")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build()

            //Ejecutor: para lanzar cosas del sistema
            val biometricManager = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun equals(other: Any?): Boolean {
                        return super.equals(other)
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                    }
                })
            biometricManager.authenticate(biometricPrompt)
        } else {
            Snackbar.make(
                binding.boton,
                "No existen los requisitos necesarios",
                Snackbar.LENGTH_LONG
            )
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkBiometric(): Boolean {
        var returnValid: Boolean = false
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                returnValid = true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
                returnValid = false
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
                returnValid = false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
                startActivity(enrollIntent)
                returnValid = false
            }
        }
        return returnValid
    }
}