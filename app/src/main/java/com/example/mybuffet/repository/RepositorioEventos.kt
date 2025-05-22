package com.example.mybuffet.repository

import com.example.mybuffet.models.Evento
import com.google.firebase.firestore.FirebaseFirestore

object RepositorioEventos {
    private val db = FirebaseFirestore.getInstance()
    private val eventosRef = db.collection("eventos")

    fun obtenerEventos(
        onSuccess: (List<Evento>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        eventosRef.get()
            .addOnSuccessListener { result ->
                val listaEventos = result.mapNotNull { doc ->
                    doc.toObject(Evento::class.java).copy(id = doc.id)
                }
                onSuccess(listaEventos)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun crearEvento(
        nombre: String,
        onSuccess: (Evento) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val nuevoEvento = Evento(nombre = nombre, estado = 1)
        eventosRef.add(nuevoEvento)
            .addOnSuccessListener { docRef ->
                val eventoConId = nuevoEvento.copy(id = docRef.id)
                onSuccess(eventoConId)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}
