package com.example.mybuffet.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mybuffet.ui.MyBuffetViewModel
import com.example.mybuffet.ui.dashboard.DashboardScreen
import com.example.mybuffet.ui.evento.EventoDetalleScreen
import com.example.mybuffet.ui.login.LoginScreen
import com.example.mybuffet.ui.splash.SplashScreen

@Composable
fun MyBuffetNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: MyBuffetViewModel = viewModel()
) {
    NavHost(navController = navController, startDestination = Rutas.Splash.ruta) {

        composable(Rutas.Splash.ruta) {
            SplashScreen(
                onTimeout = {
                    if (viewModel.usuarioLogueado) {
                        navController.navigate(Rutas.Dashboard.ruta) {
                            popUpTo(Rutas.Splash.ruta) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Rutas.Login.ruta) {
                            popUpTo(Rutas.Splash.ruta) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Rutas.Login.ruta) {
            LoginScreen(
                onLoginSuccess = { user ->
                    viewModel.login(user)
                    navController.navigate(Rutas.Dashboard.ruta) {
                        popUpTo(Rutas.Login.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.Dashboard.ruta) {
            DashboardScreen(
                eventos = viewModel.eventos,
                username = viewModel.username,
                onEventoClick = { evento ->
                    Log.d("MyBuffet", "Evento clickeado: ${evento.id} - ${evento.nombre}")
                    viewModel.seleccionarEvento(evento)
                    navController.navigate(Rutas.DetalleEvento.ruta)
                },
                onAgregarClick = { /* lógica agregar evento */ },
                onCerrarClick = {
                    viewModel.cerrarSesion()
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(Rutas.Dashboard.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.DetalleEvento.ruta) {
            val evento = viewModel.eventoSeleccionado
            if (evento == null) {
                Log.e("MyBuffet-debug", "eventoSeleccionado es null en DetalleEvento. Volviendo a Dashboard.")
                navController.popBackStack()
            } else {
                EventoDetalleScreen(
                    evento = evento,
                    onVolver = { navController.popBackStack() },
                    onEventoActualizado = {
                        // viewModel.setEventos(...) si querés actualizar
                    },
                    onEventoBorrado = {
                        navController.popBackStack()
                        // viewModel.setEventos(...) si querés actualizar
                    },
                    onProductos = { /* Ir a pantalla de productos */ },
                    onVerRecaudacion = { /* Pantalla futura */ },
                    onIrAlBuffet = { /* Pantalla futura */ }
                )
            }
        }
    }
}
