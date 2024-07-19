package com.andrefpc.tvmazeclient.presentation.xml_based.navigation

import android.app.Activity
import android.content.Intent
import com.andrefpc.tvmazeclient.domain.model.Person
import com.andrefpc.tvmazeclient.domain.model.Show
import com.andrefpc.tvmazeclient.presentation.compose.navigation.AppNavigation
import com.andrefpc.tvmazeclient.presentation.compose.navigation.NavigatorScreen
import com.andrefpc.tvmazeclient.presentation.xml_based.screen.favorites.FavoritesActivity
import com.andrefpc.tvmazeclient.presentation.xml_based.screen.main.MainActivity
import com.andrefpc.tvmazeclient.presentation.xml_based.screen.people.PeopleActivity
import com.andrefpc.tvmazeclient.presentation.xml_based.screen.person_details.PersonDetailsActivity
import com.andrefpc.tvmazeclient.presentation.xml_based.screen.show_details.ShowDetailsActivity
import com.andrefpc.tvmazeclient.presentation.xml_based.screen.shows.ShowsActivity

class AppNavigationXmlImpl : AppNavigation {
    override fun navigateTo(activity: Activity, navigatorScreen: NavigatorScreen) {
        when (navigatorScreen) {
            NavigatorScreen.Favorites -> openFavorites(activity)
            NavigatorScreen.Main -> openMain(activity)
            NavigatorScreen.People -> openPeople(activity)
            is NavigatorScreen.PersonDetails -> openPersonDetails(activity, navigatorScreen.person)
            is NavigatorScreen.ShowDetails -> openShowDetails(activity, navigatorScreen.show)
            NavigatorScreen.Shows -> openShows(activity)
        }
    }

    private fun openFavorites(activity: Activity) {
        activity.startActivity(Intent(activity, FavoritesActivity::class.java))
    }

    private fun openMain(activity: Activity) {
        activity.startActivity(Intent(activity, MainActivity::class.java))
    }

    private fun openPeople(activity: Activity) {
        activity.startActivity(Intent(activity, PeopleActivity::class.java))
    }

    private fun openPersonDetails(activity: Activity, person: Person) {
        val intent = Intent(activity, PersonDetailsActivity::class.java)
        intent.putExtra("person", person)
        activity.startActivity(intent)
    }

    private fun openShowDetails(activity: Activity, show: Show) {
        val intent = Intent(activity, ShowDetailsActivity::class.java)
        intent.putExtra("show", show)
        activity.startActivity(intent)
    }

    private fun openShows(activity: Activity) {
        activity.startActivity(Intent(activity, ShowsActivity::class.java))
    }
}