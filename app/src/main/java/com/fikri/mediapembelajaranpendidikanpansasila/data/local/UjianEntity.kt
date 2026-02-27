package com.fikri.mediapembelajaranpendidikanpansasila.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "chapters_exams")
data class ExamChapterEntity(
    @PrimaryKey val id: String,
    val title: String,
    val iconAssetName: String?,
    val durationMinutes: Int = 30,
    val passingGrade: Int = 70,
    val isActive: Boolean = true,

    val createdAt: Long? = null,
    val updatedAt: Long? = null
)

@Entity(
    tableName = "exam_questions",
    foreignKeys = [
        ForeignKey(
            entity = ExamChapterEntity::class,
            parentColumns = ["id"],
            childColumns = ["chapterExamId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("chapterExamId")]
)
data class ExamQuestionEntity(
    @PrimaryKey val id: String,
    val chapterExamId: String,

    val questionText: String,
    val questionImage: String?,
    val questionVoiceFile: String?,

    val optionAText: String,
    val optionBText: String,
    val optionCText: String,
    val correctOption: String,
    val scoreWeight: Int = 10,

    val createdAt: Long? = null,
    val updatedAt: Long? = null
)