package com.example.mybuffet.ui.productos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.mybuffet.models.Producto

@Composable
fun AgregarProductoScreen(
    onAgregarProducto: (Producto) -> Unit,
    onVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    Column {
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        TextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") })

        Button(onClick = {
            val nuevoProducto = Producto(nombre = nombre, precio = precio.toDoubleOrNull() ?: 0.0)
            onAgregarProducto(nuevoProducto)
        }) {
            Text("Agregar")
        }
        Button(onClick = onVolver) {
            Text("Volver")
        }
    }
}
