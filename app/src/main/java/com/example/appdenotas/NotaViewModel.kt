package com.example.appdenotas

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel

class NotaViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    var ordenar: String
        get() = sharedPreferences.getString("ordenar", getApplication<Application>().getString(R.string.ordenar_fecha))!!
        set(value) = sharedPreferences.edit().putString("ordenar", value).apply()

    var ver: String
        get() = sharedPreferences.getString("ver", getApplication<Application>().getString(R.string.ver_lista))!!
        set(value) = sharedPreferences.edit().putString("ver", value).apply()
}
