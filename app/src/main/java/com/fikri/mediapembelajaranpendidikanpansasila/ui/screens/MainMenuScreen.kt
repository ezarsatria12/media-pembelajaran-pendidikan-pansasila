package com.fikri.mediapembelajaranpendidikanpansasila.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Pastikan import R sesuai dengan package kamu
import com.fikri.mediapembelajaranpendidikanpansasila.R

@Composable
fun MainMenuScreen(
    onNavigateToMateri: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    onNavigateToUjian: () -> Unit
) {
    var showInfoDialog by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // --- BAGIAN BACKGROUND DIUBAH ---
        Image(
            // Pastikan nama file gambarmu adalah 'bg_menu' di folder drawable
            painter = painterResource(id = R.drawable.bg_menu),
            contentDescription = "Background Menu",
            contentScale = ContentScale.Crop, // Agar gambar full memenuhi layar
            modifier = Modifier.fillMaxSize()
        )
        // --------------------------------

        // --- TOMBOL PENGATURAN (KIRI ATAS) ---
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            UtilityButton(
                icon = Icons.Default.Info,
                color = Color(0xFFFFF176),
                onClick = { showInfoDialog = true }
            )
            UtilityButton(
                // Menggunakan icon Email sementara jika belum tambah library extended
                icon = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                color = Color(0xFFE040FB),
                onClick = { isMuted = !isMuted }
            )
        }

        // --- JUDUL ---
        Text(
            text = "PANCASILA CERIA",
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp)
        )

        // --- MENU UTAMA ---
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            MenuButton("MATERI", Color(0xFF4CAF50), onNavigateToMateri)
            MenuButton("KUIS", Color(0xFF2196F3), onNavigateToQuiz)
            MenuButton("UJIAN", Color(0xFFF44336), onNavigateToUjian)
        }

        // --- DIALOG INFO ---
        if (showInfoDialog) {
            AlertDialog(
                onDismissRequest = { showInfoDialog = false },
                title = { Text("Tentang Aplikasi") },
                text = { Text("Aplikasi Pembelajaran Pancasila\nKelas 1 SD\nVersi 1.0.0") },
                confirmButton = {
                    TextButton(onClick = { showInfoDialog = false }) { Text("Tutup") }
                }
            )
        }
    }
}

// ... (Kode UtilityButton dan MenuButton tetap sama seperti sebelumnya)
@Composable
fun UtilityButton(
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(Color.White)
            .padding(4.dp)
            .clip(CircleShape)
            .background(color)
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun MenuButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(180.dp)
            .height(60.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
    ) {
        Text(text = text, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}