package com.example.dispositivosmoviles.logic.marvelLogic

import android.util.Log
import com.example.dispositivosmoviles.data.connections.ApiConnection
import com.example.dispositivosmoviles.data.endpoints.MarvelEndpoint
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelCharsDB
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.getMarvelChars

import com.example.dispositivosmoviles.data.entities.marvel.characters.getMarvelChars
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.example.dispositivosmoviles.logic.data.getMarvelCharsDB

import com.example.dispositivosmoviles.ui.utilities.DispositivosMoviles
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException

class MarvelLogic {

    suspend fun getMarvelChars(name: String, limit: Int): ArrayList<MarvelChars> {

        var itemsList = arrayListOf<MarvelChars>()

        val call = ApiConnection.getService(
            ApiConnection.typeApi.Marvel,
            MarvelEndpoint::class.java
        )

        if (call != null) {
            val response = call.getCharactersStartWith(name, limit)
            if (response.isSuccessful) {
                response.body()!!.data.results.forEach {
                    val m = it.getMarvelChars()
                    itemsList.add(m)
                }
            } else {
                Log.d("UCE", response.toString())
            }
        }
        return itemsList
    }

    suspend fun getAllMarvelCharacters(offset: Int, limit: Int): ArrayList<MarvelChars> {
        val itemList = arrayListOf<MarvelChars>()
        var call =
            ApiConnection.getService(
                ApiConnection.typeApi.Marvel,
                MarvelEndpoint::class.java
            )

        if (call != null) {
            val response = call.getAllMarvelChars(offset, limit)
            Log.d("UCE", response.toString())
            if (response.isSuccessful) {
                response.body()!!.data.results.forEach() {
                    val m = it.getMarvelChars()
                    itemList.add(m)
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
                it.getMarvelChars()
            )
        }
        return items
    }

    suspend fun getInitChar(offset: Int, limit: Int): MutableList<MarvelChars> {
        var items = mutableListOf<MarvelChars>()
        try {
            items = MarvelLogic()
                .getAllMarvelCharsDB()
                .toMutableList()
            if (items.isEmpty()) {
                items = (MarvelLogic()
                    .getAllMarvelCharacters(
                        offset,
                        limit
                    ))
            }
        } catch (ex: Exception) {
            throw RuntimeException(ex.message)
        } finally {
            return items
        }
    }

    suspend fun insertMarvelCharstoDB(items: List<MarvelChars>) {
        var itemsDB = arrayListOf<MarvelCharsDB>()
        items.forEach {
            itemsDB.add(
                it.getMarvelCharsDB()
            )
        }
        DispositivosMoviles.getDbInstance().marvelDao().insertMarvelCharacter(
            itemsDB
        )
    }

    suspend fun  getInitChars(limit: Int, offset: Int):MutableList<MarvelChars>{
        var items = mutableListOf<MarvelChars>()
        try{
            items = MarvelLogic()
                .getAllMarvelCharsDB()
                .toMutableList()

            if(items.isEmpty()){
                items = (MarvelLogic().getAllMarvelCharacters(
                    offset = offset,limit
                ))
                MarvelLogic().insertMarvelCharstoDB(items)
            }

        }catch (ex: Exception){
            throw RuntimeException(ex.message)
        }
        return items
    }

}