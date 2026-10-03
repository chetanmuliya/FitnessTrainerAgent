package com.example.fitnesstraineragent.presentation.trainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstraineragent.agent.AgentRunner
import com.example.fitnesstraineragent.data.DietQuestions
import com.example.fitnesstraineragent.data.model.DietPlan
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.coroutines.cancellation.CancellationException

class TrainerViewModel(

): ViewModel() {

    private val agentRunner = AgentRunner()

    private val questions = DietQuestions.questions

    private val _answeredQuestionId = MutableStateFlow<String?>(null)
    val answeredQuestionId = _answeredQuestionId.asStateFlow()

    private val _dietPlan = MutableStateFlow<DietPlan?>(null)
    val dietPlan = _dietPlan.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val sessionId = UUID.randomUUID().toString()
    private var lastQuestion: String? = null

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestion = _currentQuestionIndex
        .map { questions[it] }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            questions[0]
        )
    private val answers = mutableMapOf<String, String>()
    private var isCollectingDietData = false
    private var waitingForPlanConfirmation = false

    init {
        startDietPlan()
    }

    fun sendMessage(message: String) {

        if (message.isBlank()) return

        // Show user's message
        _messages.update {
            it + ChatMessage(
                text = message,
                isUser = true
            )
        }

        when {

            isCollectingDietData -> {
                viewModelScope.launch {
                    handleDietAnswer(message)
                }
            }

            waitingForPlanConfirmation -> {
                handlePlanConfirmation(message)
            }

            else -> {
                sendToGemini(message)
            }
        }
    }

    private fun handlePlanConfirmation(answer: String) {

        waitingForPlanConfirmation = false

        if (
            answer.equals("yes", ignoreCase = true) ||
            answer.equals("y", ignoreCase = true)
        ) {

            sendDietDataToGemini()

        } else {

            addTrainerMessage(
                "No problem. Let me know whenever you'd like me to create the diet plan."
            )
        }
    }

    private suspend fun handleDietAnswer(answer: String) {

        val index = _currentQuestionIndex.value
        val questionId = questions[index].id

        answers[questionId] = answer

        _answeredQuestionId.value = questionId

        delay(500)

        if (index < questions.lastIndex) {


            addTrainerMessage(
                questions[index+1].question
            )

            _currentQuestionIndex.value = index + 1


        } else {

            // All questions answered
            isCollectingDietData = false
            waitingForPlanConfirmation = true

            addTrainerMessage(
                "Thanks! I have all the information I need. Would you like me to create your diet plan?"
            )
        }
    }

    private fun sendDietDataToGemini() {

        val prompt = """
        Create a personalized 7-day diet plan based on the
        following user information.

        Goal:
        ${answers["goal"]}

        Age:
        ${answers["age"]}

        Gender:
        ${answers["gender"]}

        Height:
        ${answers["height"]} cm

        Weight:
        ${answers["weight"]} kg

        Activity level:
        ${answers["activity"]}

        Workout days per week:
        ${answers["workoutDays"]}

        Food preference:
        ${answers["foodPreference"]}

        Allergies / foods to avoid:
        ${answers["allergies"]}

        Meals per day:
        ${answers["mealsPerDay"]}

        Create a practical 7-day diet plan.

        For every day include:
        - Breakfast
        - Lunch
        - Snack
        - Dinner

        Respect the user's food preferences and
        allergies / foods to avoid.

        Return ONLY valid JSON.

        Do not use markdown.
        Do not use ```json.
        Do not add explanations before or after the JSON.

        Use exactly this structure:

        {
          "title": "7 Day Diet Plan",
          "days": [
            {
              "day": "Monday",
              "breakfast": "...",
              "lunch": "...",
              "snack": "...",
              "dinner": "..."
            }
          ]
        }

        The "days" array must contain exactly 7 days:
        Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday.

        This is general nutrition guidance, not medical advice.
    """.trimIndent()

        sendToGemini(prompt)
    }

    fun sendToGemini(message: String) {

        if (message.isBlank()) return

        lastQuestion = message

       /* // Add user message
        _messages.value = _messages.value + ChatMessage(
            text = message,
            isUser = true
        )*/

        viewModelScope.launch(Dispatchers.IO) {

            _isLoading.value = true

            try {

                agentRunner
                    .run(
                        sessionId = sessionId,
                        question = message
                    )
                    .collect { response ->

//                        _messages.value =
//                            _messages.value + ChatMessage(
//                                text = response,
//                                isUser = false
//                            )

                        try {
                            val plan = Gson().fromJson(
                                response,
                                DietPlan::class.java
                            )

                            _dietPlan.value = plan

                        } catch (e: Exception) {
                            _error.value = "Unable to parse diet plan"
                        }

                    }

            } catch (e: CancellationException) {

                throw e

            }catch (e: Exception) {

                _error.value = getErrorMessage(e)

            } finally {

                _isLoading.value = false
            }
        }
    }


    fun retry() {

        lastQuestion?.let { question ->
            sendMessage(question)
        }

    }

    fun startDietPlan() {

        _currentQuestionIndex.value = 0
        answers.clear()

        isCollectingDietData = true

        addTrainerMessage(
            questions[_currentQuestionIndex.value].question
        )
    }

    private fun addTrainerMessage(message: String) {

        _messages.update {
            it + ChatMessage(
                text = message,
                isUser = false
            )
        }
    }

    private fun getErrorMessage(
        exception: Exception
    ): String {

        return when {

            exception.message?.contains("503") == true ->
                "The AI trainer is temporarily busy. Please try again in a moment."

            exception.message?.contains("429") == true ->
                "Too many requests right now. Please wait a moment and try again."

            exception.message?.contains("401") == true ->
                "There is a problem with the AI configuration."

            exception.message?.contains("403") == true ->
                "The AI request was not authorized."

            exception.message?.contains("timeout", ignoreCase = true) == true ->
                "The request took too long. Please check your connection and try again."

            else ->
                "Something went wrong. Please try again.\n ${exception.message}"
        }
    }

}