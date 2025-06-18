package com.jean.cuidemonosaqp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jean.cuidemonosaqp.modules.auth.ui.login.LoginScreenHost
import com.jean.cuidemonosaqp.modules.auth.ui.login.LoginViewModel
import com.jean.cuidemonosaqp.modules.auth.ui.register.RegisterScreenHost
import com.jean.cuidemonosaqp.modules.auth.ui.register.RegisterViewModel
import com.jean.cuidemonosaqp.modules.map.ui.MapScreen
import com.jean.cuidemonosaqp.modules.profile.ui.ProfileScreen

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    // Definir el NavHost y sus rutas
    NavHost(
        navController = navController,
        startDestination = Routes.Auth.Login.route,  // Rutas definidas
        modifier = modifier
    ) {

        // Pantalla de Login
        composable(Routes.Auth.Login.route) {
            val viewModel = hiltViewModel<LoginViewModel>()

            LoginScreenHost(
                viewModel = viewModel,
                onLoginSuccess = {

//                    navController.navigate(Routes.Map.route) {
                    navController.navigate(Routes.Profile.route) {
                        popUpTo(Routes.Auth.Login.route) { inclusive = true }

                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.Auth.Register.route)
                }
            )
        }
        // Pantalla de Registro
        composable(Routes.Auth.Register.route) {
            val viewModel = hiltViewModel<RegisterViewModel>()
            RegisterScreenHost(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navController.navigate(Routes.Map.route) {
                        popUpTo(Routes.Auth.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    //
                    navController.navigate(Routes.Auth.Register.route)
                }
            )
        }

        composable(Routes.Profile.route) {
            ProfileScreen()
        }
        // Pantalla de Perfil (ProfileScreen)
        composable(Routes.Map.route) {
            MapScreen()
        }

        // Pantalla de Crear Zona Segura (CreateSafeZoneScreen)
        composable(Routes.CreateSafeZone.route) {
//            CreateSafeZoneScreen() // Aquí se navega a la pantalla de creación de zona segura
        }
    }
}
