package com.example.si_akademik_its.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Biodata : Screen("biodata")
    object Jadwal : Screen("jadwal")
}
