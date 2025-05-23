package com.example.mybuffet.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CompraViewModel : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        // Aquí deberías cargar los productos desde tu fuente de datos
        _productos.value = listOf(
            Producto(id = "1", nombre = "Gaseosa", precio = 50.0, eventoId = "Bu"),
            Producto(id = "2", nombre = "Papas", precio = 30.0, eventoId = "Bu"),
            Producto(id = "3", nombre = "Agua", precio = 20.0, eventoId = "Bu"),
        )
    }

    fun agregarCompra(producto: Producto, cantidad: Int) {
        // Lógica para registrar compra (guardar en BD o lista interna)
        // Por ahora solo print para testear
        println("Comprando ${cantidad} x ${producto.nombre}")
    }
}