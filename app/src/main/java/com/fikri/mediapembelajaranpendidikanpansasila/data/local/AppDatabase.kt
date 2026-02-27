package com.fikri.mediapembelajaranpendidikanpansasila.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Tulis semua Entity yang kamu punya di sini
@Database(
    entities = [
        MateriEntity::class,
        QuizChapterEntity::class,
        QuizPairEntity::class,
        ExamChapterEntity::class,
        ExamQuestionEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun materiDao(): MateriDao
    abstract fun kuisDao(): KuisDao
    abstract fun ujianDao(): UjianDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "materi_database" // Nama file database di dalam HP
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}