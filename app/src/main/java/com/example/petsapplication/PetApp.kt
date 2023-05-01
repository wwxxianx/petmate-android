package com.example.petsapplication

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.petsapplication.presentation.screens.chat.ChatScreen
import com.example.petsapplication.presentation.screens.edit_profile.EditProfileScreen
import com.example.petsapplication.presentation.screens.home.HomeSections
import com.example.petsapplication.presentation.screens.home.addHomeGraph
import com.example.petsapplication.presentation.screens.home.components.PetAppBottomBar
import com.example.petsapplication.presentation.screens.login.LoginScreen
import com.example.petsapplication.presentation.screens.login.SignUpScreen
import com.example.petsapplication.presentation.screens.pet_detail.PetDetailScreen
import com.example.petsapplication.presentation.screens.pet_list.PetsScreen
import com.example.petsapplication.presentation.screens.pet_posting.PetPostingScreen
import com.example.petsapplication.presentation.ui.theme.PetsApplicationTheme

@Composable
fun PetApp() {
    PetsApplicationTheme {
        val appState = rememberAppState()
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = it,
                    modifier = Modifier,
                    snackbar = { snackbarData ->
                        Snackbar(
                            snackbarData = snackbarData,
                            contentColor = Color.White
                        )
                    }
                )
            },
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    PetAppBottomBar(
                        tabs = appState.bottomBarTabs,
                        currentRoute = appState.currentRoute!!,
                        navigateToRoute = appState::navigate
                    )
                }
            },
            scaffoldState = appState.scaffoldState
        ) { innerPadding ->
            NavHost(
                navController = appState.navController,
                startDestination = ScreenRoutes.LOGIN_ROUTE,
                modifier = Modifier.padding(innerPadding)
            ) {
                petAppNavGraph(appState)
            }
        }
    }
}

private fun NavGraphBuilder.petAppNavGraph(appState: PetAppState) {
    navigation(
        route = ScreenRoutes.HOME_ROUTE,
        startDestination = HomeSections.FEED.route
    ) {
        addHomeGraph(appState)
    }

    composable(
        route = ScreenRoutes.LOGIN_ROUTE
    ) {
        LoginScreen(
            navigate = { route -> appState.navigate(route) },
            navigateAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }

    composable(
        route = ScreenRoutes.SIGN_UP_ROUTE
    ) {
        SignUpScreen(
            navigateBack = { appState.navigateBack() },
            navigateAndPopUp = { route, popUp ->
                appState.navigateAndPopUp(route, popUp)
            }
        )
    }

    composable(
        route = ScreenRoutes.PETS_ROUTE
    ) {
        PetsScreen(
            navigateBack = appState::navigateBack,
            navigateToRoute = appState::navigate
        )
    }

    composable(
        route = ScreenRoutes.EDIT_PROFILE_ROUTE
    ) {
        EditProfileScreen(
            navigateBack = appState::navigateBack,
            navigateAndPopUp = appState::navigateAndPopUp
        )
    }

    composable(
        route = ScreenRoutes.PET_POSTING_ROUTE
    ) {
        PetPostingScreen(
            navigateBack = appState::navigateBack,
            navigateAndPopUp = appState::navigateAndPopUp
        )
    }

    composable(
        route = "${ScreenRoutes.PET_DETAIL_ROUTE}${ScreenRoutes.PET_ID_ARGS}",
        arguments = listOf(navArgument(ScreenRoutes.PET_ID) { defaultValue = ScreenRoutes.PET_DEFAULT_ID })
    ) {
        PetDetailScreen(
            petId = it.arguments?.getString(ScreenRoutes.PET_ID) ?: ScreenRoutes.PET_DEFAULT_ID,
            navigateBack = appState::navigateBack,
            navigate = appState::navigate
        )
    }

    composable(
        route = "${ScreenRoutes.CHAT}${ScreenRoutes.CHAT_ID_ARGS}",
        arguments = listOf(navArgument(ScreenRoutes.CHAT_ID) {type = NavType.StringType})
    ) {
        ChatScreen(
            chatUserId = it.arguments?.getString(ScreenRoutes.CHAT_ID) ?: ScreenRoutes.CHAT_DEFAULT_ID,
            navigateBack = appState::navigateBack,
        )
    }
}

