package com.example.logic

import com.example.model.Difficulty
import com.example.model.LineType
import com.example.model.Player
import com.example.model.WinningLine
import kotlin.random.Random

object TicTacToeEngine {

    val WINNING_COMBINATIONS = listOf(
        Triple(0, 1, 2) to LineType.ROW_0,
        Triple(3, 4, 5) to LineType.ROW_1,
        Triple(6, 7, 8) to LineType.ROW_2,
        Triple(0, 3, 6) to LineType.COL_0,
        Triple(1, 4, 7) to LineType.COL_1,
        Triple(2, 5, 8) to LineType.COL_2,
        Triple(0, 4, 8) to LineType.DIAG_MAIN,
        Triple(2, 4, 6) to LineType.DIAG_ANTI
    )

    fun checkWinner(board: List<Player?>): Pair<Player, WinningLine>? {
        for ((comb, lineType) in WINNING_COMBINATIONS) {
            val p0 = board[comb.first]
            val p1 = board[comb.second]
            val p2 = board[comb.third]
            if (p0 != null && p0 == p1 && p1 == p2) {
                return p0 to WinningLine(listOf(comb.first, comb.second, comb.third), lineType)
            }
        }
        return null
    }

    fun isBoardFull(board: List<Player?>): Boolean {
        return board.none { it == null }
    }

    fun getAvailableMoves(board: List<Player?>): List<Int> {
        return board.indices.filter { board[it] == null }
    }

    fun calculateAiMove(
        board: List<Player?>,
        aiPlayer: Player,
        difficulty: Difficulty
    ): Int {
        val availableMoves = getAvailableMoves(board)
        if (availableMoves.isEmpty()) return -1
        if (availableMoves.size == 1) return availableMoves.first()

        return when (difficulty) {
            Difficulty.EASY -> getEasyMove(board, aiPlayer, availableMoves)
            Difficulty.MEDIUM -> getMediumMove(board, aiPlayer, availableMoves)
            Difficulty.HARD -> getHardMove(board, aiPlayer)
        }
    }

    private fun getEasyMove(
        board: List<Player?>,
        aiPlayer: Player,
        availableMoves: List<Int>
    ): Int {
        // Mostly random, occasionally takes immediate win
        if (Random.nextFloat() < 0.25f) {
            val winningMove = findWinningMove(board, aiPlayer, availableMoves)
            if (winningMove != null) return winningMove
        }
        return availableMoves.random()
    }

    private fun getMediumMove(
        board: List<Player?>,
        aiPlayer: Player,
        availableMoves: List<Int>
    ): Int {
        // 1. Can AI win immediately?
        val winMove = findWinningMove(board, aiPlayer, availableMoves)
        if (winMove != null) return winMove

        // 2. Can opponent win immediately? Block it!
        val blockMove = findWinningMove(board, aiPlayer.other(), availableMoves)
        if (blockMove != null) return blockMove

        // 3. Take center if available (70% chance)
        if (4 in availableMoves && Random.nextFloat() < 0.70f) {
            return 4
        }

        // 4. Prefer corners
        val corners = listOf(0, 2, 6, 8).filter { it in availableMoves }
        if (corners.isNotEmpty() && Random.nextFloat() < 0.50f) {
            return corners.random()
        }

        return availableMoves.random()
    }

    private fun getHardMove(board: List<Player?>, aiPlayer: Player): Int {
        var bestScore = Int.MIN_VALUE
        var bestMove = -1
        val human = aiPlayer.other()

        // If board is empty, picking a corner or center is fast and optimal
        val emptyCount = board.count { it == null }
        if (emptyCount == 9) {
            return listOf(0, 2, 4, 6, 8).random()
        }

        val available = getAvailableMoves(board)
        for (move in available) {
            val nextBoard = board.toMutableList()
            nextBoard[move] = aiPlayer

            val score = minimax(
                board = nextBoard,
                depth = 0,
                isMaximizing = false,
                aiPlayer = aiPlayer,
                humanPlayer = human,
                alpha = Int.MIN_VALUE,
                beta = Int.MAX_VALUE
            )

            if (score > bestScore) {
                bestScore = score
                bestMove = move
            }
        }

        return if (bestMove != -1) bestMove else available.random()
    }

    private fun minimax(
        board: MutableList<Player?>,
        depth: Int,
        isMaximizing: Boolean,
        aiPlayer: Player,
        humanPlayer: Player,
        alpha: Int,
        beta: Int
    ): Int {
        val winnerInfo = checkWinner(board)
        if (winnerInfo != null) {
            return if (winnerInfo.first == aiPlayer) {
                10 - depth
            } else {
                depth - 10
            }
        }

        if (isBoardFull(board)) {
            return 0
        }

        var currentAlpha = alpha
        var currentBeta = beta

        if (isMaximizing) {
            var maxScore = Int.MIN_VALUE
            for (i in board.indices) {
                if (board[i] == null) {
                    board[i] = aiPlayer
                    val score = minimax(board, depth + 1, false, aiPlayer, humanPlayer, currentAlpha, currentBeta)
                    board[i] = null
                    maxScore = maxOf(maxScore, score)
                    currentAlpha = maxOf(currentAlpha, score)
                    if (currentBeta <= currentAlpha) break
                }
            }
            return maxScore
        } else {
            var minScore = Int.MAX_VALUE
            for (i in board.indices) {
                if (board[i] == null) {
                    board[i] = humanPlayer
                    val score = minimax(board, depth + 1, true, aiPlayer, humanPlayer, currentAlpha, currentBeta)
                    board[i] = null
                    minScore = minOf(minScore, score)
                    currentBeta = minOf(currentBeta, score)
                    if (currentBeta <= currentAlpha) break
                }
            }
            return minScore
        }
    }

    private fun findWinningMove(
        board: List<Player?>,
        player: Player,
        availableMoves: List<Int>
    ): Int? {
        for (move in availableMoves) {
            val testBoard = board.toMutableList()
            testBoard[move] = player
            val result = checkWinner(testBoard)
            if (result != null && result.first == player) {
                return move
            }
        }
        return null
    }
}
