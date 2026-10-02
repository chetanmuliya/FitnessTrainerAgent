package com.example.fitnesstraineragent.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fitnesstraineragent.R

@Composable
fun QuickActions() {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            QuickAction(
                icon = painterResource(R.drawable.ic_fitness_centre),
                title = "Workout"
            )
        }

        item {
            QuickAction(
                icon = painterResource(R.drawable.ic_progress),
                title = "Progress"
            )
        }

        item {
            QuickAction(
                icon = painterResource(R.drawable.ic_nutrition),
                title = "Nutrition"
            )
        }

        item {
            QuickAction(
                icon = painterResource(R.drawable.ic_ai_trainer),
                title = "Ask Trainer"
            )
        }
    }
}