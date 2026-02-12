package com.example.roboapp.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.roboapp.ui.screens.login.LoginScreen
import com.example.roboapp.ui.screens.login.RegisterScreen
import com.example.roboapp.ui.screens.login.child.ChildModeScreen
import com.example.roboapp.ui.screens.login.therapist.TherapistScreen
@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(
                onLoginChild = {
                    navController.navigate("child")
                },
                onLoginTherapist = {
                    navController.navigate("therapist")
                },
                onRegister = {
                    navController.navigate("register")
                },
                onForgotPassword = {
                    // Aquí puedes añadir navegación para recuperar contraseña si lo implementas
                    // navController.navigate("forgot_password")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegister = {
                    // Aquí va la lógica después de registrar exitosamente
                    // Por ejemplo, podrías navegar a login o directamente al modo correspondiente
                    navController.popBackStack()
                    navController.navigate("login")
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("child") {
            ChildModeScreen()
        }

        composable("therapist") {
            TherapistScreen()
        }
    }
}