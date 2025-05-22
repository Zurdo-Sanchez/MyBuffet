package com.example.mybuffet.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mybuffet.models.Evento
import com.example.mybuffet.repository.RepositorioEventos

@Composable
fun CrearEventoScreen(
    onEventoCreado: (Evento) -> Unit,
    onError: (String) -> Unit
) {
    var nombreEvento by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Crear nuevo evento", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = nombreEvento,
            onValueChange = { nombreEvento = it },
            label = { Text("Nombre del evento") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (nombreEvento.isBlank()) {
                    onError("El nombre no puede estar vacío")
                    return@Button
                }
                cargando = true

                RepositorioEventos.crearEvento(
                    nombre = nombreEvento,
                    onSuccess = {
                        cargando = false
                        onEventoCreado(it)
                        nombreEvento = ""
                    },
                    onFailure = {
                        cargando = false
                        onError("Error al crear evento: ${it.message}")
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Crear evento")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCrearEventoScreen() {
    CrearEventoScreen(
        onEventoCreado = {},
        onError = {}
    )
}
