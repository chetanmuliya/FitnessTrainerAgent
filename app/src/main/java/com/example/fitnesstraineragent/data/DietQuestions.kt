package com.example.fitnesstraineragent.data

import com.example.fitnesstraineragent.data.model.DietQuestion

object  DietQuestions {

    val questions = listOf(

        DietQuestion(
            id = "goal",
            question = "What is your primary goal?",
            options = listOf(
                "Lose weight",
                "Build muscle",
                "Maintain weight"
            )
        ),

        DietQuestion(
            id = "age",
            question = "What is your age?"
        ),

        DietQuestion(
            id = "gender",
            question = "What is your gender?",
            options = listOf(
                "Male",
                "Female",
                "Other"
            )
        ),

        DietQuestion(
            id = "height",
            question = "What is your height in cm?"
        ),

        DietQuestion(
            id = "weight",
            question = "What is your weight in kg?"
        ),

        DietQuestion(
            id = "activity",
            question = "What is your daily activity level?",
            options = listOf(
                "Sedentary",
                "Lightly active",
                "Moderately active",
                "Very active"
            )
        ),

        DietQuestion(
            id = "workoutDays",
            question = "How many days per week do you work out?",
            options = listOf(
                "1 day",
                "2 days",
                "3 days",
                "4 days",
                "5 days",
                "6 days",
                "7 days"
            )
        ),

        DietQuestion(
            id = "foodPreference",
            question = "What type of food do you prefer?",
            options = listOf(
                "Vegetarian",
                "Non-vegetarian",
                "Vegan"
            )
        ),

        DietQuestion(
            id = "allergies",
            question = "Do you have any allergies or foods you want to avoid?"
        ),

        DietQuestion(
            id = "mealsPerDay",
            question = "How many meals do you prefer per day?",
            options = listOf(
                "2 meals",
                "3 meals",
                "4 meals",
                "5 meals",
                "6 meals"
            )
        )
    )

}