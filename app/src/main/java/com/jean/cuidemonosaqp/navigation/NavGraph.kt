package com.jean.cuidemonosaqp.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jean.cuidemonosaqp.modules.auth.ui.login.LoginScreenHost
import com.jean.cuidemonosaqp.modules.auth.ui.login.LoginViewModel

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.Auth.Login,
        modifier = modifier
    ) {
        composable<Routes.Auth.Login> {
            val viewModel = hiltViewModel<LoginViewModel>()

            LoginScreenHost(
                viewModel = viewModel
            )
        }

        composable<Routes.Profile> {
            Text("PRofile Sscreen")
        }
    }
}