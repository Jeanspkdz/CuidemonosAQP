package com.jean.cuidemonosaqp.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.jean.cuidemonosaqp.modules.auth.ui.login.LoginScreenHost
import com.jean.cuidemonosaqp.modules.auth.ui.login.LoginViewModel
import com.jean.cuidemonosaqp.modules.auth.ui.register.RegisterScreenHost
import com.jean.cuidemonosaqp.modules.auth.ui.register.RegisterViewModel
import com.jean.cuidemonosaqp.modules.map.ui.MapScreenHost
import com.jean.cuidemonosaqp.modules.map.ui.MapViewModel
import com.jean.cuidemonosaqp.modules.notification.ui.NotificationsScreen
import com.jean.cuidemonosaqp.modules.notification.ui.NotificationsScreenHost
import com.jean.cuidemonosaqp.modules.notification.ui.NotificationsViewModel
import com.jean.cuidemonosaqp.modules.profile.ui.ProfileScreenHost
import com.jean.cuidemonosaqp.modules.profile.ui.ProfileViewModel
import com.jean.cuidemonosaqp.modules.safeZone.ui.createPoint.CreateSafeZoneScreen
import com.jean.cuidemonosaqp.modules.safeZone.ui.safeZoneDetail.SafeZoneDetailScreenHost
import com.jean.cuidemonosaqp.modules.safeZone.ui.safeZoneDetail.SafeZoneDetailViewModel
import com.jean.cuidemonosaqp.shared.components.Splash
import com.jean.cuidemonosaqp.shared.viewmodel.SharedViewModel
import kotlinx.coroutines.delay

@Composable
fun NavGraph(
    sharedViewModel: SharedViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val isInitialLoading by sharedViewModel.isInitialLoading.collectAsStateWithLifecycle()
    val isAuthenticated by sharedViewModel.isAuthenticated.collectAsStateWithLifecycle()

    if (isInitialLoading) {
        Splash()
        return
    }

    val startDestination = if (isAuthenticated) Routes.Map else Routes.Auth.Login

    NavHost(
        navController = navController,
        startDestination = startDestination,
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
                    navController.navigate(Routes.Auth.Login) {
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
            ProfileScreenHost(
                sharedViewModel = sharedViewModel,
                viewModel = viewModel
            )
        }
        composable<Routes.Map>() {
            val viewModel = hiltViewModel<MapViewModel>()
            MapScreenHost(
                sharedViewModel = sharedViewModel,
                viewModel = viewModel,
                onNavigateToCreateSafeZone = {
                    navController.navigate(Routes.SafeZone.Create)
                },
                onNavigateToSafeZoneDetail = { safeZoneId ->
                    navController.navigate(Routes.SafeZone.Detail(id = safeZoneId))
                },
                onNavigateToNotifications = {
                    navController.navigate(Routes.Notifications)
                },
                onNavigateToProfile = {
                    navController.navigate(Routes.Profile(id = sharedViewModel.userId.value!!))
                },
                onLogout = {
                    sharedViewModel.logout()
                }
            )
        }
        composable<Routes.SafeZone.Create>() {
            CreateSafeZoneScreen()
        }
        composable<Routes.SafeZone.Detail> {
            val viewModel = hiltViewModel<SafeZoneDetailViewModel>()
            SafeZoneDetailScreenHost(viewModel = viewModel, onNavigateToUserProfile = { userId ->
                navController.navigate(Routes.Profile(id = userId))
            })
        }

        composable<Routes.Notifications> {
            val viewModel = hiltViewModel<NotificationsViewModel>()
            NotificationsScreenHost(
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.popBackStack()
                }
            )
        }

    }
}
