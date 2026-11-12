package com.example.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

@Composable
fun ResetConfirmDialog(
    isDarkTheme: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val dialogBg = if (isDarkTheme) Color(0xFF141B2C) else Color.White

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Reset Scores?",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = if (isDarkTheme) Color.White else Color(0xFF0F172A)
            )
        },
        text = {
            Text(
                text = stringResource(R.string.reset_scores_confirm),
                fontSize = 14.sp,
                color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF475569)
            )
        },
        containerColor = dialogBg,
        shape = RoundedCornerShape(20.dp),
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDC2626),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = androidx.compose.ui.Modifier.testTag("confirm_reset_scores")
            ) {
                Text(text = "Reset All", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                ),
                modifier = androidx.compose.ui.Modifier.testTag("cancel_reset_scores")
            ) {
                Text(text = "Cancel")
            }
        }
    )
}
