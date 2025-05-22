package com.example.mybuffet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybuffet.models.Evento
import com.example.mybuffet.ui.theme.AgregarEventoDialog
import com.example.mybuffet.ui.theme.EventoDetalleScreen
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    private val db = FirebaseFirestore.getInstance()

    enum class EstadoFiltro(val valor: Int, val texto: String) {
        TODOS(-1, "Todos"),
        ACTIVOS(1, "Activos"),
        CERRADOS(0, "Cerrados"),
        BORRADOS(8, "Borrados")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var eventos by remember { mutableStateOf<List<Evento>>(emptyList()) }
            var filtroSeleccionado by remember { mutableStateOf(EstadoFiltro.TODOS) }
            var mostrarConfirmacionCerrar by remember { mutableStateOf(false) }
            var mostrarDialogoAgregar by remember { mutableStateOf(false) }
            var eventoSeleccionado by remember { mutableStateOf<Evento?>(null) }

            fun cargarEventos() {
                db.collection("eventos")
                    .get()
                    .addOnSuccessListener { result ->
                        eventos = result.map { doc ->
                            Evento(
                                id = doc.id,
                                nombre = doc.getString("nombre") ?: "",
                                estado = (doc.getLong("estado") ?: 1).toInt()
                            )
                        }
                    }
            }

            fun agregarEvento(nombre: String) {
                val nuevoEvento = hashMapOf(
                    "nombre" to nombre,
                    "estado" to 1  // Por defecto activo al crear
                )
                db.collection("eventos")
                    .add(nuevoEvento)
                    .addOnSuccessListener {
                        cargarEventos()
                    }
            }

            LaunchedEffect(Unit) {
                cargarEventos()
            }

            MaterialTheme {
                if (eventoSeleccionado == null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Título
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
                                color = Color.White,
                                modifier = Modifier.align(Alignment.Center),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Filtros estado
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            EstadoFiltro.values().forEach { filtro ->
                                FilterChip(
                                    selected = filtro == filtroSeleccionado,
                                    onClick = { filtroSeleccionado = filtro },
                                    label = { Text(filtro.texto) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Lista filtrada
                        val eventosFiltrados = when (filtroSeleccionado) {
                            EstadoFiltro.TODOS -> eventos.filter { it.estado != 8 }  // Excluir borrados
                            EstadoFiltro.BORRADOS -> eventos.filter { it.estado == 8 } // Solo borrados
                            else -> eventos.filter { it.estado == filtroSeleccionado.valor }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            if (eventosFiltrados.isEmpty()) {
                                Text(
                                    "No hay eventos para mostrar",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            } else {
                                eventosFiltrados.forEach { evento ->
                                    Button(
                                        onClick = { eventoSeleccionado = evento },
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

                        // Botones
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { mostrarDialogoAgregar = true },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Agregar")
                            }
                            Button(
                                onClick = { mostrarConfirmacionCerrar = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Cerrar App", color = MaterialTheme.colorScheme.onError)
                            }
                        }
                    }

                    if (mostrarDialogoAgregar) {
                        AgregarEventoDialog(
                            onAgregar = { nombre ->
                                agregarEvento(nombre)
                                mostrarDialogoAgregar = false
                            },
                            onCancelar = { mostrarDialogoAgregar = false }
                        )
                    }

                    if (mostrarConfirmacionCerrar) {
                        AlertDialog(
                            onDismissRequest = { mostrarConfirmacionCerrar = false },
                            title = { Text("Confirmar salida") },
                            text = { Text("¿Seguro que querés cerrar la aplicación?") },
                            confirmButton = {
                                TextButton(onClick = { finish() }) {
                                    Text("Sí")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { mostrarConfirmacionCerrar = false }) {
                                    Text("No")
                                }
                            }
                        )
                    }
                } else {
                    EventoDetalleScreen(
                        evento = eventoSeleccionado!!,
                        onVolver = { eventoSeleccionado = null },
                        onEventoActualizado = { cargarEventos() },
                        onEventoBorrado = {
                            eventoSeleccionado = null
                            cargarEventos()
                        },
                        onProductos = { /* Navegar a agregar productos */ },
                        onVerRecaudacion = { /* Navegar a ver recaudación */ },
                        onIrAlBuffet = { /* Navegar a buffet */ },
                    )
                }
            }
        }
    }
}
