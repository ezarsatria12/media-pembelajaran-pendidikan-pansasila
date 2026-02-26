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
import androidx.navigation.navArgument
import com.fikri.mediapembelajaranpendidikanpansasila.ui.screens.DetailMateriScreen
import com.fikri.mediapembelajaranpendidikanpansasila.ui.screens.MainMenuScreen
import com.fikri.mediapembelajaranpendidikanpansasila.ui.screens.MateriScreen
import com.fikri.mediapembelajaranpendidikanpansasila.ui.theme.MediaPembelajaranPendidikanPansasilaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI() // Agar Fullscreen
        enableEdgeToEdge()

        setContent {
            MediaPembelajaranPendidikanPansasilaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "menu") {

                        // 1. MENU UTAMA
                        composable("menu") {
                            MainMenuScreen(
                                onNavigateToMateri = { navController.navigate("materi") },
                                onNavigateToQuiz = { /* Nanti: navController.navigate("kuis") */ },
                                onNavigateToUjian = { /* Nanti: navController.navigate("ujian") */ }
                            )
                        }

                        // 2. DAFTAR MATERI (GRID)
                        composable("materi") {
                            MateriScreen(
                                onBackClick = { navController.popBackStack() },
                                onMateriClick = { babId ->
                                    // Saat kartu diklik, pindah ke detail bawa ID-nya (misal: "bab1")
                                    navController.navigate("detail_materi/$babId")
                                }
                            )
                        }

                        // 3. DETAIL MATERI (PDF VIEWER)
                        composable(
                            route = "detail_materi/{babId}",
                            // Kita terima argumen "babId" dari rute sebelumnya
                        ) { backStackEntry ->
                            val babId = backStackEntry.arguments?.getString("babId") ?: "bab1"

                            // LOGIKA PENENTUAN FILE PDF
                            // Jika babId="bab1", maka buka file "materi_bab1.pdf"
                            // Pastikan kamu punya file 'materi_bab1.pdf' di folder assets!
                            val namaFilePdf = "materi_$babId.pdf"

                            DetailMateriScreen(
                                namaFilePdf = namaFilePdf,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }

    // Fungsi untuk menyembunyikan Status Bar (Fullscreen)
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