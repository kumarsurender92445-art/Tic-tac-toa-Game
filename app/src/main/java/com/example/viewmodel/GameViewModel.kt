package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GamePreferencesRepository
import com.example.logic.TicTacToeEngine
import com.example.model.Difficulty
import com.example.model.GameMode
import com.example.model.GameState
import com.example.model.GameStatus
import com.example.model.Player
import com.example.sound.SoundManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: GamePreferencesRepository = GamePreferencesRepository(application),
    private val soundManager: SoundManager = SoundManager()
) : AndroidViewModel(application) {

    private val _gameState = MutableStateFlow(
        GameState(
            gameMode = repository.gameMode,
            difficulty = repository.difficulty,
            scoreX = repository.scoreX,
            scoreO = repository.scoreO,
            scoreDraws = repository.scoreDraws,
            isSoundEnabled = repository.isSoundEnabled,
            isDarkTheme = repository.isDarkTheme
        )
    )
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var aiMoveJob: Job? = null

    fun onCellClicked(index: Int) {
        val currentState = _gameState.value
        val currentStatus = currentState.gameStatus

        if (currentStatus !is GameStatus.InProgress) return
        if (currentState.isAiThinking) return
        if (currentState.board[index] != null) return

        val activePlayer = currentStatus.activePlayer
        executeMove(index, activePlayer)
    }

    private fun executeMove(index: Int, player: Player) {
        val state = _gameState.value
        val newBoard = state.board.toMutableList()
        newBoard[index] = player

        // Sound effect for move
        if (player == Player.X) {
            soundManager.playMoveX(state.isSoundEnabled)
        } else {
            soundManager.playMoveO(state.isSoundEnabled)
        }

        // Check for winner
        val winResult = TicTacToeEngine.checkWinner(newBoard)
        if (winResult != null) {
            val (winner, line) = winResult
            val updatedScoreX = if (winner == Player.X) state.scoreX + 1 else state.scoreX
            val updatedScoreO = if (winner == Player.O) state.scoreO + 1 else state.scoreO
            repository.scoreX = updatedScoreX
            repository.scoreO = updatedScoreO

            _gameState.update {
                it.copy(
                    board = newBoard,
                    gameStatus = GameStatus.Won(winner, line),
                    scoreX = updatedScoreX,
                    scoreO = updatedScoreO,
                    isAiThinking = false
                )
            }
            soundManager.playWin(state.isSoundEnabled)
            return
        }

        // Check for draw
        if (TicTacToeEngine.isBoardFull(newBoard)) {
            val updatedDraws = state.scoreDraws + 1
            repository.scoreDraws = updatedDraws

            _gameState.update {
                it.copy(
                    board = newBoard,
                    gameStatus = GameStatus.Draw,
                    scoreDraws = updatedDraws,
                    isAiThinking = false
                )
            }
            soundManager.playDraw(state.isSoundEnabled)
            return
        }

        // Switch to next player
        val nextPlayer = player.other()
        _gameState.update {
            it.copy(
                board = newBoard,
                gameStatus = GameStatus.InProgress(nextPlayer)
            )
        }

        // Trigger AI if applicable
        if (state.gameMode == GameMode.VS_AI && nextPlayer == Player.O) {
            scheduleAiMove(newBoard, state.difficulty)
        }
    }

    private fun scheduleAiMove(currentBoard: List<Player?>, difficulty: Difficulty) {
        aiMoveJob?.cancel()
        aiMoveJob = viewModelScope.launch {
            _gameState.update { it.copy(isAiThinking = true) }
            // Natural thinking delay between 350ms - 500ms
            delay(420)

            val aiMove = TicTacToeEngine.calculateAiMove(currentBoard, Player.O, difficulty)
            if (aiMove in 0..8 && currentBoard[aiMove] == null) {
                executeMove(aiMove, Player.O)
            } else {
                _gameState.update { it.copy(isAiThinking = false) }
            }
        }
    }

    fun restartGame() {
        aiMoveJob?.cancel()
        soundManager.playClick(_gameState.value.isSoundEnabled)
        _gameState.update {
            it.copy(
                board = List(9) { null },
                gameStatus = GameStatus.InProgress(Player.X),
                isAiThinking = false
            )
        }
    }

    fun resetScores() {
        soundManager.playClick(_gameState.value.isSoundEnabled)
        repository.resetScores()
        _gameState.update {
            it.copy(
                scoreX = 0,
                scoreO = 0,
                scoreDraws = 0
            )
        }
    }

    fun setGameMode(mode: GameMode) {
        if (_gameState.value.gameMode == mode) return
        soundManager.playClick(_gameState.value.isSoundEnabled)
        repository.gameMode = mode
        aiMoveJob?.cancel()
        _gameState.update {
            it.copy(
                gameMode = mode,
                board = List(9) { null },
                gameStatus = GameStatus.InProgress(Player.X),
                isAiThinking = false
            )
        }
    }

    fun setDifficulty(difficulty: Difficulty) {
        if (_gameState.value.difficulty == difficulty) return
        soundManager.playClick(_gameState.value.isSoundEnabled)
        repository.difficulty = difficulty
        _gameState.update { it.copy(difficulty = difficulty) }
    }

    fun toggleSound() {
        val newSound = !_gameState.value.isSoundEnabled
        repository.isSoundEnabled = newSound
        _gameState.update { it.copy(isSoundEnabled = newSound) }
        if (newSound) {
            soundManager.playClick(true)
        }
    }

    fun toggleTheme() {
        val newTheme = !_gameState.value.isDarkTheme
        repository.isDarkTheme = newTheme
        soundManager.playClick(_gameState.value.isSoundEnabled)
        _gameState.update { it.copy(isDarkTheme = newTheme) }
    }
}
