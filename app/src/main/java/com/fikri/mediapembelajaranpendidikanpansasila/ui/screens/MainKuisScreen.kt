package com.fikri.mediapembelajaranpendidikanpansasila.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fikri.mediapembelajaranpendidikanpansasila.R

@Composable
fun MainKuisScreen(
    kuisId: String, // Nanti dipakai untuk ambil data soal dari Room DB
    onBackClick: () -> Unit
) {
    // Dummy Data Sementara untuk Tampilan Visual
    val daftarTarget = listOf("Sila 1", "Sila 2", "Sila 3")
    val daftarPilihan = listOf("Bintang", "Rantai", "Pohon Beringin")

    Box(modifier = Modifier.fillMaxSize()) {
        // --- 1. BACKGROUND ---
        Image(
            painter = painterResource(id = R.drawable.bg_menu),
            contentDescription = "Background Game",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // --- 2. HEADER ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                FloatingActionButton(
                    onClick = onBackClick,
                    containerColor = Color(0xFFF44336),
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(50),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "Cocokkan Jawaban!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- 3. AREA TARGET (TEMPAT MENARUH JAWABAN) ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("Pindahkan gambar ke kotak yang benar", fontWeight = FontWeight.Bold, color = Color.Gray)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        daftarTarget.forEach { targetText ->
                            // Kotak putus-putus sebagai Target Drop
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .border(
                                        width = 3.dp,
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .background(Color.Transparent, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(targetText, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 4. AREA SUMBER (PILIHAN YANG BISA DIGESER) ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)), // Kuning lembut
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    daftarPilihan.forEach { pilihanText ->
                        // Kotak Solid sebagai Item yang bisa di-drag (Nanti)
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFF9800)), // Orange
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = pilihanText,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}