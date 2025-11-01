package id.firobusiness.propolisstocklite

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import feature.inventory.InventoryRoute
import feature.inventory.CreateStockRoute
import feature.sales.SalesRoute

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { 
            HomeRoute(
                onInventoryClick = { navController.navigate("inventory") },
                onCreateStockClick = { navController.navigate("create_stock") },
                onBuyerPurchaseClick = { navController.navigate("sales") }
            )
        }
        composable("inventory") { 
            InventoryRoute(
                onNewSale = { navController.navigate("sales") }
            ) 
        }
        composable("create_stock") { 
            CreateStockRoute(
                onBack = { navController.popBackStack() }
            ) 
        }
        composable("sales") { 
            SalesRoute() 
        }
    }
}