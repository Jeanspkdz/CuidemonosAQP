package com.jean.cuidemonosaqp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jean.cuidemonosaqp.modules.auth.ui.login.LoginScreenHost
import com.jean.cuidemonosaqp.modules.auth.ui.login.LoginViewModel
import com.jean.cuidemonosaqp.modules.auth.ui.register.RegisterScreenHost
import com.jean.cuidemonosaqp.modules.auth.ui.register.RegisterViewModel
import com.jean.cuidemonosaqp.modules.map.ui.MapScreenHost
import com.jean.cuidemonosaqp.modules.map.ui.MapViewModel
import com.jean.cuidemonosaqp.modules.profile.ui.ProfileScreenHost
import com.jean.cuidemonosaqp.modules.profile.ui.ProfileViewModel
import com.jean.cuidemonosaqp.modules.safeZone.ui.createPoint.CreateSafeZoneScreen
import com.jean.cuidemonosaqp.modules.safeZone.ui.list.SafeZoneListScreenHost
import com.jean.cuidemonosaqp.modules.safeZone.ui.list.SafeZoneListViewModel
import com.jean.cuidemonosaqp.modules.safeZone.ui.safeZoneDetail.SafeZoneDetailScreenHost
import com.jean.cuidemonosaqp.modules.safeZone.ui.safeZoneDetail.SafeZoneDetailViewModel

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Routes.Auth.Login,
        modifier = modifier
    ) {

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
            CreateSafeZoneScreen()
        }
        composable<Routes.SafeZone.List>() {
            val viewModel = hiltViewModel<SafeZoneListViewModel>()
            SafeZoneListScreenHost(
                viewModel = viewModel,
                onNavigateToSafeZoneDetail = { safeZoneId ->
                    navController.navigate(Routes.SafeZone.Detail(id = safeZoneId))
                }
            )
        }
        composable<Routes.SafeZone.Detail> {
            val viewModel  = hiltViewModel<SafeZoneDetailViewModel>()
            SafeZoneDetailScreenHost(viewModel = viewModel , onNavigateToUserProfile = {userId ->
                navController.navigate(Routes.Profile(id = userId))
            })
        }
    }

}
