package com.example.model

enum class Player {
    X,
    O;

    fun other(): Player = if (this == X) O else X
}

enum class GameMode {
    VS_AI,
    TWO_PLAYER
}

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD
}

enum class LineType {
    ROW_0, ROW_1, ROW_2,
    COL_0, COL_1, COL_2,
    DIAG_MAIN, DIAG_ANTI
}

data class WinningLine(
    val indices: List<Int>,
    val lineType: LineType
)

sealed class GameStatus {
    data class InProgress(val activePlayer: Player) : GameStatus()
    data class Won(val winner: Player, val winningLine: WinningLine) : GameStatus()
    data object Draw : GameStatus()
}

data class GameState(
    val board: List<Player?> = List(9) { null },
    val gameStatus: GameStatus = GameStatus.InProgress(Player.X),
    val gameMode: GameMode = GameMode.VS_AI,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val scoreX: Int = 0,
    val scoreO: Int = 0,
    val scoreDraws: Int = 0,
    val isAiThinking: Boolean = false,
    val isSoundEnabled: Boolean = true,
    val isDarkTheme: Boolean = true
)
