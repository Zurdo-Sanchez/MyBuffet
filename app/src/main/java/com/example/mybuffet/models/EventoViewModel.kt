package com.example.mybuffet.models

import androidx.lifecycle.ViewModel
import com.example.mybuffet.repository.RepositorioEventos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.firebase.firestore.FirebaseFirestore

class EventoViewModel : ViewModel() {

    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())
    val eventos: StateFlow<List<Evento>> get() = _eventos

    // 🔹 Agregamos eventoSeleccionado para compartirlo entre pantallas
    var eventoSeleccionado: Evento? = null

    fun cargarEventos(onResultado: (List<Evento>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("eventos")
            .get()
            .addOnSuccessListener { result ->
                val lista = result.map { doc ->
                    Evento(
                        id = doc.id,
                        nombre = doc.getString("nombre") ?: "",
                        estado = (doc.getLong("estado") ?: 1).toInt()
                    )
                }
                onResultado(lista)
            }
            .addOnFailureListener {
                onResultado(emptyList())
            }
    }

    fun crearEvento(nombre: String, onSuccess: (Evento) -> Unit, onFailure: (Exception) -> Unit) {
        RepositorioEventos.crearEvento(nombre, onSuccess, onFailure)
    }
}
