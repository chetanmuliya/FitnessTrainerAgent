package com.example.fitnesstraineragent.presentation.trainer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnesstraineragent.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerScreen(
    onBackClick: () -> Unit,
    onDietPlanReady: () -> Unit,
    viewModel: TrainerViewModel = viewModel()
) {

    var message by remember {
        mutableStateOf("")
    }

    val messages by viewModel.messages
        .collectAsStateWithLifecycle()

    val isLoading by viewModel.isLoading
        .collectAsStateWithLifecycle()

    val error by viewModel.error
        .collectAsStateWithLifecycle()

    val question by viewModel.currentQuestion
        .collectAsStateWithLifecycle()

    val answeredQuestionId by viewModel.answeredQuestionId
        .collectAsStateWithLifecycle()

    val dietPlan by viewModel.dietPlan
        .collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {

        if (messages.isNotEmpty()) {

            listState.animateScrollToItem(
                messages.lastIndex
            )
        }
    }

    LaunchedEffect(dietPlan) {

        if (dietPlan != null) {
            onDietPlanReady()
        }
    }


    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("AI Trainer")
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBackClick
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),

                state = listState,

                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                items(messages) { chatMessage ->

                    MessageBubble(
                        message = chatMessage
                    )
                }

                if (question.options.isNotEmpty() &&
                    answeredQuestionId != question.id) {

                    item {

                        Column(
                            modifier = Modifier.padding(1.dp),
                            horizontalAlignment = Alignment.Start
                        ) {

                            question.options.forEach { option ->

                                Button(
                                    onClick = {
                                        viewModel.sendMessage(option)
                                    },
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Text(option)
                                }
                            }
                        }
                    }
                }

                if (isLoading) {

                    item {

                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            CircularProgressIndicator(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                            )

                            Spacer(
                                modifier = Modifier.width(8.dp)
                            )

                            Text("Your trainer is thinking...")
                        }
                    }
                }

                error?.let { errorMessage ->

                    item {

                        ErrorMessage(
                            message = errorMessage,
                            onRetry = {
                                viewModel.retry()
                            }
                        )
                    }
                }

            }

            MessageInput(
                message = message,

                onMessageChange = {
                    message = it
                },

                onSend = {

                    viewModel.sendMessage(message)

                    message = ""
                },

                enabled = !isLoading,
                modifier = Modifier.imePadding()
            )
        }
    }
}