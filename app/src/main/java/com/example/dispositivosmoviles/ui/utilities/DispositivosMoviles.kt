package com.example.dispositivosmoviles.ui.utilities

import android.app.Application
import androidx.room.Room
import com.example.dispositivosmoviles.data.connections.MarvelConnectionDB
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelCharsDB

class DispositivosMoviles : Application() {

    //Room nos crea una instancia de la base de datos
    //con el contexto "applicationContext"
    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(applicationContext,
            MarvelConnectionDB::class.java,
            "marvelDB"

        ).fallbackToDestructiveMigration()
            .build()
    }

    companion object{
        private var db : MarvelConnectionDB? = null

        fun getDbInstance():MarvelConnectionDB {
            //!! significa que nunca va a ser nula
            return db!!

        }
    }


}