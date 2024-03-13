package app.electronics.com.testtask.presentation.route

import app.electronics.com.testtask.presentation.route.AppDestinationsArgs.PHOTO_ID_ARG
import app.electronics.com.testtask.presentation.route.ConstantAppScreenName.DETAILS_SCREEN
import app.electronics.com.testtask.presentation.route.ConstantAppScreenName.HOME_SCREEN

sealed class AppScreen(val route: String) {
    data object HomeScreen : AppScreen(HOME_SCREEN)
    data object DetailsScreen : AppScreen("$DETAILS_SCREEN/{$PHOTO_ID_ARG}")
}

object ConstantAppScreenName {
    const val HOME_SCREEN = "home_screen"
    const val DETAILS_SCREEN = "details_screen"
}

object AppDestinationsArgs {
    const val PHOTO_ID_ARG = "photo_id"
}
