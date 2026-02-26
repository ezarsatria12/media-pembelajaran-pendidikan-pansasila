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

// --- 1. DATA MATERI (Update: Tambah ID untuk nama file PDF) ---
data class MateriBab(val id: String, val title: String, val description: String)

val daftarMateri = listOf(
    // ID "bab1" berarti nanti akan membuka file "bab1.pdf" (atau "materi_bab1.pdf")
    MateriBab("bab1", "Bab 1", "Simbol Pancasila"),
    MateriBab("bab2", "Bab 2", "Makna Sila Pertama"),
    MateriBab("bab3", "Bab 3", "Makna Sila Kedua"),
    MateriBab("bab4", "Bab 4", "Hidup Rukun"),
    MateriBab("bab5", "Bab 5", "Gotong Royong")
)

// --- 2. LAYAR UTAMA (GRID) ---
@Composable
fun MateriScreen(
    onBackClick: () -> Unit,
    onMateriClick: (String) -> Unit // Callback baru untuk navigasi ke Detail
) {
    var isMuted by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // BACKGROUND
        Image(
            painter = painterResource(id = R.drawable.bg_menu),
            contentDescription = "Background Materi",
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
                    text = "MATERI PANCASILA",
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
                items(daftarMateri) { bab ->
                    // Kirim aksi klik ke atas (MainActivity)
                    MateriCardGrid(bab = bab, onClick = { onMateriClick(bab.id) })
                }
            }
        }
    }
}

// --- 3. KOMPONEN KARTU GRID ---
@Composable
fun MateriCardGrid(
    bab: MateriBab,
    onClick: () -> Unit // Tambah parameter onClick
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable { onClick() } // Pasang aksi klik di sini
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Bagian Gambar
            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxWidth()
                    .background(Color(0xFF8BC34A)),
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
                    color = Color(0xFF2E7D32)
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