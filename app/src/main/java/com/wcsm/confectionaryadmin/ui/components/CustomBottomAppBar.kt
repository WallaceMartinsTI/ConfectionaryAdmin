package com.wcsm.confectionaryadmin.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wcsm.confectionaryadmin.R
import com.wcsm.confectionaryadmin.data.model.navigation.BottomNavItem
import com.wcsm.confectionaryadmin.data.model.navigation.Screen
import com.wcsm.confectionaryadmin.ui.theme.ButtonBackgroundColor
import com.wcsm.confectionaryadmin.ui.theme.ConfectionaryAdminTheme
import com.wcsm.confectionaryadmin.ui.theme.PrimaryColor

@Composable
fun CustomBottomAppBar(
    navController: NavController
) {
    val bottomNavItems: List<BottomNavItem> = listOf(
        BottomNavItem(
            title = stringResource(id = R.string.bottom_nav_home),
            route = Screen.Main.route,
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            title = stringResource(id = R.string.bottom_nav_orders),
            route = Screen.Orders.route,
            icon = Icons.AutoMirrored.Filled.Notes
        ),
        BottomNavItem(
            title = stringResource(id = R.string.bottom_nav_customers),
            route = Screen.Customers.route,
            icon = Icons.Default.Person
        ),
        BottomNavItem(
            title = stringResource(id = R.string.bottom_nav_info),
            route = Screen.Info.route,
            icon = Icons.Default.Info
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = PrimaryColor,
    ) {
        bottomNavItems.forEach { bottomNavItem ->
            AddItem(
                navItem = bottomNavItem,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    navItem: BottomNavItem,
    currentDestination: NavDestination?,
    navController: NavController
) {
    NavigationBarItem(
        label = {
            Text(text = navItem.title)
        },
        icon = {
            Icon(
                imageVector = navItem.icon,
                contentDescription = null
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedTextColor = ButtonBackgroundColor,
            selectedIconColor = Color.White,
            indicatorColor = ButtonBackgroundColor,
            unselectedIconColor = Color.White,
            unselectedTextColor = Color.White
        ),
        selected = currentDestination?.hierarchy?.any {
            it.route == navItem.route
        } == true,
        onClick = {
            navController.navigate(navItem.route)
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun CustomBottomAppBarPreview() {
    ConfectionaryAdminTheme {
        val navController = rememberNavController()
        CustomBottomAppBar(navController = navController)
    }
}