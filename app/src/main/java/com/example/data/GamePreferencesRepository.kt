package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.Difficulty
import com.example.model.GameMode

class GamePreferencesRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var scoreX: Int
        get() = prefs.getInt(KEY_SCORE_X, 0)
        set(value) = prefs.edit().putInt(KEY_SCORE_X, value).apply()

    var scoreO: Int
        get() = prefs.getInt(KEY_SCORE_O, 0)
        set(value) = prefs.edit().putInt(KEY_SCORE_O, value).apply()

    var scoreDraws: Int
        get() = prefs.getInt(KEY_SCORE_DRAWS, 0)
        set(value) = prefs.edit().putInt(KEY_SCORE_DRAWS, value).apply()

    var gameMode: GameMode
        get() {
            val name = prefs.getString(KEY_GAME_MODE, GameMode.VS_AI.name) ?: GameMode.VS_AI.name
            return try {
                GameMode.valueOf(name)
            } catch (_: Exception) {
                GameMode.VS_AI
            }
        }
        set(value) = prefs.edit().putString(KEY_GAME_MODE, value.name).apply()

    var difficulty: Difficulty
        get() {
            val name = prefs.getString(KEY_DIFFICULTY, Difficulty.MEDIUM.name) ?: Difficulty.MEDIUM.name
            return try {
                Difficulty.valueOf(name)
            } catch (_: Exception) {
                Difficulty.MEDIUM
            }
        }
        set(value) = prefs.edit().putString(KEY_DIFFICULTY, value.name).apply()

    var isSoundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_SOUND_ENABLED, value).apply()

    var isDarkTheme: Boolean
        get() = prefs.getBoolean(KEY_DARK_THEME, true)
        set(value) = prefs.edit().putBoolean(KEY_DARK_THEME, value).apply()

    fun resetScores() {
        prefs.edit()
            .putInt(KEY_SCORE_X, 0)
            .putInt(KEY_SCORE_O, 0)
            .putInt(KEY_SCORE_DRAWS, 0)
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "tictactoe_prefs"
        private const val KEY_SCORE_X = "score_x"
        private const val KEY_SCORE_O = "score_o"
        private const val KEY_SCORE_DRAWS = "score_draws"
        private const val KEY_GAME_MODE = "game_mode"
        private const val KEY_DIFFICULTY = "difficulty"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_DARK_THEME = "dark_theme"
    }
}
