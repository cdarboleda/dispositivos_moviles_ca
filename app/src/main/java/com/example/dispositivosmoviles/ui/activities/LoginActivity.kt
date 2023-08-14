package com.example.dispositivosmoviles.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityMainBinding
import com.example.dispositivosmoviles.logic.validator.LoginValidator
import com.example.dispositivosmoviles.ui.utilities.DispositivosMoviles
import com.example.dispositivosmoviles.ui.utilities.MyLocationManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID


// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    //Authentication Firebase
    private lateinit var auth: FirebaseAuth
    private var TAG = Constants.TAG

    //Ubicacion y GPS
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback


    private lateinit var client : SettingsClient
    private lateinit var locationSettingRequest:LocationSettingsRequest

    private var currentLocation: Location? = null
    private val speechToText =
        registerForActivityResult(StartActivityForResult()) { activityResult ->
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

    @SuppressLint("MissingPermission")
    private val locationContract = registerForActivityResult(RequestPermission()) { isGranted ->
        when (isGranted) {
            true ->{
                client.checkLocationSettings(locationSettingRequest).apply {
                    addOnSuccessListener{
                        val task = fusedLocationProviderClient.lastLocation
                        task.addOnSuccessListener { location ->
                            fusedLocationProviderClient.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                Looper.getMainLooper()
                            )
                    }

                }
                    addOnFailureListener{ex ->
                        if (ex is ResolvableApiException){
                            //Para que abra las configuraciones de idioma
                            //
                            //startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))

                            ex.startResolutionForResult(
                                this@MainActivity,
                                LocationSettingsStatusCodes.RESOLUTION_REQUIRED
                            )

                        }

                    }
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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Los botones con binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            2000
        )
            //.setMaxUpdates(3)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (locationResult !== null) {
                    locationResult.locations.forEach { location ->
                        Log.d(
                            "UCE",
                            "Ubicacion: ${location.latitude}, " +
                                    "${location.longitude}"
                        )
                    }
                }
            }
        }

        client = LocationServices.getSettingsClient(this)
        locationSettingRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest).build()


        auth = Firebase.auth
        binding.btnSignup.setOnClickListener {
            //signInWithEmailAndPassword(binding.txtEmail.text.toString(), binding.txtPassword.text.toString())

            authWithFirebaseEmail(binding.txtName.text.toString(),
                binding.txtPass.text.toString())
        }
    }

    //Crea el usuario y el password
    private fun authWithFirebaseEmail(email : String, password : String) {
        auth.createUserWithEmailAndPassword(email, password)
                //Una task es una corrutina que inicia con un invocador
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(Constants.TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    //updateUI(user)
                    Toast.makeText(
                        baseContext,
                        "Authentication successfull.",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(Constants.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //updateUI(null)
                }
            }
    }

    //Iniciar Sesion
    private fun signInWithEmailAndPassword(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //updateUI(null)
                }
            }
    }

    private fun recoveryPasswordWithEmail(email : String){
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {task ->
            if (task.isSuccessful){
                Toast.makeText(
                    baseContext,
                    "Correo de recuperacion enviado correctamente.",
                    Toast.LENGTH_SHORT,
                ).show()

                MaterialAlertDialogBuilder(this).apply {
                    setTitle("Alert")
                    setMessage("Correo de recuperacion enviado correctamente")
                    setCancelable(true)
                }.show()
            }else{

            }

        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }

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


        binding.btnFacebook.setOnClickListener {
            /*
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("tel:099876543")
                //Uri.parse("geo:-0.2006288,-78.5023889")
            )
*/
            fusedLocationProviderClient.removeLocationUpdates(
                locationCallback
            )
            //EÑ EL FINE ME DA LAS CORDENAS DE DONDE SE ENCUENTRA MI CELULAR
            locationContract.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
//          ESTE SIRVE PRA EL BUSCADOR EN GOOGLE
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
                    color = resources.getColor(R.color.red)
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
                "Di algo pe ctm..."
            )

            speechToText.launch(intentSpeech)
        }

    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(
            locationCallback
        )
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

    private fun test(){
        //INYECCION DE DEPENDENCIAS
        var location =  MyLocationManager(this)
        location.getUserLocation()
    }



}