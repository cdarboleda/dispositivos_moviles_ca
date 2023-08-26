package com.example.dispositivosmoviles.logic.lists

import com.example.dispositivosmoviles.data.entities.Funcionalidad

class ListFuncionalidad {

    fun getData(): List<Funcionalidad> {
        val data = arrayListOf<Funcionalidad>(
            Funcionalidad("com.example.dispositivosmoviles.ui.activities.SpeechToTextActivity",
                "Voz a texto","Le hablamos a google y este buscará lo que digamos",
            "speech_background"),
            Funcionalidad("com.example.dispositivosmoviles.ui.activities.MapsLocationActivity",
                "Ubicación en Google Maps","Se abrirá la aplicación de Google Maps con nuestras coordenadas",
            "maps_background"),
            Funcionalidad("com.example.dispositivosmoviles.ui.activities.PrincipalActivity",
                "Apis Jikan y Marvel","La Principal Activity con los fragments y las APIs de Marvel y Anime",
                "principal_background"),
            Funcionalidad("com.example.dispositivosmoviles.ui.activities.NotificationActivity",
                "Alarma con notificación","Podemos programar una alarma que aparecerá en una notificación",
                "notification_background"),
            Funcionalidad("com.example.dispositivosmoviles.ui.activities.CameraActivity",
                "Cámara","Podemos programar una alarma que aparecerá en una notificación",
                "camera_background"),
            Funcionalidad("com.example.dispositivosmoviles.ui.activities.BiometricActivity",
                "Autenticación con huella digital","Se puede utilizar la capacidad biométrica del teléfono",
                "biometric_background"),
            Funcionalidad("com.example.dispositivosmoviles.ui.activities.ChatGPT",
                "ChatGPT","Preguntamos alguna duda a una inteligencia artificial",
                "ia_background"),
            )
        return data

    }
}