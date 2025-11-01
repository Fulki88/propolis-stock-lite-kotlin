package id.firobusiness.propolisstocklite.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import feature.inventory.InventoryRoute
import feature.sales.SalesRoute

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "inventory") {
        composable("inventory") { InventoryRoute(onNewSale = { navController.navigate("sales") }) }
        composable("sales") { SalesRoute() }
    }
}
