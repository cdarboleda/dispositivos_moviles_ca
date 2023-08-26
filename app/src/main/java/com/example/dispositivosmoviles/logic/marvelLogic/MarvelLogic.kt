package com.example.dispositivosmoviles.logic.marvelLogic

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.dispositivosmoviles.data.connections.ApiConnection
import com.example.dispositivosmoviles.data.endpoints.MarvelEndpoint
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelCharsDB
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelCharsFavoritesDB
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.convertMarvelCharsDBtoNormal
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.convertMarvelCharsFavDBtoNormal

import com.example.dispositivosmoviles.data.entities.marvel.characters.getMarvelCharsApiCustom
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.example.dispositivosmoviles.logic.data.convertMarvelCharsNormaltoDB
import com.example.dispositivosmoviles.logic.data.convertMarvelCharsNormaltoFavDB

import com.example.dispositivosmoviles.ui.utilities.DispositivosMoviles
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MarvelLogic {


    suspend fun getMarvelCharsApiStartsWith(name: String, limit: Int): ArrayList<MarvelChars> {

        var itemsList = arrayListOf<MarvelChars>()

        val call = ApiConnection.getService(
            ApiConnection.typeApi.Marvel,
            MarvelEndpoint::class.java
        )

        if (call != null) {
            val response = call.getCharactersStartWith(name, limit)
            if (response.isSuccessful) {
                response.body()!!.data.results.forEach {
                    val m = it.getMarvelCharsApiCustom()
                    itemsList.add(m)
                }
            } else {
                Log.d("UCE", response.toString())
            }
        }
        return itemsList
    }

    suspend fun getAllMarvelCharsApiInit(context: Context): MutableList<MarvelChars> {
        var offset = 0
        val limit = 100
        var items = mutableListOf<MarvelChars>()
        items = MarvelLogic().getAllMarvelCharsDB().toMutableList()
        if(items.isEmpty()) {
            while(offset <= 1600){
                try {
                    var list = MarvelLogic().getAllMarvelCharsApi(offset,limit,context)
                    MarvelLogic().insertMarvelCharstoDB(list)
                    withContext(Dispatchers.Main) {
                        Log.d("UCE", "Se obtuvo de la API hacia la DB, offset: ${offset}, y limit: ${limit}")
                        Toast.makeText(context, "Se obtuvo de la API hacia la DB, offset: ${offset}, y limit: ${limit}", Toast.LENGTH_SHORT).show()
                    }
                }catch (ex: Exception){
                    offset = 5000
                    withContext(Dispatchers.Main) {
                        Log.e("UCE", "Hubo un Error la API hacia la DB, offset: ${offset}, y limit: ${limit}")
                        Toast.makeText(context, "Hubo un Error la API hacia la DB, offset: ${offset}, y limit: ${limit}", Toast.LENGTH_SHORT).show()
                    }
                }
                offset+=limit
            }

        }
        items = MarvelLogic().getAllMarvelCharsDB().toMutableList()
        return items
    }
    suspend fun getAllMarvelCharsApi(offset: Int, limit: Int, context: Context): ArrayList<MarvelChars> {
        val itemList = arrayListOf<MarvelChars>()
        var call =
            ApiConnection.getService(
                ApiConnection.typeApi.Marvel,
                MarvelEndpoint::class.java
            )

        if (call != null) {
            val response = call.getAllMarvelChars(offset, limit)
            Log.d("UCE", "LLAMADA AL API TODOS"+response.toString())
            if (response.isSuccessful) {
                response.body()!!.data.results.forEach() {
                    val m = it.getMarvelCharsApiCustom()
                    itemList.add(m)
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Se obtuvo de la API", Toast.LENGTH_SHORT).show()
                }

            } else {
                Log.d("UCE", response.toString())
            }

        }
        return itemList
    }

    suspend fun getAllMarvelCharsDB(): MutableList<MarvelChars> {
        var items: ArrayList<MarvelChars> = arrayListOf()
        DispositivosMoviles.getDbInstance().marvelDao().getAllCharacters().forEach {
            items.add(
                it.convertMarvelCharsDBtoNormal()
            )
        }
        return items
    }

    suspend fun getAllMarvelFavCharsDB(): MutableList<MarvelChars> {
        var items: ArrayList<MarvelChars> = arrayListOf()
        DispositivosMoviles.getDbInstance().marvelDao().getAllFavCharacters().forEach {
            items.add(
                it.convertMarvelCharsFavDBtoNormal()
            )
        }
        return items
    }

    suspend fun  getInitChars(offset: Int,limit: Int, context: Context):MutableList<MarvelChars>{
        var items = mutableListOf<MarvelChars>()
        try{
            items = MarvelLogic()
                .getAllMarvelCharsDB()
                .toMutableList()

            if(items.isEmpty() || items.size <= 20){
                items = (MarvelLogic().getAllMarvelCharsApi(
                    offset = offset,limit = limit, context
                ))
                MarvelLogic().insertMarvelCharstoDB(items)
                Log.d("UCE", "LLAMADA AL API TODOS e insercion")
            }
            else{
                Log.d("UCE", "No entro en el empty ni size <20 Initchars")
            }
        }catch (ex: Exception){
            Log.d("UCE", ""+"${ex.message}")
        }
        return items
    }

    suspend fun  getInitFavChars(context: Context):MutableList<MarvelChars>{
        var items = mutableListOf<MarvelChars>()

            items = MarvelLogic()
                .getAllMarvelFavCharsDB()
                .toMutableList()
        if(items.isEmpty()){
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Aún no tienes favoritos", Toast.LENGTH_SHORT).show()
            }
        }
        return items
    }
    suspend fun insertMarvelCharstoDB(items: List<MarvelChars>) {
        var itemsDB = arrayListOf<MarvelCharsDB>()
        items.forEach {
            itemsDB.add(
                it.convertMarvelCharsNormaltoDB()
            )
        }
        try {
            DispositivosMoviles.getDbInstance().marvelDao().insertMarvelCharacter(itemsDB)
        }catch (ex: Exception){
            Log.d("UCE", "${ex.message}")
        }

    }

    suspend fun insertMarvelFavChartoDB(item: MarvelChars, context : Context) {
        val itemDB =  item.convertMarvelCharsNormaltoFavDB()
        try{
            DispositivosMoviles.getDbInstance().marvelDao().insertMarvelFavCharacter(itemDB)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Ahora ${item.name} es uno de tus favoritos", Toast.LENGTH_SHORT).show()
            }
        }catch (ex: Exception){
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Ya es tu favorito", Toast.LENGTH_SHORT).show()
            }
        }

    }

    suspend fun deleteMarvelFavChartoDB(id : Int, context : Context) {
        try{
            DispositivosMoviles.getDbInstance().marvelDao().deleteMarvelFavCharacterById(id)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show()
            }
        }catch (ex: Exception){
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Hubo un problema al tratar de borrarlo", Toast.LENGTH_SHORT).show()
            }
        }

    }



}