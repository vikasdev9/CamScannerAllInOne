package com.example.camscannerallinone.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Camera : Screen("camera")
    object DocumentDetail : Screen("document/{documentId}") {
        fun createRoute(documentId: Long) = "document/$documentId"
    }
    object EditPage : Screen("edit/{pageId}") {
        fun createRoute(pageId: Long) = "edit/$pageId"
    }
    object PDFPreview : Screen("pdf_preview/{pdfPath}") {
        fun createRoute(pdfPath: String) = "pdf_preview/$pdfPath"
    }
    object Settings : Screen("settings")
    object Security : Screen("security")
}
