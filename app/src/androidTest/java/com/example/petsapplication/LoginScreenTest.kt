package com.example.petsapplication

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.example.petsapplication.presentation.screens.login.LoginScreen
import com.example.petsapplication.presentation.screens.login.SignUpScreen
import com.example.petsapplication.presentation.ui.theme.PetsApplicationTheme
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginScreenTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    @Test
    fun navigate_to_SignUpScreen_and_clear_all_textfields() {
        composeTestRule.setContent {
            PetsApplicationTheme {
                val appState = rememberAppState()
                NavHost(
                    navController = appState.navController,
                    startDestination = ScreenRoutes.LOGIN_ROUTE)
                {
                    composable(route = ScreenRoutes.LOGIN_ROUTE) {
                        LoginScreen(
                            navigate = appState::navigate,
                            navigateAndPopUp = appState::navigateAndPopUp
                        )
                    }
                    composable(route = ScreenRoutes.SIGN_UP_ROUTE) {
                        SignUpScreen(
                            navigateBack = appState::navigateBack,
                            navigateAndPopUp = appState::navigateAndPopUp
                        )
                    }
                }
            }
        }
        composeTestRule.onNodeWithText("Enter your email").performTextInput("testing@gmail.com")
        composeTestRule.onNodeWithText("Enter your password").performTextInput("testing")
        composeTestRule.onNodeWithText("Re-enter your password").performTextInput("testing")
        composeTestRule.onNodeWithText("Sign up").performClick()
        val expectedValueInTextField = ""
        composeTestRule.onNodeWithText("Enter your email").assertTextEquals(expectedValueInTextField)
        composeTestRule.onNodeWithText("Enter your password").assertTextEquals(expectedValueInTextField)
        composeTestRule.onNodeWithText("Re-enter your password").assertTextEquals(expectedValueInTextField)

    }
}