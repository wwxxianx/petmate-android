package com.example.petsapplication.presentation.screens.home.components

import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.petsapplication.presentation.screens.home.HomeSections

@Composable
fun PetAppBottomBar(
    tabs: Array<HomeSections>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
) {
    NavigationBar(
        containerColor = Color.White
    ) {
        val routes = remember { tabs.map { it.route } }
        val currentSection = tabs.first { it.route == currentRoute }

        tabs.forEach { section ->
            BottomNavigationItem(
                selected = section == currentSection,
                icon = { Icon(painter = painterResource(id = section.icon), contentDescription = section.route) },
                onClick = { navigateToRoute(section.route) },
                unselectedContentColor = Color.Gray
            )
        }
    }
}