package com.example.fitnesstraineragent.presentation.trainer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnesstraineragent.data.model.DietPlan

@Composable
fun DietPlanScreen(
    modifier: Modifier = Modifier,
    viewModel: TrainerViewModel = viewModel()
) {

    val dietPlan by viewModel.dietPlan
        .collectAsStateWithLifecycle()

    val plan = dietPlan!!

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            Text(
                text = plan.title,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        items(plan.days) { day ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = day.day,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("🍳 Breakfast")
                    Text(day.breakfast)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("🥗 Lunch")
                    Text(day.lunch)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("🍎 Snack")
                    Text(day.snack)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("🍽️ Dinner")
                    Text(day.dinner)
                }
            }
        }
    }

}