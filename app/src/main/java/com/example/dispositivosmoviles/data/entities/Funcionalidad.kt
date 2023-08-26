package com.example.dispositivosmoviles.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Funcionalidad(val nombreClass: String, val titulo: String, val descripcion: String, val imagen : String) :
    Parcelable