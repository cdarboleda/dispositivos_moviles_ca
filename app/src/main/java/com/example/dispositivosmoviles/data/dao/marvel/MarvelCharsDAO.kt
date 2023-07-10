package com.example.dispositivosmoviles.data.dao.marvel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelCharsDB
import com.example.dispositivosmoviles.logic.data.MarvelChars

@Dao
interface MarvelCharsDAO {
    @Query("select * from MarvelCharsDB")
    fun getAllCharacters():List<MarvelCharsDB>

    @Query("select * from MarvelCharsDB where id= :id")
    fun getOneCharacter(id: Int) : MarvelCharsDB

    @Insert
    fun insertMarvelCharacter(ch:List<MarvelCharsDB>)



}