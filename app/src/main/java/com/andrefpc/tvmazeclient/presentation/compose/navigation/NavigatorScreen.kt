package com.andrefpc.tvmazeclient.presentation.compose.navigation

import com.andrefpc.tvmazeclient.domain.model.Person
import com.andrefpc.tvmazeclient.domain.model.Show

sealed class NavigatorScreen {
    data object Main : NavigatorScreen()
    data object Shows : NavigatorScreen()
    data object Favorites : NavigatorScreen()
    data object People : NavigatorScreen()
    data class PersonDetails(val person: Person) : NavigatorScreen()
    data class ShowDetails(val show: Show) : NavigatorScreen()

}