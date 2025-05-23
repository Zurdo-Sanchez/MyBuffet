package com.example.mybuffet.repository

import com.example.mybuffet.models.Producto
import com.google.firebase.firestore.FirebaseFirestore

class RepositorioProductos {

    // Suponiendo que usás Firebase Firestore
    private val db = FirebaseFirestore.getInstance()
    private val productosCollection = db.collection("productos")

    fun obtenerProductosPorEvento(eventoId: String, onResult: (List<Producto>) -> Unit) {
        productosCollection
            .whereEqualTo("eventoId", eventoId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val productos = querySnapshot.documents.map { doc ->
                    doc.toObject(Producto::class.java)!!.copy(id = doc.id)
                }
                onResult(productos)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun agregarProducto(producto: Producto, onComplete: (Boolean) -> Unit) {
        productosCollection.add(producto)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
