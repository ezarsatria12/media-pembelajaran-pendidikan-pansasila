package com.fikri.mediapembelajaranpendidikanpansasila.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MateriDao {
    // Mengambil SEMUA materi secara real-time (langsung update kalau ada perubahan)
    @Query("SELECT * FROM tabel_materi")
    fun getAllMateri(): Flow<List<MateriEntity>>

    // Mengambil SATU materi saja berdasarkan ID-nya
    @Query("SELECT * FROM tabel_materi WHERE id = :materiId")
    suspend fun getMateriById(materiId: String): MateriEntity?

    // Menyimpan daftar materi. Kalau ID-nya sudah ada, datanya akan ditimpa (REPLACE) dengan yang baru
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(materiList: List<MateriEntity>)

    // Mengubah status apakah PDF sudah di-download atau belum
    @Query("UPDATE tabel_materi SET isDownloaded = :status WHERE id = :materiId")
    suspend fun updateDownloadStatus(materiId: String, status: Boolean)
}