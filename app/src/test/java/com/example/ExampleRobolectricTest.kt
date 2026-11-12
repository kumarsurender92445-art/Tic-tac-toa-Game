package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.GamePreferencesRepository
import com.example.model.Difficulty
import com.example.model.GameMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app name string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Tic Tac Toe", appName)
  }

  @Test
  fun `verify preferences persistence and reset`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = GamePreferencesRepository(context)

    repo.scoreX = 5
    repo.scoreO = 3
    repo.scoreDraws = 2
    repo.gameMode = GameMode.TWO_PLAYER
    repo.difficulty = Difficulty.HARD

    assertEquals(5, repo.scoreX)
    assertEquals(3, repo.scoreO)
    assertEquals(2, repo.scoreDraws)
    assertEquals(GameMode.TWO_PLAYER, repo.gameMode)
    assertEquals(Difficulty.HARD, repo.difficulty)

    repo.resetScores()
    assertEquals(0, repo.scoreX)
    assertEquals(0, repo.scoreO)
    assertEquals(0, repo.scoreDraws)
  }

  @Test
  fun `verify ViewModelProvider can instantiate GameViewModel`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(app)
    val vm = factory.create(com.example.viewmodel.GameViewModel::class.java)
    org.junit.Assert.assertNotNull(vm)
  }
}
