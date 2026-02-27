package com.fikri.mediapembelajaranpendidikanpansasila.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface KuisDao {
    // 1. Ambil semua Bab Kuis yang aktif (untuk ditampilkan di Menu Kuis)
    @Query("SELECT * FROM chapters_quizzes WHERE isActive = 1")
    fun getAllActiveQuizChapters(): Flow<List<QuizChapterEntity>>

    // 2. Ambil detail satu Bab Kuis
    @Query("SELECT * FROM chapters_quizzes WHERE id = :chapterId")
    suspend fun getQuizChapterById(chapterId: String): QuizChapterEntity?

    // 3. Ambil semua pasangan soal (Drag & Drop) khusus untuk Bab tertentu
    @Query("SELECT * FROM quiz_pairs WHERE chapterQuizId = :chapterId")
    suspend fun getQuizPairsByChapter(chapterId: String): List<QuizPairEntity>

    // 4. Simpan/Update data Bab Kuis dari Server
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizChapters(chapters: List<QuizChapterEntity>)

    // 5. Simpan/Update data Pasangan Soal dari Server
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizPairs(pairs: List<QuizPairEntity>)
}