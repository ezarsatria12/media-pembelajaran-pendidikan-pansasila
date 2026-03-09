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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fikri.mediapembelajaranpendidikanpansasila.R
import com.fikri.mediapembelajaranpendidikanpansasila.data.local.AppDatabase
import com.fikri.mediapembelajaranpendidikanpansasila.data.local.QuizChapterEntity

// DATA HARDCODE SUDAH DIHAPUS, SEKARANG MURNI DARI DATABASE

@Composable
fun KuisScreen(
    onBackClick: () -> Unit,
    onKuisClick: (String) -> Unit
) {
    val context = LocalContext.current
    var isMuted by remember { mutableStateOf(false) }

    // --- 1. AMBIL MESIN DATABASE ---
    val db = remember { AppDatabase.getDatabase(context) }
    val kuisDao = remember { db.kuisDao() }

    // --- 2. SEDOT DATA SECARA REAL-TIME ---
    // collectAsState() akan mengubah Flow dari Room menjadi State yang dikenali oleh UI Compose
    val daftarKuis by kuisDao.getAllActiveQuizChapters().collectAsState(initial = emptyList())

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_kuis),
            contentDescription = "Background Kuis",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

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
                UtilityButton(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    color = Color(0xFFF44336),
                    onClick = onBackClick
                )

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
            if (daftarKuis.isEmpty()) {
                // Tampilkan loading berputar jika data sedang dimuat dari memori internal
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    // Masukkan daftarKuis yang diambil dari Database
                    items(daftarKuis) { bab ->
                        KuisCardGrid(bab = bab, onClick = { onKuisClick(bab.id) })
                    }
                }
            }
        }
    }
}

// --- 3. KOMPONEN KARTU ---
@Composable
fun KuisCardGrid(
    bab: QuizChapterEntity, // Sekarang menerima entitas database langsung
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
            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxWidth()
                    .background(Color(0xFF2196F3)),
                contentAlignment = Alignment.Center
            ) {
                // Nanti ini bisa dikembangkan memanggil gambar dari bab.iconAssetName
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = bab.title, // Ambil judul dari kolom "title" di Room DB
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Latihan Interaktif", // Fallback deskripsi statis karena tabel kuis kita tidak ada kolom deskripsi
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