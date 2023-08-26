package com.example.dispositivosmoviles.logic.jikanLogic

import android.util.Log
import com.example.dispositivosmoviles.data.connections.ApiConnection
import com.example.dispositivosmoviles.data.endpoints.JikanEndpoint
import com.example.dispositivosmoviles.logic.data.JikanChars

class JikanAnimeLogic {

    suspend fun getAllAnimes():ArrayList<JikanChars> {

        var itemList = arrayListOf<JikanChars>()

        var call = ApiConnection.getService(
            ApiConnection.typeApi.Jikan,
            JikanEndpoint::class.java
        )

        if (call != null) {
            val response = call.getAllAnimes()
            if (response.isSuccessful) {
                response.body()!!.data.forEach {
                    val m = JikanChars(
                        it.mal_id,
                        it.title,
                        it.rating,
                        it.synopsis,
                        it.images.jpg.image_url
                    )
                    itemList.add(m)
                }
            } else {
                Log.d("UCE", response.toString())
            }
        }
        return itemList
    }
}