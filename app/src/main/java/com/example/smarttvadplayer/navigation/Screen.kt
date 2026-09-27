package com.example.smarttvadplayer.navigation

/**
 * All screen routes for the application navigation.
 */
sealed class Screen(val route: String) {
    object OrientationSelection : Screen("orientation_selection")
    object CompanyAd : Screen("company_ad")
    object LayoutSelection : Screen("layout_selection")
    object ZoneEditor : Screen("zone_editor")
    object AddContent : Screen("add_content/{zoneId}") {
        fun createRoute(zoneId: Int) = "add_content/$zoneId"
    }
    object TextEditor : Screen("text_editor/{zoneId}") {
        fun createRoute(zoneId: Int) = "text_editor/$zoneId"
    }
    object ScrollingTextEditor : Screen("scrolling_text_editor/{zoneId}") {
        fun createRoute(zoneId: Int) = "scrolling_text_editor/$zoneId"
    }
    object DurationPicker : Screen("duration_picker/{zoneId}") {
        fun createRoute(zoneId: Int) = "duration_picker/$zoneId"
    }
    object Preview : Screen("preview")
    object Playback : Screen("playback")
    object Settings : Screen("settings")
}
