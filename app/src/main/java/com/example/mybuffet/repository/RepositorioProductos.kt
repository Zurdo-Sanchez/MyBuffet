package com.example.mybuffet.repository

import com.example.mybuffet.models.Producto

object RepositorioProductos {

    private val productos = mutableListOf<Producto>() // Simulación local; reemplazar con Firebase si usás

    // Método para obtener productos por evento (simulación simple)
    fun obtenerProductosPorEvento(eventoId: String): List<Producto> {
        return productos.filter { it.eventoId == eventoId }
    }

    fun agregarProducto(producto: Producto, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        try {
            productos.add(producto)
            onSuccess()
        } catch (e: Exception) {
            onError(e)
        }
    }

    // Otros métodos que necesites para manipular productos
}
