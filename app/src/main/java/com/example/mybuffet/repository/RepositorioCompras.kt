package com.example.mybuffet.repository

import com.example.mybuffet.models.Compra

import com.google.firebase.firestore.FirebaseFirestore

object RepositorioCompras {

    private val db = FirebaseFirestore.getInstance()

    fun obtenerCompras(
        onSuccess: (List<Compra>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("compras")
            .get()
            .addOnSuccessListener { result ->
                val compras = result.documents.mapNotNull { it.toObject(Compra::class.java) }
                onSuccess(compras)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun guardarCompra(
        compra: Compra,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val idCompra = db.collection("compras").document().id
        val compraConId = compra.copy(id = idCompra)
        db.collection("compras").document(idCompra)
            .set(compraConId)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
