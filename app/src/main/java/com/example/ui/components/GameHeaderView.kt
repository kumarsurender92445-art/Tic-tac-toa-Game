package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink

@Composable
fun GameHeaderView(
    isSoundEnabled: Boolean,
    isDarkTheme: Boolean,
    onToggleSound: () -> Unit,
    onToggleTheme: () -> Unit,
    onResetScoresClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Title with glowing letters
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(NeonCyan)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "TIC",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = NeonCyan,
                letterSpacing = 1.sp
            )
            Text(
                text = " TAC ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = if (isDarkTheme) Color.White else Color(0xFF0F172A),
                letterSpacing = 1.sp
            )
            Text(
                text = "TOE",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = NeonPink,
                letterSpacing = 1.sp
            )
        }

        // Action Buttons Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeaderIconButton(
                icon = if (isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                contentDescription = if (isSoundEnabled) "Mute sound" else "Unmute sound",
                testTag = "sound_toggle_button",
                tint = if (isSoundEnabled) NeonCyan else (if (isDarkTheme) Color(0xFF64748B) else Color(0xFF94A3B8)),
                isDarkTheme = isDarkTheme,
                onClick = onToggleSound
            )

            HeaderIconButton(
                icon = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                contentDescription = if (isDarkTheme) "Switch to light mode" else "Switch to dark mode",
                testTag = "theme_toggle_button",
                tint = if (isDarkTheme) Color(0xFFFBBF24) else Color(0xFF6366F1),
                isDarkTheme = isDarkTheme,
                onClick = onToggleTheme
            )

            HeaderIconButton(
                icon = Icons.Default.RestartAlt,
                contentDescription = "Reset scoreboard",
                testTag = "reset_scores_button",
                tint = if (isDarkTheme) Color(0xFFEF4444) else Color(0xFFDC2626),
                isDarkTheme = isDarkTheme,
                onClick = onResetScoresClick
            )
        }
    }
}

@Composable
private fun HeaderIconButton(
    icon: ImageVector,
    contentDescription: String,
    testTag: String,
    tint: Color,
    isDarkTheme: Boolean,
    onClick: () -> Unit
) {
    val buttonBg = if (isDarkTheme) Color(0xFF141B2C) else Color(0xFFE2E8F0)
    val buttonBorder = if (isDarkTheme) DarkBorder else Color(0xFFCBD5E1)
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = Modifier
            .testTag(testTag)
            .size(44.dp)
            .clip(shape)
            .background(buttonBg)
            .border(1.dp, buttonBorder, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
    }
}
