package com.fikri.mediapembelajaranpendidikanpansasila.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fikri.mediapembelajaranpendidikanpansasila.R

// Data Dummy untuk Materi Bab
data class MateriBab(val title: String, val description: String)

val daftarMateri = listOf(
    MateriBab("Bab 1: Simbol Pancasila", "Mengenal 5 simbol dalam burung Garuda."),
    MateriBab("Bab 2: Makna Sila Pertama", "Ketuhanan Yang Maha Esa dan contohnya."),
    MateriBab("Bab 3: Makna Sila Kedua", "Kemanusiaan yang adil dan beradab."),
    MateriBab("Bab 4: Hidup Rukun", "Penerapan Pancasila di rumah dan sekolah."),
    MateriBab("Bab 5: Gotong Royong", "Bekerja sama membersihkan lingkungan.")
)
@Composable
fun MateriScreen(
    onBackClick: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. BACKGROUND (Sama dengan Menu Utama)
        Image(
            painter = painterResource(id = R.drawable.bg_menu), // Pastikan bg_menu ada
            contentDescription = "Background Materi",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. HEADER (Tombol Kembali & Judul)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tombol Kembali (Pengganti Tombol Info)
            UtilityButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                color = Color(0xFFF44336), // Merah agar terlihat 'Back'
                onClick = onBackClick
            )

            UtilityButton(
                icon = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                color = Color(0xFFE040FB),
                onClick = { isMuted = !isMuted }
            )

            // Judul Halaman
            Text(
                text = "MATERI PANCASILA",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f) // Agar teks di tengah
            )

            // Spacer kosong biar judul benar-benar di tengah (menyeimbangkan tombol back)
            Spacer(modifier = Modifier.size(56.dp))
        }

        // 3. CONTENT (Kartu Bab Scrollable)
        // Kita beri padding atas agar tidak tertutup Header
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Jarak antar kartu
        ) {
            items(daftarMateri) { bab ->
                MateriCard(bab)
            }
        }
    }
}

@Composable
fun MateriCard(bab: MateriBab) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { /* TODO: Klik masuk ke detail bab */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nomor / Ikon Bab
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50)), // Hijau
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Ikon sementara
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Teks Judul & Deskripsi
            Column {
                Text(
                    text = bab.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = bab.description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}