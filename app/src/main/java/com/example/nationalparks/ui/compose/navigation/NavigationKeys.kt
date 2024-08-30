package com.example.nationalparks.ui.compose.navigation

import com.example.nationalparks.ui.compose.navigation.NavigationKeys.Arg.TOUR_ID

object NavigationKeys {

    object Arg {
        const val TOUR_ID = "tourId"
    }

    object Route {
        const val TOUR_LIST = "tour_list"
        const val TOUR_DETAIL = "$TOUR_LIST/{$TOUR_ID}"
    }
}