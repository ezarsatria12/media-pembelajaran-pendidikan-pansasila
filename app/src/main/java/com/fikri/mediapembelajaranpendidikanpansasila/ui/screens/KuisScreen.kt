package com.fikri.mediapembelajaranpendidikanpansasila.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fikri.mediapembelajaranpendidikanpansasila.R

// --- 1. DATA KUIS (Sama seperti MateriBab) ---
data class KuisBab(val id: String, val title: String, val description: String)

val daftarKuis = listOf(
    KuisBab("kuis_bab1", "Latihan Bab 1", "Simbol Pancasila"),
    KuisBab("kuis_bab2", "Latihan Bab 2", "Makna Sila Pertama"),
    KuisBab("kuis_bab3", "Latihan Bab 3", "Makna Sila Kedua"),
    KuisBab("kuis_bab4", "Latihan Bab 4", "Hidup Rukun"),
    KuisBab("kuis_bab5", "Latihan Bab 5", "Gotong Royong")
)

// --- 2. LAYAR UTAMA (GRID) ---
@Composable
fun KuisScreen(
    onBackClick: () -> Unit,
    onKuisClick: (String) -> Unit // Callback untuk navigasi ke permainan kuis
) {
    var isMuted by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // BACKGROUND
        Image(
            painter = painterResource(id = R.drawable.bg_menu),
            contentDescription = "Background Kuis",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // COLUMN (Header + Grid)
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // A. HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tombol Kembali
                UtilityButton(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    color = Color(0xFFF44336),
                    onClick = onBackClick
                )

                // Tombol Suara
                UtilityButton(
                    icon = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                    color = Color(0xFFE040FB),
                    onClick = { isMuted = !isMuted }
                )

                Text(
                    text = "LATIHAN KUIS",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.size(56.dp))
            }

            // B. GRID CONTENT
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(daftarKuis) { bab ->
                    KuisCardGrid(bab = bab, onClick = { onKuisClick(bab.id) })
                }
            }
        }
    }
}

// --- 3. KOMPONEN KARTU GRID ---
@Composable
fun KuisCardGrid(
    bab: KuisBab,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Bagian Gambar
            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxWidth()
                    .background(Color(0xFF2196F3)), // Biru ceria untuk Kuis
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
            }

            // Bagian Teks
            Column(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = bab.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1) // Biru tua
                )
                Text(
                    text = bab.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}