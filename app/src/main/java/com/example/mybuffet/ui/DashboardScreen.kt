package com.example.mybuffet.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
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
    username: String,
    onEventoClick: (Evento) -> Unit,
    onAgregarClick: () -> Unit,
    onCerrarClick: () -> Unit
) {
    var filtroTexto by remember { mutableStateOf("") }
    var filtroEstado by remember { mutableStateOf(1) } // Por defecto activos

    // Colores botones filtro
    val colorAgregar = MaterialTheme.colorScheme.primary
    val colorActivo = Color(0xFF4CAF50) // Verde
    val colorCerrado = Color(0xFFFF9800) // Naranja
    val colorBorrado = MaterialTheme.colorScheme.error // Rojo igual que botón cerrar

    // Filtrar eventos según búsqueda y estado
    val eventosFiltrados = eventos
        .filter { evento ->
            val nombreCoincide = evento.nombre.contains(filtroTexto, ignoreCase = true)
            val estadoCoincide = when (filtroEstado) {
                -1 -> true // Todos
                else -> evento.estado == filtroEstado
            }
            nombreCoincide && estadoCoincide
        }
        .sortedBy { it.nombre.lowercase() } // Orden alfabético ignorando mayúsculas

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Hola, $username!",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
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

        // Filtros con botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Todos
            FilterButton(
                text = "Todos",
                selected = filtroEstado == -1,
                colorSelected = colorAgregar,
                onClick = { filtroEstado = -1 }
            )
            // Activos
            FilterButton(
                text = "Activos",
                selected = filtroEstado == 1,
                colorSelected = colorActivo,
                onClick = { filtroEstado = 1 }
            )
            // Cerrados
            FilterButton(
                text = "Cerrados",
                selected = filtroEstado == 0,
                colorSelected = colorCerrado,
                onClick = { filtroEstado = 0 }
            )
            // Borrados
            FilterButton(
                text = "Borrados",
                selected = filtroEstado == 8,
                colorSelected = colorBorrado,
                onClick = { filtroEstado = 8 }
            )
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
                    val colorEvento = when (evento.estado) {
                        1 -> colorActivo
                        0 -> colorCerrado
                        8 -> colorBorrado
                        else -> Color.Gray
                    }
                    Button(
                        onClick = { onEventoClick(evento) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorEvento)
                    ) {
                        Text(
                            text = evento.nombre,
                            modifier = Modifier.padding(vertical = 4.dp),
                            fontSize = 18.sp,
                            color = Color.White
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
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Agregar")
            }

            Button(
                onClick = onCerrarClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = colorBorrado),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar App", color = MaterialTheme.colorScheme.onError)
            }
        }
    }
}

@Composable
fun FilterButton(
    text: String,
    selected: Boolean,
    colorSelected: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) colorSelected else Color.Transparent,
            contentColor = if (selected) Color.White else Color.Gray
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        border = if (selected) null else ButtonDefaults.outlinedButtonBorder
    ) {
        Text(text = text, fontSize = 14.sp)
    }
}

// Preview con datos ficticios
@Composable
fun DashboardScreenPreview() {
    val eventosFicticios = listOf(
        Evento(id = "1", nombre = "Cena Navidad", estado = 1),
        Evento(id = "2", nombre = "Fiesta Año Nuevo", estado = 0),
        Evento(id = "3", nombre = "Reunión Equipo", estado = 8),
        Evento(id = "4", nombre = "Cumpleaños Juan", estado = 1),
        Evento(id = "5", nombre = "Evento Borrado", estado = 8),
        Evento(id = "6", nombre = "Acto Escolar", estado = 0)
    )
    DashboardScreen(
        eventos = eventosFicticios,
        username = "Juan",
        onEventoClick = {},
        onAgregarClick = {},
        onCerrarClick = {}
    )
}
