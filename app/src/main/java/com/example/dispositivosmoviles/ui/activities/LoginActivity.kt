package com.example.dispositivosmoviles.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityMainBinding
import com.example.dispositivosmoviles.ui.utilities.DispositivosMoviles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    //Authentication Firebase
    private lateinit var auth: FirebaseAuth
    private var TAG = Constants.TAG
    private lateinit var binding: ActivityMainBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(2000)
        setTheme(R.style.Theme_DispositivosMoviles)

        super.onCreate(savedInstanceState)
        //Los botones con binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        session()
    }
    private fun session(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        if(email != null){ showHome(email) }

    }
    private fun showHome(email : String){
        var intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showSuccesful() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Genial!")
        builder.setMessage("Su cuenta se ha creado con éxito")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun initClass() {
        binding.btnLogin.setOnClickListener {
            if (binding.txtEmail.text.toString().isNotBlank() && binding.txtPassword.text.toString().isNotBlank()){
                auth.signInWithEmailAndPassword(binding.txtEmail.text.toString(), binding.txtPassword.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInWithEmail:success")
                            showHome(task.result.user?.email?:"")
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            showAlert()
                        }
                    }
            }

        }
        binding.btnSignup.setOnClickListener {
            if (binding.txtEmail.text!!.isNotEmpty() && binding.txtPassword.text!!.isNotEmpty()){
                auth.createUserWithEmailAndPassword(binding.txtEmail.text.toString(), binding.txtPassword.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d(Constants.TAG, "createUserWithEmail:success")
                            showSuccesful()
                        } else {
                            Log.w(Constants.TAG, "createUserWithEmail:failure", task.exception)
                            showAlert()
                        }
                    }

            }
        }
        binding.btnFacebook.setOnClickListener {
            val facebookUrl = "https://www.facebook.com"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl))
            startActivity(intent)
        }
        binding.btnTwitter.setOnClickListener {
            val twitterUrl = "https://twitter.com/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl))
            startActivity(intent)
        }
    }


    override fun onStart() {
        super.onStart()
        initClass()
        //Cuando instancio accedo a lo que esta dentro de la clase
        //Cuando no instancio accedo a lo que tiene dentro del companion object
        val db = DispositivosMoviles.getDbInstance()

    }



}