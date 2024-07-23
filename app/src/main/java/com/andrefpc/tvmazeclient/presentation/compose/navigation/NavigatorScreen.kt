package com.andrefpc.tvmazeclient.presentation.compose.navigation

import com.andrefpc.tvmazeclient.presentation.model.PersonViewState
import com.andrefpc.tvmazeclient.presentation.model.ShowViewState

sealed class NavigatorScreen {
    data object Main : NavigatorScreen()
    data object Shows : NavigatorScreen()
    data object Favorites : NavigatorScreen()
    data object People : NavigatorScreen()
    data class PersonDetails(val person: PersonViewState) : NavigatorScreen()
    data class ShowDetails(val show: ShowViewState) : NavigatorScreen()

}