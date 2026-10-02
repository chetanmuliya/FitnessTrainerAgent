package com.example.fitnesstraineragent.data.model

data class DietQuestion(
    val id: String,
    val question: String,
    val options: List<String> = emptyList()
)