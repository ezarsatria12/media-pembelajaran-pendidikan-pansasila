package com.fikri.mediapembelajaranpendidikanpansasila

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fikri.mediapembelajaranpendidikanpansasila.ui.screens.MainMenuScreen
import com.fikri.mediapembelajaranpendidikanpansasila.ui.screens.MateriScreen // Import MateriScreen
import com.fikri.mediapembelajaranpendidikanpansasila.ui.theme.MediaPembelajaranPendidikanPansasilaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()
        enableEdgeToEdge()

        setContent {
            MediaPembelajaranPendidikanPansasilaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // --- SETUP NAVIGASI DI SINI ---
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "menu") {

                        // 1. Rute Halaman Menu
                        composable("menu") {
                            MainMenuScreen(
                                onNavigateToMateri = { navController.navigate("materi") }, // Pindah ke Materi
                                onNavigateToQuiz = { /* Nanti dibuat */ },
                                onNavigateToUjian = { /* Nanti dibuat */ }
                            )
                        }

                        // 2. Rute Halaman Materi
                        composable("materi") {
                            MateriScreen(
                                onBackClick = { navController.popBackStack() } // Kembali ke Menu
                            )
                        }
                    }
                }
            }
        }
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}