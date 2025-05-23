package com.example.mybuffet

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.mybuffet.models.Evento
import com.example.mybuffet.ui.dashboard.DashboardScreen
import com.example.mybuffet.ui.evento.CrearEventoScreen
import com.example.mybuffet.ui.evento.EventoDetalleScreen
import com.example.mybuffet.ui.login.LoginScreen
import com.example.mybuffet.ui.navigation.Rutas
import com.example.mybuffet.ui.navigation.detalleEventoConId
import com.example.mybuffet.ui.splash.SplashScreen
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                var eventos by remember { mutableStateOf<List<Evento>>(emptyList()) }
                var username by remember { mutableStateOf("") }

                fun cargarEventos() {
                    db.collection("eventos")
                        .get()
                        .addOnSuccessListener { result ->
                            eventos = result.map { doc ->
                                Evento(
                                    id = doc.id,
                                    nombre = doc.getString("nombre") ?: "",
                                    descripcion = doc.getString("descripcion") ?: "",
                                    estado = (doc.getLong("estado") ?: 1).toInt()
                                )
                            }
                        }
                }

                val currentRoute by navController.currentBackStackEntryAsState()
                LaunchedEffect(currentRoute?.destination?.route) {
                    if (currentRoute?.destination?.route == Rutas.Dashboard.ruta) {
                        cargarEventos()
                    }
                }

                NavHost(navController = navController, startDestination = Rutas.Splash.ruta) {
                    composable(Rutas.Splash.ruta) {
                        SplashScreen {
                            navController.navigate(Rutas.Login.ruta) {
                                popUpTo(Rutas.Splash.ruta) { inclusive = true }
                            }
                        }
                    }
                    composable(Rutas.Login.ruta) {
                        LoginScreen { user ->
                            username = user
                            navController.navigate(Rutas.Dashboard.ruta) {
                                popUpTo(Rutas.Login.ruta) { inclusive = true }
                            }
                        }
                    }
                    composable(Rutas.Dashboard.ruta) {
                        DashboardScreen(
                            eventos = eventos,
                            onAgregarClick = {
                                navController.navigate(Rutas.CrearEvento.ruta)
                            },
                            onCerrarClick = {
                                (context as? Activity)?.finish()
                            },
                            onEventoClick = { evento ->
                                navController.navigate(detalleEventoConId(evento.id))
                            },
                            username = username
                        )
                    }
                    composable(
                        route = "${Rutas.DetalleEvento.ruta}/{eventoId}",
                        arguments = listOf(navArgument("eventoId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val eventoId = backStackEntry.arguments?.getString("eventoId")
                        var evento by remember { mutableStateOf<Evento?>(null) }
                        var cargando by remember { mutableStateOf(true) }

                        LaunchedEffect(eventoId) {
                            if (eventoId != null) {
                                db.collection("eventos").document(eventoId)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        evento = Evento(
                                            id = doc.id,
                                            nombre = doc.getString("nombre") ?: "",
                                            descripcion = doc.getString("descripcion") ?: "",
                                            estado = (doc.getLong("estado") ?: 1).toInt()
                                        )
                                        cargando = false
                                    }
                                    .addOnFailureListener {
                                        cargando = false
                                    }
                            }
                        }

                        if (cargando) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            evento?.let {
                                EventoDetalleScreen(
                                    evento = it,
                                    onVolver = { navController.popBackStack() },
                                    onEventoActualizado = { /* actualizar lista o refrescar */ },
                                    onEventoBorrado = { navController.popBackStack() },
                                    onIrAlBuffet = { /* TODO */ },
                                    onVerRecaudacion = { /* TODO */ },
                                    onProductos = { /* TODO */ }
                                )
                            } ?: run {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Evento no encontrado")
                                }
                            }
                        }
                    }
                    composable(Rutas.CrearEvento.ruta) {
                        CrearEventoScreen(
                            onEventoCreado = {
                                navController.popBackStack()
                            },
                            onCancelar = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
