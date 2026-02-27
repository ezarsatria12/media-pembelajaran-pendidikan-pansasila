package com.fikri.mediapembelajaranpendidikanpansasila.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "chapters_quizzes")
data class QuizChapterEntity(
    @PrimaryKey val id: String,
    val title: String,
    val iconAssetName: String?,
    val isActive: Boolean = true,

    val createdAt: Long? = null,
    val updatedAt: Long? = null
)

@Entity(
    tableName = "quiz_pairs",
    foreignKeys = [
        ForeignKey(
            entity = QuizChapterEntity::class,
            parentColumns = ["id"],
            childColumns = ["chapterQuizId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("chapterQuizId")]
)
data class QuizPairEntity(
    @PrimaryKey val id: String,
    val chapterQuizId: String,

    val draggableImage: String?,
    val draggableText: String?,
    val targetImage: String?,
    val targetText: String?,
    val correctAudioFile: String?,

    val createdAt: Long? = null,
    val updatedAt: Long? = null
)