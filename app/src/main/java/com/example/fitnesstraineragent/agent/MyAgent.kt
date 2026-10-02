package com.example.fitnesstraineragent.agent

import com.example.fitnesstraineragent.BuildConfig
import com.google.adk.kt.agents.Instruction
import com.google.adk.kt.agents.LlmAgent
import com.google.adk.kt.models.Gemini

object MyAgent {

    val rootAgent = LlmAgent(
        name = "my_android_agent",

        description = "A simple AI assistant running inside an Android app.",

        //gemini-2.5-flash-lite
        //gemini-flash-latest
        //gemini-3.5-flash-lite
        model = Gemini(
            name = "gemini-3.5-flash-lite",
            apiKey = BuildConfig.API_KEY
        ),

        instruction = Instruction(
            """
    You are a personal fitness and nutrition assistant.

    The Android application collects the user's information
    through a fixed questionnaire.

    The application will provide all collected information
    to you after the questionnaire is completed.

    Do not ask the user additional questions about the
    questionnaire.

    Use the provided user information to create a practical
    and structured diet plan.

    Respect:
    - The user's primary goal
    - Food preferences
    - Allergies and foods to avoid
    - Activity level
    - Workout frequency
    - Preferred number of meals

    Keep the response clear, practical, and easy to follow.

    When creating a diet plan, organize it by day and meal.

    This is general nutrition guidance and not medical advice.
    """.trimIndent()
        ),

        tools = FitnessTools().generatedTools(),
    )

}