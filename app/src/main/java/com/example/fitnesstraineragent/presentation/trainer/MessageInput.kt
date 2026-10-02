package com.example.fitnesstraineragent.presentation.trainer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fitnesstraineragent.R

@Composable
fun MessageInput(
    message: String,
    onMessageChange: (String) -> Unit,
    onSend: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        OutlinedTextField(

            value = message,

            onValueChange = onMessageChange,

            modifier = Modifier.weight(1f),

            placeholder = {
                Text("Ask your trainer...")
            },

            shape = RoundedCornerShape(20.dp),

            enabled = enabled
        )

        IconButton(
            onClick = onSend,

            enabled =
                enabled && message.isNotBlank()
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_send),
                contentDescription = "Send"
            )
        }
    }
}