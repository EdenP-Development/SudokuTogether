package com.playereden.sudokutogether

import android.animation.ValueAnimator
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartupBrandingUiTest {
    @get:Rule val compose = createEmptyComposeRule()

    @Test fun tapSkipsEntireSequence() {
        assumeTrue(ValueAnimator.areAnimatorsEnabled())
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            compose.mainClock.autoAdvance = false
            scenario.onActivity {
                it.setContent {
                    SudokuTogetherTheme {
                        Box {
                            Text("Menu fixture")
                            StartupBranding(true, R.drawable.gridmates_v1_runtime_transparent)
                        }
                    }
                }
            }
            compose.mainClock.advanceTimeByFrame()
            compose.onNodeWithTag("startup_branding").assertExists().performClick()
            compose.mainClock.advanceTimeByFrame()
            compose.onNodeWithTag("startup_branding").assertDoesNotExist()
            compose.onNodeWithText("Menu fixture").assertIsDisplayed()
        }
    }

    @Test fun recreationEntryDoesNotShowStartup() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity {
                it.setContent {
                    SudokuTogetherTheme {
                        Box {
                            Text("Menu fixture")
                            StartupBranding(false, R.drawable.gridmates_v1_runtime_transparent)
                        }
                    }
                }
            }
            compose.onNodeWithTag("startup_branding").assertDoesNotExist()
            compose.onNodeWithText("Menu fixture").assertIsDisplayed()
        }
    }
}
