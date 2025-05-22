package com.example.mybuffet.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mybuffet.models.Evento
import com.example.mybuffet.models.Producto
import com.example.mybuffet.models.CompraViewModel

@Composable
fun CompraScreen(
    evento: Evento,
    viewModel: CompraViewModel,
    onVolver: () -> Unit
) {
    val productos by viewModel.productos.collectAsState(initial = emptyList())
    var cantidadInput: String by remember { mutableStateOf("") }
    var productoSeleccionado: Producto? by remember { mutableStateOf(null) }
    var error: String? by remember { mutableStateOf(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Registrar Compras para evento: ${evento.nombre}", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(productos) { producto ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = producto.nombre)
                    Text(text = "$${producto.precioUnitario}")
                    Button(onClick = { productoSeleccionado = producto }) {
                        Text("Agregar")
                    }
                }
            }
        }

        productoSeleccionado?.let { producto ->
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Agregar cantidad para: ${producto.nombre}", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = cantidadInput,
                onValueChange = { cantidadInput = it },
                label = { Text("Cantidad") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = error != null
            )
            error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = {
                    val cantidadInt = cantidadInput.toIntOrNull()
                    if (cantidadInt == null || cantidadInt <= 0) {
                        error = "Cantidad inválida"
                        return@Button
                    }
                    viewModel.agregarCompra(producto, cantidadInt)
                    cantidadInput = ""
                    error = null
                    productoSeleccionado = null
                }) {
                    Text("Confirmar")
                }
                OutlinedButton(onClick = {
                    productoSeleccionado = null
                    cantidadInput = ""
                    error = null
                }) {
                    Text("Cancelar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}
