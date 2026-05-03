package com.example.roboapp.ui.theme.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.roboapp.ui.screens.login.LoginScreen
import com.example.roboapp.ui.screens.login.RegisterScreen
import com.example.roboapp.data.model.RegisterUserType
import com.example.roboapp.ui.screens.login.AuthViewModel
import com.example.roboapp.ui.screens.login.ChooseRoleScreen
import com.example.roboapp.ui.screens.login.child.ChildModeScreen
import com.example.roboapp.ui.screens.login.therapist.TherapistScreen

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(
                onLoginSuccess = { role, uid ->
                    when (role) {
                        RegisterUserType.CHILD -> navController.navigate("child_home/$uid") {
                            popUpTo("login") { inclusive = true }
                        }
                        RegisterUserType.THERAPIST -> navController.navigate("therapist_home/$uid") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                onRegister = {
                    navController.navigate("register")
                },
                onFirstTimeGoogle = {
                    navController.navigate("choose_role")
                },
                viewModel = authViewModel
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { preselectedRole ->
                    // Navegar a choose_role con el rol preseleccionado
                    navController.navigate("choose_role/${preselectedRole.name}")
                },
                onBack = { navController.popBackStack() },
                viewModel = authViewModel
            )
        }

        composable(
            "choose_role/{role}",
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val roleName = backStackEntry.arguments?.getString("role") ?: "CHILD"
            val preselectedRole = try {
                RegisterUserType.valueOf(roleName.uppercase())
            } catch (e: IllegalArgumentException) {
                RegisterUserType.CHILD
            }
            ChooseRoleScreen(
                preselectedRole = preselectedRole,
                onRoleSelected = { role, uid ->
                    when (role) {
                        RegisterUserType.CHILD -> navController.navigate("child_home/$uid") {
                            popUpTo("login") { inclusive = true }
                        }
                        RegisterUserType.THERAPIST -> navController.navigate("therapist_home/$uid") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                onBack = { navController.popBackStack() },
                viewModel = authViewModel
            )
        }

        // También necesitamos una ruta para choose_role sin parámetro (desde login con first time)
        composable("choose_role") {
            ChooseRoleScreen(
                preselectedRole = null,
                onRoleSelected = { role, uid ->
                    when (role) {
                        RegisterUserType.CHILD -> navController.navigate("child_home/$uid") {
                            popUpTo("login") { inclusive = true }
                        }
                        RegisterUserType.THERAPIST -> navController.navigate("therapist_home/$uid") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                onBack = { navController.popBackStack() },
                viewModel = authViewModel
            )
        }

        composable(
            "child_home/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ChildModeScreen(userId = userId)
        }

        composable(
            "therapist_home/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            TherapistScreen(userId = userId)
        }
    }
}