package com.fikri.mediapembelajaranpendidikanpansasila.ui.screens

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Audiotrack
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
import androidx.compose.ui.window.Dialog
import com.fikri.mediapembelajaranpendidikanpansasila.R
import com.fikri.mediapembelajaranpendidikanpansasila.data.local.AppDatabase
import com.fikri.mediapembelajaranpendidikanpansasila.data.local.ExamChapterEntity
import com.fikri.mediapembelajaranpendidikanpansasila.data.local.ExamQuestionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun getResourceIdentifier(context: Context, name: String?, type: String): Int? {
    if (name.isNullOrEmpty()) return null
    val id = context.resources.getIdentifier(name, type, context.packageName)
    return if (id != 0) id else null
}

@Composable
fun MainUjianScreen(
    ujianId: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(true) }
    var chapterInfo by remember { mutableStateOf<ExamChapterEntity?>(null) }
    val daftarSoal = remember { mutableStateListOf<ExamQuestionEntity>() }

    val jawabanUser = remember { mutableStateMapOf<String, String>() }
    var timeLeftSeconds by remember { mutableStateOf(0) }
    var isExamFinished by remember { mutableStateOf(false) }

    // --- STATE AUDIO (HANYA VO) ---
    var isPlayingVo by remember { mutableStateOf(false) }
    var voPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    var showResultDialog by remember { mutableStateOf(false) }
    var finalScore by remember { mutableStateOf(0) }

    // 1. Ambil Data DB
    LaunchedEffect(key1 = ujianId) {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)
            val ujianDao = db.ujianDao()

            val info = ujianDao.getExamChapterById(ujianId)
            val soalDb = ujianDao.getExamQuestionsByChapter(ujianId)

            withContext(Dispatchers.Main) {
                if (info != null && soalDb.isNotEmpty()) {
                    chapterInfo = info
                    daftarSoal.addAll(soalDb.shuffled())
                    timeLeftSeconds = info.durationMinutes * 60
                    isLoading = false
                } else {
                    onBackClick()
                }
            }
        }
    }

    // 2. Lifecycle Audio (Bersihkan VO saat layar ditutup)
    DisposableEffect(Unit) {
        onDispose {
            voPlayer?.stop()
            voPlayer?.release()
        }
    }

    // 3. Timer Countdown
    LaunchedEffect(key1 = isLoading, key2 = isExamFinished) {
        if (!isLoading && !isExamFinished) {
            while (timeLeftSeconds > 0) {
                delay(1000L)
                timeLeftSeconds--
            }
            if (timeLeftSeconds == 0) isExamFinished = true
        }
    }

    fun hitungDanTampilkanHasil() {
        var totalSkor = 0
        daftarSoal.forEach { soal ->
            if (jawabanUser[soal.id] == soal.correctOption) {
                totalSkor += soal.scoreWeight
            }
        }
        finalScore = totalSkor.coerceAtMost(100)
        isExamFinished = true

        // Pastikan VO mati saat ujian selesai/waktu habis
        voPlayer?.stop()
        isPlayingVo = false

        showResultDialog = true
    }

    LaunchedEffect(key1 = isExamFinished) {
        if (isExamFinished && !showResultDialog) {
            hitungDanTampilkanHasil()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // --- BACKGROUND ---
        val bgResId = getResourceIdentifier(context, chapterInfo?.iconAssetName, "drawable") ?: R.drawable.bg_ujian
        Image(
            painter = painterResource(id = bgResId),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            val pagerState = rememberPagerState(pageCount = { daftarSoal.size })

            // Matikan VO otomatis jika slide digeser ke soal lain
            LaunchedEffect(pagerState.currentPage) {
                voPlayer?.stop()
                voPlayer?.release()
                voPlayer = null
                isPlayingVo = false
            }

            Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                // --- HEADER ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FloatingActionButton(
                        onClick = { if (!isExamFinished) onBackClick() },
                        containerColor = Color(0xFFF44336), contentColor = Color.White, shape = CircleShape, modifier = Modifier.size(56.dp)
                    ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali") }

                    val minutes = timeLeftSeconds / 60
                    val seconds = timeLeftSeconds % 60

                    Card(
                        colors = CardDefaults.cardColors(containerColor = if (timeLeftSeconds < 60) Color.Red else Color.White),
                        shape = RoundedCornerShape(50), elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Text(
                            text = String.format("%02d:%02d", minutes, seconds),
                            fontSize = 24.sp, fontWeight = FontWeight.ExtraBold,
                            color = if (timeLeftSeconds < 60) Color.White else Color(0xFFD32F2F),
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                        )
                    }

                    // --- TOMBOL VOICE OVER (TUNGGAL) ---
                    val soalSaatIni = daftarSoal[pagerState.currentPage]
                    val rawVoId = getResourceIdentifier(context, soalSaatIni.questionVoiceFile, "raw")
                    val isVoAvailable = rawVoId != null

                    FloatingActionButton(
                        onClick = {
                            if (isVoAvailable && !isExamFinished) {
                                if (isPlayingVo) {
                                    // Jika sedang main, di-pause
                                    voPlayer?.pause()
                                    isPlayingVo = false
                                } else {
                                    // Mainkan dari awal
                                    voPlayer?.release()
                                    voPlayer = MediaPlayer.create(context, rawVoId!!).apply {
                                        isLooping = false // ATURAN: Play 1 kali saja
                                        setOnCompletionListener {
                                            isPlayingVo = false // Ubah warna tombol kembali ke biru saat selesai
                                        }
                                        start()
                                    }
                                    isPlayingVo = true
                                }
                            }
                        },
                        containerColor = if (!isVoAvailable) Color.LightGray else if (isPlayingVo) Color.Green else Color(0xFF2196F3),
                        contentColor = Color.White,
                        shape = CircleShape, modifier = Modifier.size(56.dp),
                        elevation = FloatingActionButtonDefaults.elevation(if (isVoAvailable) 6.dp else 0.dp)
                    ) { Icon(Icons.Default.Audiotrack, "Voice Over") }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- AREA PERMAINAN ---
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // TOMBOL SEBELUMNYA (<)
                    Box(modifier = Modifier.width(80.dp), contentAlignment = Alignment.CenterStart) {
                        FloatingActionButton(
                            onClick = {
                                if (pagerState.currentPage > 0 && !isExamFinished) {
                                    coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                                }
                            },
                            containerColor = if (pagerState.currentPage > 0 && !isExamFinished) Color(0xFF2196F3) else Color.LightGray,
                            contentColor = Color.White, shape = CircleShape, modifier = Modifier.size(64.dp),
                            elevation = FloatingActionButtonDefaults.elevation(if (pagerState.currentPage > 0 && !isExamFinished) 8.dp else 0.dp)
                        ) { Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Sebelumnya", modifier = Modifier.size(36.dp)) }
                    }

                    // PAGER SOAL
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        userScrollEnabled = !isExamFinished
                    ) { page ->
                        val soal = daftarSoal[page]

                        Row(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            // BLOK KIRI: SOAL
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                                shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(8.dp),
                                modifier = Modifier.weight(1.2f).fillMaxHeight()
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Soal ${page + 1} dari ${daftarSoal.size}",
                                        fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Gray, modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    val imageId = getResourceIdentifier(context, soal.questionImage, "drawable")
                                    if (imageId != null) {
                                        Image(
                                            painter = painterResource(id = imageId),
                                            contentDescription = "Gambar Soal",
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFF0F0F0))
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))
                                    }

                                    Text(
                                        text = soal.questionText,
                                        fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF333333),
                                        textAlign = TextAlign.Start, lineHeight = 32.sp, modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            // BLOK KANAN: PILIHAN JAWABAN
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4).copy(alpha = 0.9f)),
                                shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(8.dp),
                                modifier = Modifier.weight(0.8f).fillMaxHeight()
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text("Pilih Jawaban:", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFF9800))

                                    val opsiList = listOf(
                                        Pair("A", soal.optionAText),
                                        Pair("B", soal.optionBText),
                                        Pair("C", soal.optionCText)
                                    )

                                    opsiList.forEach { (huruf, teksOpsi) ->
                                        val isSelected = jawabanUser[soal.id] == huruf

                                        Card(
                                            modifier = Modifier.fillMaxWidth().clickable(enabled = !isExamFinished) {
                                                jawabanUser[soal.id] = huruf
                                            },
                                            shape = RoundedCornerShape(16.dp),
                                            colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFF4CAF50) else Color.White),
                                            border = BorderStroke(2.dp, if (isSelected) Color(0xFF388E3C) else Color.LightGray)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier.size(40.dp).background(if (isSelected) Color.White else Color(0xFFE0E0E0), CircleShape),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(huruf, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color(0xFF388E3C) else Color.Gray)
                                                }
                                                Spacer(modifier = Modifier.width(16.dp))
                                                Text(teksOpsi, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = if (isSelected) Color.White else Color(0xFF333333))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // TOMBOL SELANJUTNYA (>) ATAU SELESAI
                    Box(modifier = Modifier.width(80.dp), contentAlignment = Alignment.CenterEnd) {
                        if (pagerState.currentPage == daftarSoal.size - 1) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                FloatingActionButton(
                                    onClick = { if (!isExamFinished) hitungDanTampilkanHasil() },
                                    containerColor = if (!isExamFinished) Color(0xFFD32F2F) else Color.LightGray,
                                    contentColor = Color.White, shape = CircleShape, modifier = Modifier.size(64.dp),
                                    elevation = FloatingActionButtonDefaults.elevation(if (!isExamFinished) 8.dp else 0.dp)
                                ) { Icon(Icons.Default.Check, "Selesai", modifier = Modifier.size(36.dp)) }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Selesai", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (!isExamFinished) Color(0xFFD32F2F) else Color.LightGray)
                            }
                        } else {
                            FloatingActionButton(
                                onClick = {
                                    if (!isExamFinished) {
                                        coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                                    }
                                },
                                containerColor = if (!isExamFinished) Color(0xFF2196F3) else Color.LightGray,
                                contentColor = Color.White, shape = CircleShape, modifier = Modifier.size(64.dp),
                                elevation = FloatingActionButtonDefaults.elevation(if (!isExamFinished) 8.dp else 0.dp)
                            ) { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Selanjutnya", modifier = Modifier.size(36.dp)) }
                        }
                    }
                }
            }
        }
    }

    // --- MODAL POPUP HASIL UJIAN ---
    if (showResultDialog && chapterInfo != null) {
        Dialog(onDismissRequest = { }) {
            Card(
                shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(16.dp), modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val isLulus = finalScore >= chapterInfo!!.passingGrade

                    Text(if (isLulus) "LULUS!" else "JANGAN MENYERAH!", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = if (isLulus) Color(0xFF4CAF50) else Color.Red)
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(modifier = Modifier.size(120.dp).background(if (isLulus) Color(0xFFE8F5E9) else Color(0xFFFFEBEE), CircleShape).border(4.dp, if (isLulus) Color(0xFF4CAF50) else Color.Red, CircleShape), contentAlignment = Alignment.Center) {
                        Text(finalScore.toString(), fontSize = 48.sp, fontWeight = FontWeight.Black, color = if (isLulus) Color(0xFF2E7D32) else Color(0xFFC62828))
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Nilai KKM: ${chapterInfo!!.passingGrade}", fontSize = 16.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { onBackClick() }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                    ) { Text("Kembali ke Menu", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}