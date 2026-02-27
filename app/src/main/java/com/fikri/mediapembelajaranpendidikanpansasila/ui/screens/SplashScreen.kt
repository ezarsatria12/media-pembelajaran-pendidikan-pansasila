package com.fikri.mediapembelajaranpendidikanpansasila.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fikri.mediapembelajaranpendidikanpansasila.R
import com.fikri.mediapembelajaranpendidikanpansasila.data.local.AppDatabase
import com.fikri.mediapembelajaranpendidikanpansasila.data.local.MateriEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun SplashScreen(
    onNavigateToMenu: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        withContext(Dispatchers.IO) {
            try {
            val db = AppDatabase.getDatabase(context)
            val dao = db.materiDao()

            val cekData = dao.getMateriById("bab1")

            // Jika kosong, masukkan Data Default beserta Timestamp-nya
            if (cekData == null) {
                // Ambil waktu saat ini dalam format Unix Timestamp (Long)
                val currentTime = System.currentTimeMillis()

                val dataAwal = listOf(
                    MateriEntity(
                        id = "bab1",
                        judul = "Bab 1",
                        deskripsi = "Simbol Pancasila",
                        namaFilePdf = "materi_bab1.pdf",
                        isDownloaded = true,
                        createdAt = currentTime,
                        updatedAt = currentTime
                    ),
                    MateriEntity(
                        id = "bab2",
                        judul = "Bab 2",
                        deskripsi = "Makna Sila Pertama",
                        namaFilePdf = "materi_bab2.pdf",
                        isDownloaded = true,
                        createdAt = currentTime,
                        updatedAt = currentTime
                    ),
                    MateriEntity(
                        id = "bab3",
                        judul = "Bab 3",
                        deskripsi = "Makna Sila Kedua",
                        namaFilePdf = "materi_bab3.pdf",
                        isDownloaded = true,
                        createdAt = currentTime,
                        updatedAt = currentTime
                    ),
                    MateriEntity(
                        id = "bab4",
                        judul = "Bab 4",
                        deskripsi = "Hidup Rukun",
                        namaFilePdf = "materi_bab4.pdf",
                        isDownloaded = true,
                        createdAt = currentTime,
                        updatedAt = currentTime
                    ),
                    MateriEntity(
                        id = "bab5",
                        judul = "Bab 5",
                        deskripsi = "Gotong Royong",
                        namaFilePdf = "materi_bab5.pdf",
                        isDownloaded = true,
                        createdAt = currentTime,
                        updatedAt = currentTime
                    )
                )

                dao.insertAll(dataAwal)
            }
        } catch (e: Exception) {
        // Jika database error, tangkap errornya dan jangan bunuh aplikasinya!
        e.printStackTrace()
    }
        }

        delay(1500)
        onNavigateToMenu()
    }

    // --- TAMPILAN UI LOADING ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4CAF50)), // Hijau Segar
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Ganti logo aslimu nanti
                contentDescription = "Logo Aplikasi",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Pendidikan Pancasila",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Menyiapkan materi belajar...",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}