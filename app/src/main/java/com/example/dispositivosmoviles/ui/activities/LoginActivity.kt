package com.example.dispositivosmoviles.ui.activities


import android.Manifest
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.snackbar.Snackbar
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityMainBinding
import com.example.dispositivosmoviles.logic.validator.LoginValidator
import com.example.dispositivosmoviles.ui.utilities.DispositivosMoviles
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    //Para enalzar y utilizar el Binding
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Los botones con binding
        this.binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @SuppressLint("MissingPermission")
    private fun initClass() {
        binding.btnLogin.setOnClickListener {

            val check = LoginValidator().checkLogin(
                binding.txtName.text.toString(), binding.txtPass.text.toString()
            )

            if (check) {
                lifecycleScope.launch(Dispatchers.IO) {
                    saveDataStore(binding.txtName.text.toString())
                }

                var intent = Intent(this, PrincipalActivity::class.java)
                intent.putExtra("var1", binding.txtName.text.toString())
                intent.putExtra("var2", 2)
                startActivity(intent)
            } else {
                var snackbar = Snackbar.make(
                    binding.txtTitle,
                    "Usuario o contraseña inválidos",
                    Snackbar.LENGTH_LONG
                )
                //snackbar.setBackgroundTint(ContextCompat.getColor(binding.root.context, R.color.principal_color_dm))
                snackbar.setBackgroundTint(getResources().getColor(R.color.black))
                snackbar.show()
            }
        }

        val locationContract = registerForActivityResult(RequestPermission()) { isGranted ->
            when (isGranted) {
                true -> {
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                        it.longitude
                        it.latitude
                        val a = Geocoder(this)
                        a.getFromLocation(
                            it.latitude,
                            it.longitude, 1
                        )
                    }
                }

                shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) -> {

                }
                false -> {
                }
            }
        }

        binding.btnFacebook.setOnClickListener {
            /*
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("tel:099876543")
                //Uri.parse("geo:-0.2006288,-78.5023889")
            )
*/
            //EÑ EL FINE ME DA LAS CORDENAS DE DONDE SE ENCUENTRA MI CELULAR
            locationContract.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//       ESTE SIRVE PRA EL BUSCADOR EN GOOGLE
//            val intent = Intent(Intent.ACTION_WEB_SEARCH)
//            intent.setClassName(
//                "com.google.android.googlequicksearchbox",
//                "com.google.android.googlequicksearchbox.SearchActivity"
//            )
//            intent.putExtra(SearchManager.QUERY, "google.com")
//
//            startActivity(intent)
        }

        val appResultLocal = registerForActivityResult(
            StartActivityForResult()
        ) { resultActivity ->
            var color = resources.getColor(R.color.black)
            var message = when (resultActivity.resultCode) {
                RESULT_OK -> {
                    color = resources.getColor(R.color.blue)
                    "Resultado Exitoso"
                    resultActivity.data?.getStringExtra("result").orEmpty()
                }
                RESULT_CANCELED -> {
                    color = resources.getColor(R.color.orange)
                    "Resultado Fallido"
                    resultActivity.data?.getStringExtra("result").orEmpty()
                }
                else -> {
                    "Resultado Dudoso"
                }
            }

            val sn = Snackbar.make(binding.btnFacebook, message, Snackbar.LENGTH_LONG)
            sn.setBackgroundTint(color)
            sn.show()

        }

        val speechToText = registerForActivityResult(StartActivityForResult()) { activityResult ->
            var color = resources.getColor(R.color.blue)
            var msg = ""
            when (activityResult.resultCode) {
                RESULT_OK -> {
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

                RESULT_CANCELED -> {
                    msg = "Proceso Cancelado"
                    color = resources.getColor(R.color.orange)
                }

                else -> {
                    msg = "Ocurrio un error"
                    color = resources.getColor(R.color.red)
                }
            }

            val sn = Snackbar.make(binding.btnFacebook, msg, Snackbar.LENGTH_LONG)
            sn.setBackgroundTint(color)
            sn.show()
        }


        binding.btnTwitter.setOnClickListener {
            //val resIntent = Intent(this, ResultActivity::class.java)
            //appResultLocal.launch(resIntent)

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
                "Di algo"
            )

            speechToText.launch(intentSpeech)
        }

    }


    override fun onStart() {
        super.onStart()
        initClass()
//Cuando instancio accedo a lo que esta dentro de la clase
        //Cuando no instancio accedo a lo que tiene dentro del companion object
        val db = DispositivosMoviles.getDbInstance()

    }

    private suspend fun saveDataStore(stringData: String) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey("usuario")] = stringData
            prefs[stringPreferencesKey("session")] = UUID.randomUUID().toString()
            prefs[stringPreferencesKey("email")] = "dispositivosmoviles@uce.edu.ec"

        }
    }

}