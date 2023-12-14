package es.ulpgc.pamn.pector.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import es.ulpgc.pamn.pector.ui.theme.DarkViolet
import es.ulpgc.pamn.pector.ui.theme.LightViolet

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        // el color de fondo será violeta oscuro
        containerColor = DarkViolet,
        contentColor = LightViolet,
        modifier = Modifier.height(65.dp)
    ){
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val bottomNavItems = getBottomNavItems()
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp)) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = LightViolet
                )
            )
        }
    }
}