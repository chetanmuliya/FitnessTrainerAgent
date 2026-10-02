package com.example.fitnesstraineragent.agent

import com.google.adk.kt.runners.InMemoryRunner
import com.google.adk.kt.sessions.InMemorySessionService
import com.google.adk.kt.types.Content
import com.google.adk.kt.types.Part
import com.google.adk.kt.types.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AgentRunner {

    private val sessionService = InMemorySessionService()

    private val runner = InMemoryRunner(
        agent = MyAgent.rootAgent,
        sessionService = sessionService
    )
    fun run(
        sessionId: String,
        question: String
    ): Flow<String> = flow {

        runner.runAsync(
            userId = "user-123",
            sessionId = sessionId,

            newMessage = Content(
                role = Role.USER,
                parts = listOf(
                    Part(text = question)
                )
            )

        ).collect { event ->

            val text = event.content
                ?.parts
                ?.firstOrNull()
                ?.text

            if (!text.isNullOrBlank()) {
                emit(text)
            }
        }
    }

}