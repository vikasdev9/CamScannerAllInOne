package com.example.camscannerallinone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.camscannerallinone.presentation.navigation.Screen
import com.example.camscannerallinone.presentation.screens.camera.CameraScreen
import com.example.camscannerallinone.presentation.screens.detail.DocumentDetailScreen
import com.example.camscannerallinone.presentation.screens.edit.EditScreen
import com.example.camscannerallinone.presentation.screens.folders.FolderDetailScreen
import com.example.camscannerallinone.presentation.screens.folders.FolderScreen
import com.example.camscannerallinone.presentation.screens.home.HomeScreen
import com.example.camscannerallinone.presentation.screens.pdf.PDFPreviewScreen
import com.example.camscannerallinone.presentation.theme.ScanFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScanFlowTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ScanFlowNavHost()
                }
            }
        }
    }
}

@Composable
fun ScanFlowNavHost() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCamera = { navController.navigate(Screen.Camera.route) },
                onNavigateToDetail = { id -> 
                    navController.navigate(Screen.DocumentDetail.createRoute(id)) 
                },
                onNavigateToFolders = { navController.navigate(Screen.Folders.route) }
            )
        }

        composable(Screen.Folders.route) {
            FolderScreen(
                onNavigateToFiles = { navController.navigate(Screen.Home.route) },
                onNavigateToFolderDetail = { id -> 
                    navController.navigate(Screen.FolderDetail.createRoute(id)) 
                }
            )
        }

        composable(
            route = Screen.FolderDetail.route,
            arguments = listOf(navArgument("folderId") { type = NavType.LongType })
        ) {
            FolderDetailScreen(
                onBack = { navController.popBackStack() },
                onNavigateToDocumentDetail = { id ->
                    navController.navigate(Screen.DocumentDetail.createRoute(id)) 
                }
            )
        }
        
        composable(
            route = Screen.DocumentDetail.route,
            arguments = listOf(navArgument("documentId") { type = NavType.LongType })
        ) {
            DocumentDetailScreen(
                onBack = { navController.popBackStack() },
                onNavigateToEdit = { id -> navController.navigate(Screen.EditPage.createRoute(id)) },
                onNavigateToPDF = { path -> navController.navigate(Screen.PDFPreview.createRoute(path)) }
            )
        }

        composable(
            route = Screen.EditPage.route,
            arguments = listOf(navArgument("pageId") { type = NavType.LongType })
        ) {
            EditScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.PDFPreview.route,
            arguments = listOf(navArgument("pdfPath") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedPath = backStackEntry.arguments?.getString("pdfPath") ?: ""
            val decodedPath = java.net.URLDecoder.decode(encodedPath, "UTF-8")
            PDFPreviewScreen(
                pdfPath = decodedPath,
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Camera.route) {
            CameraScreen(
                onScanComplete = { id -> 
                    navController.popBackStack() // Remove Camera from stack
                    navController.navigate(Screen.DocumentDetail.createRoute(id)) 
                },
                onClose = { navController.popBackStack() }
            )
        }
    }
}
