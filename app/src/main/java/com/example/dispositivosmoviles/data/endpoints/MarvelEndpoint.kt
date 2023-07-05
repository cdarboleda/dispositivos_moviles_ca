package com.example.dispositivosmoviles.data.endpoints

import com.example.dispositivosmoviles.data.entities.marvel.characters.MarvelApiChars
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//Aqui se pasan parametros para el URL del api
interface MarvelEndpoint {
    @GET("characters")
    suspend fun getCharactersStartWith(
        @Query("nameStartsWith") name : String,
        @Query("limit") limit : Int,
        @Query("ts") ts : String = "uce1",//"uce",
        @Query("apikey") apikey : String = "48ed26ff242038147ce24450236a7ec2",
        @Query("hash") hash : String = "e39fb11ad271b98d8cac028063ce639b"//"f00af94ad24dd1d56b2ea26ae903030e"
    ) : Response<MarvelApiChars>
}