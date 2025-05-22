package com.example.mybuffet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import com.example.mybuffet.models.Evento

@Composable
fun DashboardScreen(
    eventos: List<Evento>,
    onAgregarClick: () -> Unit,
    onCerrarClick: () -> Unit,
    onEventoClick: (Evento) -> Unit
) {
    var filtroTexto by remember { mutableStateOf("") }
    var filtrarActivos by remember { mutableStateOf(true) }
    var filtrarInactivos by remember { mutableStateOf(false) }

    // Filtrar eventos según búsqueda y estado
    val eventosFiltrados = eventos.filter { evento ->
        val nombreCoincide = evento.nombre.contains(filtroTexto, ignoreCase = true)
        val estadoCoincide = (filtrarActivos && evento.estado == 1) || (filtrarInactivos && evento.estado == 0)
        nombreCoincide && estadoCoincide
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título artístico centrado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .shadow(8.dp, RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF6A11CB),
                            Color(0xFF2575FC)
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = "Mis Buffets",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Barra de búsqueda
        OutlinedTextField(
            value = filtroTexto,
            onValueChange = { filtroTexto = it },
            label = { Text("Buscar evento") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { /* Ocultar teclado si querés */ })
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Filtros con checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = filtrarActivos,
                onCheckedChange = { filtrarActivos = it }
            )
            Text("Activos", modifier = Modifier.padding(end = 16.dp))

            Checkbox(
                checked = filtrarInactivos,
                onCheckedChange = { filtrarInactivos = it }
            )
            Text("Inactivos")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Listado de eventos filtrados como botones, scrollable si son muchos
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (eventosFiltrados.isEmpty()) {
                Text(
                    "No hay eventos que coincidan",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                eventosFiltrados.forEach { evento ->
                    Button(
                        onClick = { onEventoClick(evento) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = evento.nombre,
                            modifier = Modifier.padding(8.dp),
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botones de agregar y cerrar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onAgregarClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Agregar")
            }

            Button(
                onClick = onCerrarClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cerrar App", color = MaterialTheme.colorScheme.onError)
            }
        }
    }
}
