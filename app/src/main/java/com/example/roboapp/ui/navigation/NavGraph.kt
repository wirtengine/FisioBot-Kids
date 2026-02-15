package com.example.roboapp.ui.theme.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.roboapp.ui.screens.login.LoginScreen
import com.example.roboapp.ui.screens.login.RegisterScreen
import com.example.roboapp.data.model.RegisterUserType   // ✅ Importación correcta
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

        // ----------------------------
        // LOGIN
        // ----------------------------
        composable("login") {

            LoginScreen(
                onLoginChild = {
                    navController.navigate("child_home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onLoginTherapist = {
                    navController.navigate("therapist_home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegister = {
                    navController.navigate("register")
                },
                onFirstTimeGoogle = {
                    Log.d("GoogleFlow", "Usuario nuevo → choose_role")
                    navController.navigate("choose_role")
                },
                viewModel = authViewModel
            )
        }

        // ----------------------------
        // REGISTER
        // ----------------------------
        composable("register") {

            RegisterScreen(
                onRegisterSuccess = { userType ->
                    when (userType) {
                        RegisterUserType.CHILD -> {
                            navController.navigate("child_home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                        RegisterUserType.THERAPIST -> {
                            navController.navigate("therapist_home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                },
                onBack = {
                    navController.popBackStack()
                },
                viewModel = authViewModel
            )
        }

        // ----------------------------
        // CHOOSE ROLE (Primera vez Google)
        // ----------------------------
        composable("choose_role") {

            ChooseRoleScreen(
                onRoleSelected = { role, username, age ->
                    authViewModel.saveGoogleUser(
                        role = role,
                        username = username,
                        age = age,
                        onSuccess = {
                            when (role) {
                                RegisterUserType.CHILD -> {
                                    navController.navigate("child_home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                                RegisterUserType.THERAPIST -> {
                                    navController.navigate("therapist_home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            }
                        }
                    )
                },
                onBack = {
                    navController.popBackStack()
                },
                viewModel = authViewModel
            )
        }

        // ----------------------------
        // CHILD HOME
        // ----------------------------
        composable("child_home") {
            ChildModeScreen()
        }

        // ----------------------------
        // THERAPIST HOME
        // ----------------------------
        composable("therapist_home") {
            TherapistScreen()
        }
    }
}