package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.GameState
import com.example.ui.components.ActionButtonsView
import com.example.ui.components.GameBoardView
import com.example.ui.components.GameHeaderView
import com.example.ui.components.ModeSelectorView
import com.example.ui.components.ResetConfirmDialog
import com.example.ui.components.ScoreboardView
import com.example.ui.components.StatusBannerView
import com.example.ui.theme.DarkBg
import com.example.ui.theme.LightBg
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.viewmodel.GameViewModel

@Composable
fun TicTacToeScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.gameState.collectAsStateWithLifecycle()
    var showResetDialog by remember { mutableStateOf(false) }

    val bgGradient = if (state.isDarkTheme) {
        Brush.verticalGradient(
            colors = listOf(
                DarkBg,
                Color(0xFF0D1424),
                Color(0xFF090D18)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                LightBg,
                Color(0xFFF1F5F9),
                Color(0xFFE2E8F0)
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgGradient)
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 480.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header: Title & Action Toggles
            GameHeaderView(
                isSoundEnabled = state.isSoundEnabled,
                isDarkTheme = state.isDarkTheme,
                onToggleSound = { viewModel.toggleSound() },
                onToggleTheme = { viewModel.toggleTheme() },
                onResetScoresClick = { showResetDialog = true }
            )

            // Mode & Difficulty Selector
            ModeSelectorView(
                currentMode = state.gameMode,
                currentDifficulty = state.difficulty,
                isDarkTheme = state.isDarkTheme,
                onModeSelected = { viewModel.setGameMode(it) },
                onDifficultySelected = { viewModel.setDifficulty(it) }
            )

            // Turn or Win Status Banner
            StatusBannerView(
                gameStatus = state.gameStatus,
                gameMode = state.gameMode,
                isAiThinking = state.isAiThinking,
                isDarkTheme = state.isDarkTheme
            )

            // 3x3 Interactive Glowing Board
            GameBoardView(
                board = state.board,
                gameStatus = state.gameStatus,
                isDarkTheme = state.isDarkTheme,
                onCellClick = { viewModel.onCellClicked(it) }
            )

            // Scoreboard (Wins, Draws, Losses)
            ScoreboardView(
                scoreX = state.scoreX,
                scoreO = state.scoreO,
                scoreDraws = state.scoreDraws,
                gameMode = state.gameMode,
                isDarkTheme = state.isDarkTheme
            )

            // Primary Action Button (Restart / Play Again)
            ActionButtonsView(
                gameStatus = state.gameStatus,
                isDarkTheme = state.isDarkTheme,
                onRestartRound = { viewModel.restartGame() }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Confirmation Dialog for Resetting Scores
        if (showResetDialog) {
            ResetConfirmDialog(
                isDarkTheme = state.isDarkTheme,
                onConfirm = {
                    viewModel.resetScores()
                    showResetDialog = false
                },
                onDismiss = { showResetDialog = false }
            )
        }
    }
}
