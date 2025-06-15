package com.jean.cuidemonosaqp.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jean.cuidemonosaqp.modules.auth.ui.login.LoginScreenHost
import com.jean.cuidemonosaqp.modules.auth.ui.login.LoginViewModel
import com.jean.cuidemonosaqp.modules.auth.ui.register.RegisterScreenHost
import com.jean.cuidemonosaqp.modules.auth.ui.register.RegisterViewModel
import com.jean.cuidemonosaqp.modules.map.ui.MapScreen
import com.jean.cuidemonosaqp.modules.points.createPoint.ui.CreatePointScreen

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.Points.Create,
        modifier = modifier
    ) {
        composable<Routes.Auth.Login> {
            val viewModel = hiltViewModel<LoginViewModel>()

            LoginScreenHost(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable<Routes.Auth.Register> {
            val viewModel = hiltViewModel<RegisterViewModel>()

            RegisterScreenHost(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable<Routes.Profile> {
            Text("Profile Sscreen")
        }

        composable<Routes.Home> {
            MapScreen()
        }

        composable<Routes.Points.Create> {
            CreatePointScreen()
        }
    }
}