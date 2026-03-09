package com.fikri.mediapembelajaranpendidikanpansasila.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fikri.mediapembelajaranpendidikanpansasila.R
import com.fikri.mediapembelajaranpendidikanpansasila.data.local.AppDatabase
import com.fikri.mediapembelajaranpendidikanpansasila.utils.DragTarget
import com.fikri.mediapembelajaranpendidikanpansasila.utils.DraggableScreen
import com.fikri.mediapembelajaranpendidikanpansasila.utils.DropTarget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Struktur Data Lokal untuk UI
data class KuisItemUI(
    val id: String,
    val teksSoal: String,
    val namaGambarJawaban: String,
    var isTerjawabBenar: Boolean = false
)

// Helper Function: Mengubah String (misal: "ic_bintang") menjadi Resource ID Gambar
fun getDrawableIdByName(context: Context, resourceName: String): Int {
    val id = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
    // Jika gambar tidak ditemukan, gunakan gambar default/logo agar aplikasi tidak crash
    return if (id != 0) id else R.drawable.ic_launcher_foreground
}

@Composable
fun MainKuisScreen2(
    kuisId: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    // State untuk menyimpan data dari Database
    var isLoading by remember { mutableStateOf(true) }
    val daftarSoal = remember { mutableStateListOf<KuisItemUI>() }
    val daftarPilihan = remember { mutableStateListOf<String>() }

    // Ambil data dari Room DB saat layar pertama kali dibuka
    LaunchedEffect(key1 = kuisId) {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)
            val dao = db.kuisDao()

            // Ambil pasangan soal dari DB
            val pairsFromDb = dao.getQuizPairsByChapter(kuisId)

            withContext(Dispatchers.Main) {
                // Konversi data DB ke Data UI dan Acak Posisinya
                val soalAcak = pairsFromDb.map {
                    KuisItemUI(it.id, it.targetText ?: "", it.draggableImage ?: "")
                }.shuffled()

                val pilihanAcak = pairsFromDb.map {
                    it.draggableImage ?: ""
                }.shuffled()

                daftarSoal.addAll(soalAcak)
                daftarPilihan.addAll(pilihanAcak)
                isLoading = false
            }
        }
    }

    DraggableScreen(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_kuis),
            contentDescription = "Background Game",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp)
            ) {
                // --- HEADER ---
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FloatingActionButton(
                        onClick = onBackClick,
                        containerColor = Color(0xFFF44336), contentColor = Color.White, shape = CircleShape, modifier = Modifier.size(56.dp)
                    ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali") }
                    Spacer(modifier = Modifier.width(16.dp))
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(50)) {
                        Text("Cocokkan Gambar!", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF2196F3), modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(24.dp)) {

                    // === BLOK KIRI: SOAL ===
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                        shape = RoundedCornerShape(20.dp), modifier = Modifier.weight(1.2f).fillMaxHeight()
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(daftarSoal.size) { index ->
                                val soal = daftarSoal[index]
                                Row(
                                    modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp)).padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("${index + 1}. ${soal.teksSoal}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF333333), modifier = Modifier.weight(1f))
                                    Spacer(modifier = Modifier.width(16.dp))

                                    DropTarget<String>(
                                        modifier = Modifier.size(70.dp), // Sedikit dibesarkan untuk menampung gambar
                                        onDrop = { droppedImageName ->
                                            if (droppedImageName == soal.namaGambarJawaban) {
                                                daftarSoal[index] = soal.copy(isTerjawabBenar = true)
                                                daftarPilihan.remove(droppedImageName)
                                            }
                                        }
                                    ) { isHovered ->
                                        Box(
                                            modifier = Modifier.fillMaxSize()
                                                .border(
                                                    width = if (soal.isTerjawabBenar) 0.dp else if (isHovered) 4.dp else 3.dp,
                                                    color = if (soal.isTerjawabBenar) Color.Transparent else if (isHovered) Color(0xFF2196F3) else Color.LightGray,
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .background(if (soal.isTerjawabBenar) Color(0xFF4CAF50) else Color.White, RoundedCornerShape(12.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (soal.isTerjawabBenar) {
                                                // Jika benar, tampilkan gambar jawabannya kecil di dalam kotak, plus overlay ceklis
                                                Image(
                                                    painter = painterResource(id = getDrawableIdByName(context, soal.namaGambarJawaban)),
                                                    contentDescription = null,
                                                    modifier = Modifier.fillMaxSize().padding(4.dp).clip(RoundedCornerShape(8.dp))
                                                )
                                                Icon(Icons.Default.Check, "Benar", tint = Color.Green, modifier = Modifier.size(48.dp))
                                            } else {
                                                Text("?", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = if (isHovered) Color(0xFF2196F3) else Color.LightGray)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // === BLOK KANAN: JAWABAN (GAMBAR) ===
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4).copy(alpha = 0.9f)),
                        shape = RoundedCornerShape(20.dp), modifier = Modifier.weight(0.8f).fillMaxHeight()
                    ) {
                        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                            Text("Pilihan Gambar:", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFF9800), modifier = Modifier.padding(bottom = 16.dp))

                            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxSize()) {
                                items(daftarPilihan, key = { it }) { imageName ->
                                    DragTarget(dataToDrop = imageName) {
                                        Card(
                                            shape = RoundedCornerShape(16.dp),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color.White),
                                            modifier = Modifier.aspectRatio(1f)
                                        ) {
                                            // Render Gambar Berdasarkan Nama dari Database!
                                            Image(
                                                painter = painterResource(id = getDrawableIdByName(context, imageName)),
                                                contentDescription = "Pilihan",
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier.fillMaxSize().padding(12.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}