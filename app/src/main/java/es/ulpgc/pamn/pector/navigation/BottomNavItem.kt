package es.ulpgc.pamn.pector.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

//initializing the data class with default parameters
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem(AppScreens.MainMenuScreen.route, Icons.Default.Home, "Home")
    object Search : BottomNavItem(AppScreens.SearchScreen.route, Icons.Default.Search, "Search")
    object Profile : BottomNavItem(AppScreens.ProfileScreen.route, Icons.Default.Person, "Profile")
}

fun getBottomNavItems(): List<BottomNavItem>{
    // we add each BottomNavItem to the list in a single line
    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Profile
    )
    return bottomNavItems
}