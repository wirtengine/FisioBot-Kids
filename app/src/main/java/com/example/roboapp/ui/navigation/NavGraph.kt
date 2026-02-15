package com.example.roboapp.ui.theme.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.roboapp.ui.screens.login.ChooseRoleScreen
import com.example.roboapp.ui.screens.login.LoginScreen
import com.example.roboapp.ui.screens.login.RegisterScreen
import com.example.roboapp.ui.screens.login.RegisterUserType
import com.example.roboapp.ui.screens.login.AuthViewModel
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
                onForgotPassword = { /* manejar recuperación */ },
                onFirstTimeGoogle = {
                    Log.d("GoogleFlow", "Navegando a choose_role")
                    navController.navigate("choose_role")
                },
                viewModel = authViewModel
            )
        }

        composable("register") {
            RegisterScreen(
                onRegister = { userType ->
                    when (userType) {
                        RegisterUserType.CHILD -> navController.navigate("child_home") {
                            popUpTo("login") { inclusive = true }
                        }
                        RegisterUserType.THERAPIST -> navController.navigate("therapist_home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                onBack = {
                    navController.popBackStack()
                },
                viewModel = authViewModel
            )
        }

        composable("choose_role") {
            ChooseRoleScreen(
                onRoleSelected = { role, username, age, password ->
                    authViewModel.saveGoogleUserWithPassword(
                        role = role,
                        username = username,
                        age = age,
                        password = password,
                        onSuccess = {
                            when (role) {
                                RegisterUserType.CHILD -> navController.navigate("child_home") {
                                    popUpTo("login") { inclusive = true }
                                }
                                RegisterUserType.THERAPIST -> navController.navigate("therapist_home") {
                                    popUpTo("login") { inclusive = true }
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

        composable("child_home") {
            ChildModeScreen()
        }

        composable("therapist_home") {
            TherapistScreen()
        }
    }
}