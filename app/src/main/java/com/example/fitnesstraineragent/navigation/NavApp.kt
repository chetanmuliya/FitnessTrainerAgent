package com.example.fitnesstraineragent.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.fitnesstraineragent.presentation.home.HomeScreen
import com.example.fitnesstraineragent.presentation.trainer.DietPlanScreen
import com.example.fitnesstraineragent.presentation.trainer.TrainerScreen
import com.example.fitnesstraineragent.presentation.trainer.TrainerViewModel

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
                    navController.navigate("trainer_graph")
                },{

                }
            )
        }

        navigation(
            startDestination = "trainer",
            route = "trainer_graph"
        ) {

            composable("trainer") { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("trainer_graph")
                }

                val viewModel: TrainerViewModel = viewModel(parentEntry)

                TrainerScreen(
                    viewModel = viewModel,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onDietPlanReady = {
                        navController.navigate("diet_plan")
                    }
                )
            }

            composable("diet_plan") { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("trainer_graph")
                }

                val viewModel: TrainerViewModel = viewModel(parentEntry)

                DietPlanScreen(
                    viewModel = viewModel
                )
            }
        }
    }

}