package com.example.petsapplication.presentation.screens.home

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.petsapplication.*
import com.example.petsapplication.presentation.screens.channel.ChannelScreen
import com.example.petsapplication.presentation.screens.favourite_list.FavouriteScreen
import com.example.petsapplication.presentation.screens.home.components.Feed
import com.example.petsapplication.presentation.screens.profile.ProfileScreen

fun NavGraphBuilder.addHomeGraph(appState: PetAppState) {
    composable(
        route = HomeSections.FEED.route
    ) {
        Feed(navigate = appState::navigate)
    }

    composable(
        route = HomeSections.FAVOURITE.route
    ) {
        FavouriteScreen(
            navigate = appState::navigate,
            navigateAndPopUp = appState::navigateAndPopUp,
            navigateBack = appState::navigateBack,
        )
    }

    composable(
        route = HomeSections.CHANNEL.route
    ) {
        ChannelScreen(
            navigate = appState::navigate,
            navigateBack = appState::navigateBack,
            navigateAndPopUp = appState::navigateAndPopUp
        )
    }

    composable(
        route = HomeSections.PROFILE.route
    ) {
        ProfileScreen(
            navigateBack = appState::navigateBack,
            navigateAndPopUp = appState::navigateAndPopUp,
            navigate = appState::navigate
        )
    }
}

enum class HomeSections(
    val icon: Int,
    val route: String
) {
    FEED(R.drawable.round_home_black_24, "home/feed"),
    FAVOURITE(R.drawable.round_favorite_black_24, "home/favourite"),
    CHANNEL( R.drawable.round_textsms_black_24, "home/channel"),
    PROFILE(R.drawable.round_person_black_24, "home/profile")
}
