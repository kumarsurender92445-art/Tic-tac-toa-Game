package com.example.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.GameStatus
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink

@Composable
fun ActionButtonsView(
    gameStatus: GameStatus,
    isDarkTheme: Boolean,
    onRestartRound: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isGameOver = gameStatus !is GameStatus.InProgress
    val buttonColor = when {
        isGameOver && isDarkTheme -> NeonCyan
        isGameOver && !isDarkTheme -> Color(0xFF0284C7)
        isDarkTheme -> Color(0xFF1E293B)
        else -> Color(0xFFE2E8F0)
    }

    val contentColor = when {
        isGameOver -> Color.Black
        isDarkTheme -> Color.White
        else -> Color(0xFF0F172A)
    }

    Button(
        onClick = onRestartRound,
        modifier = modifier
            .testTag("restart_button")
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isGameOver) 6.dp else 2.dp,
            pressedElevation = 2.dp
        )
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier.height(20.dp)
        )
        Text(
            text = if (isGameOver) "Play Again" else "Restart Round",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
