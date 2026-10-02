package com.example.fitnesstraineragent.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitnesstraineragent.presentation.home.HomeScreen
import com.example.fitnesstraineragent.presentation.trainer.TrainerScreen

@Composable
fun NavApp() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {

            HomeScreen(
                onCreatePlanClick = {
                    navController.navigate("trainer")
                },

                onWorkoutClick = {
                    // later
                }
            )
        }

        composable("trainer") {

            TrainerScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

    }

}