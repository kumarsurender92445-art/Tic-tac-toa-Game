package com.example

import com.example.logic.TicTacToeEngine
import com.example.model.Difficulty
import com.example.model.LineType
import com.example.model.Player
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TicTacToeEngineTest {

    @Test
    fun testRowWinDetection() {
        val board = listOf(
            Player.X, Player.X, Player.X,
            null, Player.O, null,
            Player.O, null, null
        )
        val result = TicTacToeEngine.checkWinner(board)
        assertNotNull(result)
        assertEquals(Player.X, result?.first)
        assertEquals(LineType.ROW_0, result?.second?.lineType)
    }

    @Test
    fun testColumnWinDetection() {
        val board = listOf(
            Player.O, Player.X, null,
            Player.O, Player.X, null,
            Player.O, null, Player.X
        )
        val result = TicTacToeEngine.checkWinner(board)
        assertNotNull(result)
        assertEquals(Player.O, result?.first)
        assertEquals(LineType.COL_0, result?.second?.lineType)
    }

    @Test
    fun testDiagonalWinDetection() {
        val board = listOf(
            Player.X, Player.O, null,
            null, Player.X, Player.O,
            null, null, Player.X
        )
        val result = TicTacToeEngine.checkWinner(board)
        assertNotNull(result)
        assertEquals(Player.X, result?.first)
        assertEquals(LineType.DIAG_MAIN, result?.second?.lineType)
    }

    @Test
    fun testDrawDetection() {
        // Full board with no 3-in-a-row
        val board = listOf(
            Player.X, Player.O, Player.X,
            Player.X, Player.O, Player.O,
            Player.O, Player.X, Player.X
        )
        val winResult = TicTacToeEngine.checkWinner(board)
        assertNull(winResult)
        assertTrue(TicTacToeEngine.isBoardFull(board))
    }

    @Test
    fun testMediumAiTakesWinningMove() {
        // AI is O. If O has 2 in a row at 0 and 1, AI should pick 2 to win
        val board = listOf(
            Player.O, Player.O, null,
            Player.X, Player.X, null,
            null, null, null
        )
        val move = TicTacToeEngine.calculateAiMove(board, Player.O, Difficulty.MEDIUM)
        assertEquals(2, move)
    }

    @Test
    fun testMediumAiBlocksOpponentWin() {
        // AI is O. Opponent X has 2 in a row at 3 and 4, AI must block at 5
        val board = listOf(
            Player.O, null, null,
            Player.X, Player.X, null,
            null, null, null
        )
        val move = TicTacToeEngine.calculateAiMove(board, Player.O, Difficulty.MEDIUM)
        assertEquals(5, move)
    }

    @Test
    fun testHardAiNeverLoses() {
        // Hard AI (Minimax) against random player moves should never lose
        for (seed in 1..20) {
            val board = MutableList<Player?>(9) { null }
            var turn = Player.X // Human is X, AI is O

            while (!TicTacToeEngine.isBoardFull(board) && TicTacToeEngine.checkWinner(board) == null) {
                if (turn == Player.X) {
                    val available = TicTacToeEngine.getAvailableMoves(board)
                    board[available.random()] = Player.X
                } else {
                    val aiMove = TicTacToeEngine.calculateAiMove(board, Player.O, Difficulty.HARD)
                    assertTrue("AI move must be in available moves", aiMove in TicTacToeEngine.getAvailableMoves(board))
                    board[aiMove] = Player.O
                }
                turn = turn.other()
            }

            val winner = TicTacToeEngine.checkWinner(board)?.first
            assertTrue("Hard AI should never lose! Winner was $winner on board $board", winner != Player.X)
        }
    }
}
