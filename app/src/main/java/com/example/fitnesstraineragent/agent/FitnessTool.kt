package com.example.fitnesstraineragent.agent

import com.google.adk.kt.annotations.Param
import com.google.adk.kt.annotations.Tool

class FitnessTools {

    @Tool
    fun calculateBMI(
        @Param("User's weight in kilograms")
        weightKg: Double,

        @Param("User's height in centimeters")
        heightCm: Double
    ): BmiResult  {

        val heightMeters = heightCm / 100.0
        val bmi = weightKg / (heightMeters * heightMeters)

        val category = when {
            bmi < 18.5 -> "Underweight"
            bmi < 25.0 -> "Normal"
            bmi < 30.0 -> "Overweight"
            else -> "Obesity"
        }

        return BmiResult(
            bmi = String.format("%.1f", bmi).toDouble(),
            category = category
        )
    }
}