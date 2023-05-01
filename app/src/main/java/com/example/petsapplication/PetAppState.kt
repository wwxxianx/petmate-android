package com.example.petsapplication

import android.content.res.Resources
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.petsapplication.common.snackbar.SnackbarManager
import com.example.petsapplication.common.snackbar.SnackbarMessage.Companion.toMessage
import com.example.petsapplication.presentation.screens.home.HomeSections
import com.example.petsapplication.presentation.screens.login.LoginScreen
import com.example.petsapplication.presentation.screens.login.SignUpScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

object ScreenRoutes {
    const val LOGIN_ROUTE = "login"
    const val SIGN_UP_ROUTE = "signUp"
    const val HOME_ROUTE = "home"
    const val PETS_ROUTE = "pets"

    const val PET_DETAIL_ROUTE = "petDetail"
    const val PET_ID = "petId"
    const val PET_DEFAULT_ID = "2"
    const val PET_ID_ARGS = "?$PET_ID={$PET_ID}"

    const val CHAT = "chat"
    const val CHAT_ID = "chatUserId"
    const val CHAT_DEFAULT_ID = "1"
    const val CHAT_ID_ARGS = "?$CHAT_ID={$CHAT_ID}"

    const val EDIT_PROFILE_ROUTE = "editProfile"
    const val PET_POSTING_ROUTE = "petPosting"
}

@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    snackbarManager: SnackbarManager = SnackbarManager,
    navController: NavHostController = rememberNavController(),
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(scaffoldState, snackbarManager, navController, resources, coroutineScope) {
    PetAppState(scaffoldState, snackbarManager, navController, resources, coroutineScope)
}

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@Stable
class PetAppState(
    val scaffoldState: ScaffoldState,
    val snackbarManager: SnackbarManager = SnackbarManager,
    val navController: NavHostController,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
                val text = snackbarMessage.toMessage(resources)
                scaffoldState.snackbarHostState.showSnackbar(text)
            }
        }
    }

    val bottomBarTabs = HomeSections.values()
    private val bottomBarRoutes = bottomBarTabs.map { it.route }

    val shouldShowBottomBar: Boolean
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes

    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }
}