package com.example.mybuffet.ui.navigation

// Clase sellada para rutas
sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash")
    object Login : Rutas("login")
    object Dashboard : Rutas("dashboard")
    object CrearEvento : Rutas("crear_evento")
    object DetalleEvento : Rutas("detalle_evento")
    object Productos : Rutas("productos")
    object AgregarProducto : Rutas("agregar_producto")
}

// Función para generar la ruta con id usando el objeto DetalleEvento
fun detalleEventoConId(id: String) = "${Rutas.DetalleEvento.ruta}/$id"
