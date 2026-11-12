package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameMode
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGold
import com.example.ui.theme.NeonPink

@Composable
fun ScoreboardView(
    scoreX: Int,
    scoreO: Int,
    scoreDraws: Int,
    gameMode: GameMode,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val cardBg = if (isDarkTheme) Color(0xFF111827) else Color.White
    val cardBorder = if (isDarkTheme) DarkBorder else Color(0xFFE2E8F0)

    val labelX = if (gameMode == GameMode.VS_AI) "You (X)" else "Player X"
    val labelO = if (gameMode == GameMode.VS_AI) "AI (O)" else "Player O"

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(1.dp, cardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Player X Column
            ScoreItem(
                symbolComposable = { GlowingX(size = 24.dp, animate = false) },
                label = labelX,
                score = scoreX,
                highlightColor = NeonCyan,
                isLeader = scoreX > scoreO && scoreX > 0,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )

            // Vertical Divider
            Box(
                modifier = Modifier
                    .size(width = 1.dp, height = 40.dp)
                    .background(cardBorder.copy(alpha = 0.8f))
            )

            // Draws Column
            ScoreItem(
                symbolComposable = {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(NeonGold.copy(alpha = 0.15f))
                            .border(1.dp, NeonGold.copy(alpha = 0.7f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "=",
                            color = NeonGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                },
                label = "Ties",
                score = scoreDraws,
                highlightColor = NeonGold,
                isLeader = false,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )

            // Vertical Divider
            Box(
                modifier = Modifier
                    .size(width = 1.dp, height = 40.dp)
                    .background(cardBorder.copy(alpha = 0.8f))
            )

            // Player O Column
            ScoreItem(
                symbolComposable = { GlowingO(size = 24.dp, animate = false) },
                label = labelO,
                score = scoreO,
                highlightColor = NeonPink,
                isLeader = scoreO > scoreX && scoreO > 0,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ScoreItem(
    symbolComposable: @Composable () -> Unit,
    label: String,
    score: Int,
    highlightColor: Color,
    isLeader: Boolean,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            symbolComposable()
            if (isLeader) {
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "👑",
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = score.toString(),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = if (isLeader) highlightColor else (if (isDarkTheme) Color.White else Color(0xFF0F172A)),
            textAlign = TextAlign.Center
        )
    }
}
