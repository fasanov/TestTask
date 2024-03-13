package app.electronics.com.testtask.presentation.route

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.electronics.com.testtask.presentation.details.DetailsScreen
import app.electronics.com.testtask.presentation.home.HomeScreen
import app.electronics.com.testtask.presentation.home.HomeViewModel
import app.electronics.com.testtask.presentation.route.AppDestinationsArgs.PHOTO_ID_ARG
import app.electronics.com.testtask.presentation.route.ConstantAppScreenName.DETAILS_SCREEN
import timber.log.Timber

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val viewModel: HomeViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = AppScreen.HomeScreen.route,
    ) {

        composable(route = AppScreen.HomeScreen.route) {
            HomeScreen({ id ->
                Timber.d("navigateToDetails id=$id")
                navController.navigate("$DETAILS_SCREEN/$id")
            }, viewModel)
        }
        composable(
            route = AppScreen.DetailsScreen.route,
            arguments = listOf(navArgument(PHOTO_ID_ARG) { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt(PHOTO_ID_ARG) ?: -1
            DetailsScreen(id, viewModel) {
                navController.popBackStack()
            }
        }
    }
}
