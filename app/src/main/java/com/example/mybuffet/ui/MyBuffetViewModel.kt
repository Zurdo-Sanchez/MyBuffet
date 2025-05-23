package com.example.mybuffet.ui

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.mybuffet.models.Evento

class MyBuffetViewModel : ViewModel() {
    var eventos by mutableStateOf<List<Evento>>(emptyList())
        private set

    var eventoSeleccionado by mutableStateOf<Evento?>(null)
        private set

    var usuarioLogueado by mutableStateOf(false)
        private set

    var username by mutableStateOf("")
        private set

    fun login(user: String) {
        usuarioLogueado = true
        username = user
    }

    fun seleccionarEvento(evento: Evento) {
        eventoSeleccionado = evento
    }

    fun cerrarSesion() {
        usuarioLogueado = false
        username = ""
        eventoSeleccionado = null
        eventos = emptyList()
    }

    // Si necesitás actualizar la lista de eventos, usá directamente:
    // viewModel.eventos = nuevosEventos
}
