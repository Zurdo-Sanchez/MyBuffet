package com.example.mybuffet.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mybuffet.models.Evento
import com.example.mybuffet.models.Producto
import com.example.mybuffet.repository.RepositorioProductos

@Composable
fun AgregarProductoScreen(
    evento: Evento,
    onProductoAgregado: () -> Unit,
    onVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Agregar producto para evento: ${evento.nombre}", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = precio,
            onValueChange = { input ->
                // Filtramos para que solo se ingresen números y punto decimal
                val filtered = input.filter { it.isDigit() || it == '.' }
                if (filtered == input) {
                    precio = input
                }
            },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (error != null) {
            Text(error ?: "", color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = onVolver) {
                Text("Volver")
            }

            Button(onClick = {
                val precioDouble = precio.toDoubleOrNull()
                if (nombre.isBlank()) {
                    error = "El nombre no puede estar vacío"
                    return@Button
                }
                if (precioDouble == null || precioDouble <= 0.0) {
                    error = "Precio inválido"
                    return@Button
                }

                val producto = Producto(
                    id = "",  // Firebase genera id
                    eventoId = evento.id,
                    nombre = nombre,
                    precioUnitario = precioDouble
                )

                RepositorioProductos.agregarProducto(producto, {
                    error = null
                    nombre = ""
                    precio = ""
                    onProductoAgregado()
                }, { e ->
                    error = e.message
                })
            }) {
                Text("Agregar Producto")
            }
        }
    }
}
