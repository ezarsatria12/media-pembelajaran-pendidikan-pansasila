package com.fikri.mediapembelajaranpendidikanpansasila.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.fikri.mediapembelajaranpendidikanpansasila.R
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.util.FitPolicy

@Composable
fun DetailMateriScreen(
    namaFilePdf: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    var totalHalaman by remember { mutableIntStateOf(0) }
    var halamanSaatIni by remember { mutableIntStateOf(0) }
    var pdfView: PDFView? by remember { mutableStateOf(null) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // --- 1. BACKGROUND PERMAINAN ---
        Image(
            painter = painterResource(id = R.drawable.bg_menu),
            contentDescription = "Background Detail",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // --- 2. HEADER (Tombol Kembali & Play Suara) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tombol Back (Kiri Atas)
            FloatingActionButton(
                onClick = onBackClick,
                containerColor = Color(0xFFF44336), // Merah
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
            }

            // Tombol Play Audio (Kanan Atas)
            FloatingActionButton(
                onClick = { /* TODO: Mainkan Suara Halaman Ini */ },
                containerColor = Color(0xFFE040FB), // Ungu
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.PlayArrow, "Mainkan Suara", modifier = Modifier.size(32.dp))
            }
        }

        // --- 3. BINGKAI PDF 16:9 (DI TENGAH) ---
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight(0.85f)
                .aspectRatio(16f / 9f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                factory = { ctx ->
                    PDFView(ctx, null).apply {
                        fromAsset(namaFilePdf)
                            .enableSwipe(false) // Matikan geser jari (Cukup pakai tombol)
                            .swipeHorizontal(true) // Arah horizontal
                            .pageFitPolicy(com.github.barteksc.pdfviewer.util.FitPolicy.BOTH) // Pas dengan layar

                            .onLoad(object : com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener {
                                override fun loadComplete(pages: Int) {
                                    totalHalaman = pages
                                }
                            })
                            .onPageChange(object : com.github.barteksc.pdfviewer.listener.OnPageChangeListener {
                                override fun onPageChanged(page: Int, pageCount: Int) {
                                    halamanSaatIni = page
                                }
                            })
                            .load()

                        pdfView = this
                    }
                }
            )
        }

        // --- 4. TOMBOL PREV (KIRI TENGAH) ---
        if (halamanSaatIni > 0) {
            FloatingActionButton(
                onClick = { pdfView?.jumpTo(halamanSaatIni - 1) },
                containerColor = Color(0xFFFF9800), // Orange
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 24.dp)
                    .size(64.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Mundur", modifier = Modifier.size(36.dp))
            }
        }

        // --- 5. TOMBOL NEXT / SELESAI (KANAN TENGAH) ---
        val isLastPage = halamanSaatIni >= totalHalaman - 1 && totalHalaman > 0
        FloatingActionButton(
            onClick = {
                if (!isLastPage) {
                    pdfView?.jumpTo(halamanSaatIni + 1)
                } else {
                    onBackClick() // Kembali ke menu materi jika sudah selesai
                }
            },
            containerColor = if (!isLastPage) Color(0xFF2196F3) else Color(0xFF4CAF50), // Biru (Next) / Hijau (Selesai)
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 24.dp)
                .size(64.dp)
        ) {
            Icon(
                imageVector = if (!isLastPage) Icons.AutoMirrored.Filled.ArrowForward else Icons.Default.Check,
                contentDescription = "Lanjut",
                modifier = Modifier.size(36.dp)
            )
        }

        // --- 6. INDIKATOR HALAMAN (BAWAH TENGAH) ---
        if (totalHalaman > 0) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(percent = 50),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Hal ${halamanSaatIni + 1} / $totalHalaman",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
        }
    }
}