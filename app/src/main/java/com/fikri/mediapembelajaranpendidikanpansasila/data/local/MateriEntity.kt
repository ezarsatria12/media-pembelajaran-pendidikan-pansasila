package com.fikri.mediapembelajaranpendidikanpansasila.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabel_materi")
data class MateriEntity(
    @PrimaryKey val id: String,
    val judul: String,
    val deskripsi: String,
    val namaFilePdf: String,
    val isDownloaded: Boolean = false,
    val versiMateri: Int = 1,

    // --- KOLOM SYNC ---
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)