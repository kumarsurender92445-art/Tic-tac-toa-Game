package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameMode
import com.example.model.GameStatus
import com.example.model.Player
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGold
import com.example.ui.theme.NeonPink

@Composable
fun StatusBannerView(
    gameStatus: GameStatus,
    gameMode: GameMode,
    isAiThinking: Boolean,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        modifier = modifier
            .testTag("status_banner")
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = gameStatus to isAiThinking,
            transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(200)) },
            label = "statusAnimation"
        ) { (status, aiThinking) ->
            when {
                aiThinking -> {
                    StatusPill(
                        bgColor = if (isDarkTheme) Color(0xFF1E1B4B) else Color(0xFFEDE9FE),
                        borderColor = Color(0xFF8B5CF6),
                        isDarkTheme = isDarkTheme
                    ) {
                        Text(
                            text = "🤖",
                            fontSize = 18.sp,
                            modifier = Modifier.scale(pulseScale)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI is thinking…",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA78BFA)
                        )
                    }
                }

                status is GameStatus.Won -> {
                    val winner = status.winner
                    val isX = winner == Player.X
                    val accentColor = if (isX) NeonCyan else NeonPink
                    val winText = when {
                        gameMode == GameMode.VS_AI && isX -> "You Won! 🎉"
                        gameMode == GameMode.VS_AI && !isX -> "AI Won! 🤖"
                        isX -> "Player X Wins! 🏆"
                        else -> "Player O Wins! 🏆"
                    }

                    StatusPill(
                        bgColor = accentColor.copy(alpha = if (isDarkTheme) 0.20f else 0.15f),
                        borderColor = accentColor,
                        isDarkTheme = isDarkTheme
                    ) {
                        if (isX) {
                            GlowingX(size = 22.dp, animate = false)
                        } else {
                            GlowingO(size = 22.dp, animate = false)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = winText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = accentColor
                        )
                    }
                }

                status is GameStatus.Draw -> {
                    StatusPill(
                        bgColor = NeonGold.copy(alpha = if (isDarkTheme) 0.18f else 0.14f),
                        borderColor = NeonGold,
                        isDarkTheme = isDarkTheme
                    ) {
                        Text(text = "🤝", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "It's a Draw!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonGold
                        )
                    }
                }

                status is GameStatus.InProgress -> {
                    val activePlayer = status.activePlayer
                    val isX = activePlayer == Player.X
                    val turnColor = if (isX) NeonCyan else NeonPink
                    val turnText = when {
                        gameMode == GameMode.VS_AI && isX -> "Your Turn"
                        gameMode == GameMode.VS_AI && !isX -> "AI Turn"
                        isX -> "Player X's Turn"
                        else -> "Player O's Turn"
                    }

                    StatusPill(
                        bgColor = if (isDarkTheme) Color(0xFF111827) else Color.White,
                        borderColor = turnColor.copy(alpha = 0.6f),
                        isDarkTheme = isDarkTheme
                    ) {
                        Box(modifier = Modifier.scale(pulseScale)) {
                            if (isX) {
                                GlowingX(size = 20.dp, animate = false)
                            } else {
                                GlowingO(size = 20.dp, animate = false)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = turnText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkTheme) Color.White else Color(0xFF0F172A)
                        )
                    }
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun StatusPill(
    bgColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(24.dp)
    Row(
        modifier = Modifier
            .clip(shape)
            .background(bgColor)
            .border(1.5.dp, borderColor, shape)
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        content()
    }
}
