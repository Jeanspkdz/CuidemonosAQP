package com.jean.cuidemonosaqp.navigation

import androidx.compose.material3.Text
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
import com.jean.cuidemonosaqp.modules.map.ui.MapScreenHost
import com.jean.cuidemonosaqp.modules.map.ui.MapViewModel
import com.jean.cuidemonosaqp.modules.profile.ui.ProfileScreen
import com.jean.cuidemonosaqp.modules.profile.ui.ProfileScreenHost
import com.jean.cuidemonosaqp.modules.profile.ui.ProfileViewModel
import com.jean.cuidemonosaqp.modules.safeZone.ui.safeZoneDetail.SafeZoneDetailScreenHost
import com.jean.cuidemonosaqp.modules.safeZone.ui.safeZoneDetail.SafeZoneDetailViewModel

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    // Definir el NavHost y sus rutas
    NavHost(
        navController = navController,
        startDestination = Routes.Auth.Login,  // Rutas definidas
        modifier = modifier
    ) {

        // Pantalla de Login
        composable<Routes.Auth.Login>() {
            val viewModel = hiltViewModel<LoginViewModel>()

            LoginScreenHost(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(route = Routes.Map) {
                        popUpTo(Routes.Auth.Login) { inclusive = true }

                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.Auth.Register)
                }
            )
        }
        // Pantalla de Registro
        composable<Routes.Auth.Register>() {
            val viewModel = hiltViewModel<RegisterViewModel>()
            RegisterScreenHost(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navController.navigate(Routes.Map) {
                        popUpTo(Routes.Auth.Register) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Routes.Auth.Login) {
                        popUpTo(Routes.Auth.Register) { inclusive = true }
                        launchSingleTop = true
                    }

                }
            )
        }

        composable<Routes.Profile>() {
            val viewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreenHost(viewModel = viewModel)
        }
        composable<Routes.Map>() {
            val viewModel = hiltViewModel<MapViewModel>()
            MapScreenHost(
                viewModel = viewModel,
                onNavigateToCreateSafeZone = {
                    navController.navigate(Routes.SafeZone.Create)
                },
                onNavigateToSafeZoneDetail = {safeZoneId ->
                    navController.navigate(Routes.SafeZone.Detail(id = safeZoneId))
                }
            )
        }

        composable<Routes.SafeZone.Create>() {
//            CreateSafeZoneScreen() // Aquí se navega a la pantalla de creación de zona segura
            Text("Crear SafeZone")
        }
        composable<Routes.SafeZone.Detail> {
            val viewModel  = hiltViewModel<SafeZoneDetailViewModel>()
            SafeZoneDetailScreenHost(viewModel = viewModel , onNavigateToUserProfile = {userId ->
                navController.navigate(Routes.Profile(id = userId))
            })
        }
    }
}
