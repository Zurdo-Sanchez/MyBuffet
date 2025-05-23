package com.example.mybuffet.ui.evento

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mybuffet.models.Evento
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearEventoScreen(
    onEventoCreado: () -> Unit,
    onCancelar: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var ctdPersonas by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nuevo Evento") }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del evento") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = ctdPersonas,
                    onValueChange = { ctdPersonas = it.filter { c -> c.isDigit() } },
                    label = { Text("Cantidad de personas") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                if (cargando) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = onCancelar,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = {
                                if (nombre.isNotBlank()) {
                                    cargando = true
                                    val nuevoEvento = hashMapOf(
                                        "nombre" to nombre,
                                        "descripcion" to descripcion,
                                        "ctdPersonas" to (ctdPersonas.toIntOrNull() ?: 0),
                                        "estado" to 1
                                    )
                                    db.collection("eventos")
                                        .add(nuevoEvento)
                                        .addOnSuccessListener {
                                            cargando = false
                                            onEventoCreado()
                                        }
                                        .addOnFailureListener {
                                            cargando = false
                                            // Mostrar error si querés
                                        }
                                }
                            }
                        ) {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    )
}
