package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.GameStatus
import com.example.model.LineType
import com.example.model.Player
import com.example.model.WinningLine
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanGlow
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPinkGlow

@Composable
fun GameBoardView(
    board: List<Player?>,
    gameStatus: GameStatus,
    isDarkTheme: Boolean,
    onCellClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val winningLine = (gameStatus as? GameStatus.Won)?.winningLine
    val winningIndices = winningLine?.indices ?: emptyList()
    val winnerPlayer = (gameStatus as? GameStatus.Won)?.winner

    val boardBorderColor = if (isDarkTheme) {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.8f)
    }

    val boardBg = if (isDarkTheme) {
        Color(0xFF0F1523)
    } else {
        Color(0xFFF1F5F9)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(
                elevation = if (isDarkTheme) 12.dp else 6.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = if (isDarkTheme) NeonCyan.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.08f)
            ),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = boardBg),
        border = BorderStroke(1.5.dp, boardBorderColor)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            val cellSpacing = 8.dp

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(cellSpacing)
            ) {
                for (row in 0..2) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(cellSpacing)
                    ) {
                        for (col in 0..2) {
                            val index = row * 3 + col
                            val player = board[index]
                            val isWinningCell = index in winningIndices

                            BoardCell(
                                player = player,
                                isWinningCell = isWinningCell,
                                winnerPlayer = winnerPlayer,
                                isDarkTheme = isDarkTheme,
                                index = index,
                                onClick = { onCellClick(index) },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                            )
                        }
                    }
                }
            }

            // Winning Strike-Through Laser Line Overlay
            if (winningLine != null && winnerPlayer != null) {
                WinningStrikeLine(
                    winningLine = winningLine,
                    winner = winnerPlayer,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun BoardCell(
    player: Player?,
    isWinningCell: Boolean,
    winnerPlayer: Player?,
    isDarkTheme: Boolean,
    index: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cellShape = RoundedCornerShape(20.dp)

    val targetBg = when {
        isWinningCell && winnerPlayer == Player.X -> {
            if (isDarkTheme) NeonCyan.copy(alpha = 0.18f) else NeonCyan.copy(alpha = 0.14f)
        }
        isWinningCell && winnerPlayer == Player.O -> {
            if (isDarkTheme) NeonPink.copy(alpha = 0.18f) else NeonPink.copy(alpha = 0.14f)
        }
        isDarkTheme -> Color(0xFF141B2C)
        else -> Color.White
    }

    val cellBg by animateColorAsState(
        targetValue = targetBg,
        animationSpec = tween(300),
        label = "cellBg"
    )

    val targetBorder = when {
        isWinningCell && winnerPlayer == Player.X -> NeonCyan
        isWinningCell && winnerPlayer == Player.O -> NeonPink
        isDarkTheme -> Color(0xFF1F2A42)
        else -> Color(0xFFE2E8F0)
    }

    val cellBorder by animateColorAsState(
        targetValue = targetBorder,
        animationSpec = tween(300),
        label = "cellBorder"
    )

    Box(
        modifier = modifier
            .testTag("cell_$index")
            .clip(cellShape)
            .background(cellBg)
            .border(
                width = if (isWinningCell) 2.dp else 1.dp,
                color = cellBorder,
                shape = cellShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        when (player) {
            Player.X -> GlowingX(size = 54.dp, animate = true)
            Player.O -> GlowingO(size = 54.dp, animate = true)
            null -> Unit
        }
    }
}

@Composable
private fun WinningStrikeLine(
    winningLine: WinningLine,
    winner: Player,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(winningLine) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 380, easing = FastOutSlowInEasing)
        )
    }

    val (laserColor, laserGlow) = if (winner == Player.X) {
        NeonCyan to NeonCyanGlow
    } else {
        NeonPink to NeonPinkGlow
    }

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val (start, end) = when (winningLine.lineType) {
            LineType.ROW_0 -> Offset(w * 0.08f, h * 0.166f) to Offset(w * 0.92f, h * 0.166f)
            LineType.ROW_1 -> Offset(w * 0.08f, h * 0.5f) to Offset(w * 0.92f, h * 0.5f)
            LineType.ROW_2 -> Offset(w * 0.08f, h * 0.833f) to Offset(w * 0.92f, h * 0.833f)

            LineType.COL_0 -> Offset(w * 0.166f, h * 0.08f) to Offset(w * 0.166f, h * 0.92f)
            LineType.COL_1 -> Offset(w * 0.5f, h * 0.08f) to Offset(w * 0.5f, h * 0.92f)
            LineType.COL_2 -> Offset(w * 0.833f, h * 0.08f) to Offset(w * 0.833f, h * 0.92f)

            LineType.DIAG_MAIN -> Offset(w * 0.10f, h * 0.10f) to Offset(w * 0.90f, h * 0.90f)
            LineType.DIAG_ANTI -> Offset(w * 0.90f, h * 0.10f) to Offset(w * 0.10f, h * 0.90f)
        }

        val currentEnd = Offset(
            x = start.x + (end.x - start.x) * progress.value,
            y = start.y + (end.y - start.y) * progress.value
        )

        // Outer neon aura
        drawLine(
            color = laserGlow.copy(alpha = 0.45f),
            start = start,
            end = currentEnd,
            strokeWidth = 24.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Mid laser bloom
        drawLine(
            color = laserGlow.copy(alpha = 0.85f),
            start = start,
            end = currentEnd,
            strokeWidth = 14.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Crisp luminous laser line
        drawLine(
            color = laserColor,
            start = start,
            end = currentEnd,
            strokeWidth = 6.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Hyper-bright center white core
        drawLine(
            color = Color.White.copy(alpha = 0.95f),
            start = start,
            end = currentEnd,
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}
