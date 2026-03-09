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
import com.fikri.mediapembelajaranpendidikanpansasila.data.local.QuizChapterEntity
import com.fikri.mediapembelajaranpendidikanpansasila.data.local.QuizPairEntity
import com.fikri.mediapembelajaranpendidikanpansasila.data.local.ExamQuestionEntity
import com.fikri.mediapembelajaranpendidikanpansasila.data.local.ExamChapterEntity
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
                val materiDao = db.materiDao()
                val kuisDao = db.kuisDao()
                val currentTime = System.currentTimeMillis()

                // ==========================================
                // 1. PENGECEKAN & SUNTIK DATA MATERI
                // ==========================================
                val cekMateri = materiDao.getMateriById("bab1")
                if (cekMateri == null) {
                    android.util.Log.d("STATUS_DB", "Menyuntikkan Data Materi...")
                    val dataAwalMateri = listOf(
                        MateriEntity("bab1", "Bab 1", "Simbol Pancasila", "materi_bab1.pdf", true, 1, currentTime, currentTime),
                        MateriEntity("bab2", "Bab 2", "Makna Sila Pertama", "materi_bab2.pdf", true, 1, currentTime, currentTime),
                        MateriEntity("bab3", "Bab 3", "Makna Sila Kedua", "materi_bab3.pdf", true, 1, currentTime, currentTime),
                        MateriEntity("bab4", "Bab 4", "Hidup Rukun", "materi_bab4.pdf", true, 1, currentTime, currentTime),
                        MateriEntity("bab5", "Bab 5", "Gotong Royong", "materi_bab5.pdf", true, 1, currentTime, currentTime)
                    )
                    materiDao.insertAll(dataAwalMateri)
                }

                // ==========================================
                // 2. PENGECEKAN & SUNTIK DATA KUIS (TERPISAH!)
                // ==========================================
                val cekKuis = kuisDao.getQuizChapterById("kuis_bab1")
                if (cekKuis == null) {
                    android.util.Log.d("STATUS_DB", "Menyuntikkan Data Kuis...")

                    val chapterKuis = listOf(
                        QuizChapterEntity("kuis_bab1", "Latihan Bab 1", "ic_kuis1", true, currentTime, currentTime)
                    )
                    kuisDao.insertQuizChapters(chapterKuis)

                    val pasanganKuis = listOf(
                        QuizPairEntity("pair_1", "kuis_bab1", draggableImage = "ic_bintang", draggableText = null, targetImage = null, targetText = "Lambang sila pertama adalah...", correctAudioFile = null, createdAt = currentTime, updatedAt = currentTime),
                        QuizPairEntity("pair_2", "kuis_bab1", draggableImage = "ic_rantai", draggableText = null, targetImage = null, targetText = "Lambang sila kedua adalah...", correctAudioFile = null, createdAt = currentTime, updatedAt = currentTime),
                        QuizPairEntity("pair_3", "kuis_bab1", draggableImage = "ic_pohon_beringin", draggableText = null, targetImage = null, targetText = "Lambang sila ketiga adalah...", correctAudioFile = null, createdAt = currentTime, updatedAt = currentTime),
                        QuizPairEntity("pair_4", "kuis_bab1", draggableImage = "ic_kepala_banteng", draggableText = null, targetImage = null, targetText = "Lambang sila keempat adalah...", correctAudioFile = null, createdAt = currentTime, updatedAt = currentTime),
                        QuizPairEntity("pair_5", "kuis_bab1", draggableImage = "ic_padi_kapas", draggableText = null, targetImage = null, targetText = "Lambang sila kelima adalah...", correctAudioFile = null, createdAt = currentTime, updatedAt = currentTime)
                    )
                    kuisDao.insertQuizPairs(pasanganKuis)

                    val ujianDao = db.ujianDao()
                    val cekUjian = ujianDao.getExamChapterById("ujian_bab1")

                    if (cekUjian == null) {
                        android.util.Log.d("STATUS_DB", "Menyuntikkan Data Ujian...")

                        // --- HEADER BAB UJIAN ---
                        val chapterUjian = listOf(
                            ExamChapterEntity(
                                id = "ujian_bab1",
                                title = "Ujian Bab 1",
                                iconAssetName = "ic_ujian",
                                durationMinutes = 30, // Waktu ujian 30 menit
                                passingGrade = 70,    // KKM 70
                                isActive = true,
                                createdAt = currentTime,
                                updatedAt = currentTime
                            ),
                            ExamChapterEntity(
                                id = "ujian_bab2",
                                title = "Ujian Bab 2",
                                iconAssetName = "ic_ujian",
                                durationMinutes = 30,
                                passingGrade = 70,
                                isActive = true,
                                createdAt = currentTime,
                                updatedAt = currentTime
                            ),
                            ExamChapterEntity(
                                id = "ujian_bab3",
                                title = "Ujian Bab 3",
                                iconAssetName = "ic_ujian",
                                durationMinutes = 30,
                                passingGrade = 70,
                                isActive = true,
                                createdAt = currentTime,
                                updatedAt = currentTime
                            ),
                            ExamChapterEntity(
                                id = "ujian_bab4",
                                title = "Ujian Bab 4",
                                iconAssetName = "ic_ujian",
                                durationMinutes = 30,
                                passingGrade = 70,
                                isActive = true,
                                createdAt = currentTime,
                                updatedAt = currentTime
                            ),
                            ExamChapterEntity(
                                id = "ujian_bab5",
                                title = "Ujian Bab 5",
                                iconAssetName = "ic_ujian",
                                durationMinutes = 45, // Ujian bab terakhir mungkin lebih lama
                                passingGrade = 75,
                                isActive = true,
                                createdAt = currentTime,
                                updatedAt = currentTime
                            )
                        )
                        ujianDao.insertExamChapters(chapterUjian)

                        // --- SOAL UJIAN (PILIHAN GANDA) ---
                        val soalUjian = listOf(
                            ExamQuestionEntity(
                                id = "soal_u1",
                                chapterExamId = "ujian_bab1",
                                questionText = "Apa lambang sila pertama Pancasila?",
                                questionImage = null, // Isi dengan nama file gambar jika ada, misal: "img_soal_1"
                                questionVoiceFile = "bgm_ujian", // Isi nama file audio jika ada
                                optionAText = "Bintang",
                                optionBText = "Rantai",
                                optionCText = "Pohon Beringin",
                                correctOption = "A", // Menyimpan kunci jawaban dalam bentuk huruf A/B/C
                                scoreWeight = 10,    // Bobot nilai soal ini
                                createdAt = currentTime,
                                updatedAt = currentTime
                            ),
                            ExamQuestionEntity(
                                id = "soal_u2",
                                chapterExamId = "ujian_bab1",
                                questionText = "Bunyi sila kedua Pancasila adalah...",
                                questionImage = null,
                                questionVoiceFile = "bgm_ujian",
                                optionAText = "Ketuhanan Yang Maha Esa",
                                optionBText = "Kemanusiaan yang Adil dan Beradab",
                                optionCText = "Persatuan Indonesia",
                                correctOption = "B",
                                scoreWeight = 10,
                                createdAt = currentTime,
                                updatedAt = currentTime
                            ),
                            ExamQuestionEntity(
                                id = "soal_u3",
                                chapterExamId = "ujian_bab1",
                                questionText = "Sikap manakah yang paling sesuai dengan pengamalan sila ketiga di sekolah?",
                                questionImage = null,
                                questionVoiceFile = "bgm_ujian",
                                optionAText = "Memilih-milih teman saat bermain",
                                optionBText = "Mengikuti upacara bendera dengan khidmat",
                                optionCText = "Membiarkan teman berkelahi",
                                correctOption = "B",
                                scoreWeight = 15, // Soal yang lebih sulit bisa diberi bobot lebih tinggi
                                createdAt = currentTime,
                                updatedAt = currentTime
                            )
                        )
                        ujianDao.insertExamQuestions(soalUjian)

                        android.util.Log.d("STATUS_DB", "Data Ujian Berhasil Disuntikkan!")
                    }

            }
        } catch (e: Exception) {
                android.util.Log.e("ERROR_ROOM", "Gagal menyuntik data:", e)
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