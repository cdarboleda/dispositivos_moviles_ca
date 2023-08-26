package com.example.dispositivosmoviles.ui.activities

import android.app.SearchManager
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.result.contract.ActivityResultContracts
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivitySpeechToTextBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class SpeechToTextActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySpeechToTextBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpeechToTextBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.btnStart.setOnClickListener{
            val intentSpeech = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            intentSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intentSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )
            intentSpeech.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "Qué deseas saber?"
            )

            speechToText.launch(intentSpeech)
        }
    }

    //Speech to text
    val speechToText = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        var color = resources.getColor(R.color.blue)
        var msg = ""
        when (activityResult.resultCode) {
            AppCompatActivity.RESULT_OK -> {
                msg = activityResult
                    .data
                    ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    ?.get(0)
                    .toString()
                if (msg.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_WEB_SEARCH)
                }

                intent.setClassName(
                    "com.google.android.googlequicksearchbox",
                    "com.google.android.googlequicksearchbox.SearchActivity"
                )
                intent.putExtra(SearchManager.QUERY, msg)

                startActivity(intent)
            }

            AppCompatActivity.RESULT_CANCELED -> {
                msg = "Proceso Cancelado"
                color = resources.getColor(R.color.orange)
            }

            else -> {
                msg = "Ocurrio un error"
                color = resources.getColor(R.color.red)
            }
        }

        val sn = Snackbar.make(binding.btnStart, msg, Snackbar.LENGTH_LONG)
        sn.setBackgroundTint(color)
        sn.show()
    }

}