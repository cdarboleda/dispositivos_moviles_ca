package com.example.dispositivosmoviles.data.endpoints

import com.example.dispositivosmoviles.data.entities.marvel.characters.MarvelApiChars
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//Aqui se pasan parametros para el URL del api
interface MarvelEndpoint {
    @GET("characters")
    fun getCharactersStartWith(
        @Query("nameStartsWith") name : String,
        @Query("limit") limit : Int,
        @Query("ts") ts : String = "uce",
        @Query("apikey") apikey : String = "48ed26ff242038147ce24450236a7ec2",
        @Query("hash") hash : String = "f00af94ad24dd1d56b2ea26ae903030e"
    ) : Response<MarvelApiChars>
}