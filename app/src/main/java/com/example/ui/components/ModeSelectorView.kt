package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Difficulty
import com.example.model.GameMode
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGold
import com.example.ui.theme.NeonPink

@Composable
fun ModeSelectorView(
    currentMode: GameMode,
    currentDifficulty: Difficulty,
    isDarkTheme: Boolean,
    onModeSelected: (GameMode) -> Unit,
    onDifficultySelected: (Difficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Mode Selector (Vs AI vs 2 Players)
        val containerBg = if (isDarkTheme) Color(0xFF111827) else Color(0xFFE2E8F0)
        val pillShape = RoundedCornerShape(14.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(pillShape)
                .background(containerBg)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ModeTab(
                title = "🤖 Vs Computer",
                isSelected = currentMode == GameMode.VS_AI,
                isDarkTheme = isDarkTheme,
                testTag = "mode_vs_ai",
                onClick = { onModeSelected(GameMode.VS_AI) },
                modifier = Modifier.weight(1f)
            )

            ModeTab(
                title = "👥 2 Players",
                isSelected = currentMode == GameMode.TWO_PLAYER,
                isDarkTheme = isDarkTheme,
                testTag = "mode_two_player",
                onClick = { onModeSelected(GameMode.TWO_PLAYER) },
                modifier = Modifier.weight(1f)
            )
        }

        // Difficulty Selector (Only visible in VS_AI mode)
        AnimatedVisibility(
            visible = currentMode == GameMode.VS_AI,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DifficultyChip(
                    title = "Easy",
                    isSelected = currentDifficulty == Difficulty.EASY,
                    accentColor = NeonCyan,
                    isDarkTheme = isDarkTheme,
                    testTag = "diff_easy",
                    onClick = { onDifficultySelected(Difficulty.EASY) },
                    modifier = Modifier.weight(1f)
                )

                DifficultyChip(
                    title = "Medium",
                    isSelected = currentDifficulty == Difficulty.MEDIUM,
                    accentColor = NeonGold,
                    isDarkTheme = isDarkTheme,
                    testTag = "diff_medium",
                    onClick = { onDifficultySelected(Difficulty.MEDIUM) },
                    modifier = Modifier.weight(1f)
                )

                DifficultyChip(
                    title = "Hard (AI)",
                    isSelected = currentDifficulty == Difficulty.HARD,
                    accentColor = NeonPink,
                    isDarkTheme = isDarkTheme,
                    testTag = "diff_hard",
                    onClick = { onDifficultySelected(Difficulty.HARD) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ModeTab(
    title: String,
    isSelected: Boolean,
    isDarkTheme: Boolean,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val targetBg = when {
        isSelected && isDarkTheme -> Color(0xFF1E293B)
        isSelected && !isDarkTheme -> Color.White
        else -> Color.Transparent
    }

    val targetTextColor = when {
        isSelected && isDarkTheme -> NeonCyan
        isSelected && !isDarkTheme -> Color(0xFF0284C7)
        isDarkTheme -> Color(0xFF94A3B8)
        else -> Color(0xFF64748B)
    }

    val targetBorder = when {
        isSelected && isDarkTheme -> NeonCyan.copy(alpha = 0.5f)
        isSelected && !isDarkTheme -> Color(0xFF0284C7).copy(alpha = 0.4f)
        else -> Color.Transparent
    }

    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .testTag(testTag)
            .height(44.dp)
            .clip(shape)
            .background(targetBg)
            .border(1.dp, targetBorder, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = targetTextColor
        )
    }
}

@Composable
private fun DifficultyChip(
    title: String,
    isSelected: Boolean,
    accentColor: Color,
    isDarkTheme: Boolean,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)

    val targetBg = when {
        isSelected && isDarkTheme -> accentColor.copy(alpha = 0.18f)
        isSelected && !isDarkTheme -> accentColor.copy(alpha = 0.15f)
        isDarkTheme -> Color(0xFF141A28)
        else -> Color.White
    }

    val targetBorder = when {
        isSelected -> accentColor
        isDarkTheme -> DarkBorder
        else -> Color(0xFFCBD5E1)
    }

    val targetText = when {
        isSelected -> accentColor
        isDarkTheme -> Color(0xFF94A3B8)
        else -> Color(0xFF475569)
    }

    Box(
        modifier = modifier
            .testTag(testTag)
            .height(36.dp)
            .clip(shape)
            .background(targetBg)
            .border(if (isSelected) 1.5.dp else 1.dp, targetBorder, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = targetText
        )
    }
}
