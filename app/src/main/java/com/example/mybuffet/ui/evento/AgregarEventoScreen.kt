package com.example.mybuffet.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybuffet.models.Evento
import com.example.mybuffet.models.EventoViewModel

@Composable
fun AgregarEventoScreen(
    eventoViewModel: EventoViewModel = viewModel(),
    onEventoCreado: (Evento) -> Unit,
    onCancelar: () -> Unit
) {
    var nombreEvento by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Agregar Evento", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombreEvento,
            onValueChange = {
                nombreEvento = it
                if (error != null) error = null
            },
            label = { Text("Nombre del evento") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (nombreEvento.isBlank()) {
                        error = "El nombre no puede estar vacío"
                        return@Button
                    }
                    isLoading = true
                    eventoViewModel.crearEvento(
                        nombre = nombreEvento.trim(),
                        onSuccess = { evento ->
                            isLoading = false
                            onEventoCreado(evento)
                        },
                        onFailure = {
                            isLoading = false
                            error = "Error al crear el evento"
                        }
                    )
                },
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Crear")
                }
            }

            OutlinedButton(
                onClick = onCancelar,
                enabled = !isLoading
            ) {
                Text("Cancelar")
            }
        }
    }
}

