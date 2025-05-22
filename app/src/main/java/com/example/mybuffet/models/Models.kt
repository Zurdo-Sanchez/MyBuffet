package com.example.mybuffet.models

data class Evento(
    val id: String = "",
    val nombre: String = "",
    val estado: Int = 1 // 1 = Abierto, 0 = Cerrado
)


data class Producto(
    val id: String,
    val eventoId: String,
    val nombre: String,
    val precioUnitario: Double
)

data class ItemCompra(
    val productoId: String = "",
    val cantidad: Int = 0,
    val precioUnitario: Double = 0.0
)

data class Compra(
    val id: String = "",
    val items: List<ItemCompra> = emptyList(),
    val fecha: Long = System.currentTimeMillis(),
    val total: Double = 0.0
)
