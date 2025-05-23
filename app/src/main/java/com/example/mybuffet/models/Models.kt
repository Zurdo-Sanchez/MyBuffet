package com.example.mybuffet.models

data class Evento(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String="",
    val ctdPersonas: Int = 0,
    val estado: Int = 1 // 1 = Abierto, 0 = Cerrado
)


data class Producto(
    val id: String = "",
    val nombre: String = "",
    val precio: Double = 0.0,
    val cantidad: Int = 0,
    val eventoId: String = ""
)
