package com.example.dispositivosmoviles.data.entities.marvel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MarvelChars(
    val id: Int,
    val name: String,
    val comic: String,
    val synopsis: String,
    val image: String
) : Parcelable