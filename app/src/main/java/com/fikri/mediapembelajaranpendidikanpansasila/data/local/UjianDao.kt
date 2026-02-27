package com.fikri.mediapembelajaranpendidikanpansasila.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UjianDao {
    // 1. Ambil semua Bab Ujian yang aktif
    @Query("SELECT * FROM chapters_exams WHERE isActive = 1")
    fun getAllActiveExamChapters(): Flow<List<ExamChapterEntity>>

    // 2. Ambil detail satu Bab Ujian
    @Query("SELECT * FROM chapters_exams WHERE id = :chapterId")
    suspend fun getExamChapterById(chapterId: String): ExamChapterEntity?

    // 3. Ambil semua soal pilihan ganda untuk ujian tertentu
    @Query("SELECT * FROM exam_questions WHERE chapterExamId = :chapterId")
    suspend fun getExamQuestionsByChapter(chapterId: String): List<ExamQuestionEntity>

    // 4. Simpan/Update data Bab Ujian dari Server
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamChapters(chapters: List<ExamChapterEntity>)

    // 5. Simpan/Update data Soal Ujian dari Server
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamQuestions(questions: List<ExamQuestionEntity>)
}