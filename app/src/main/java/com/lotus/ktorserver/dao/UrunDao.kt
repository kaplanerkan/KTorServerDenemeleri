package com.lotus.ktorserver.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lotus.ktorserver.models.Urun
import kotlinx.coroutines.flow.Flow

@Dao
interface UrunDao {
    @Upsert
    suspend fun upsertUrun(urun: Urun)

    @Query("SELECT * FROM urunler WHERE id = :id")
    suspend fun getUrunById(id: Int): Urun?

    @Query("UPDATE urunler SET urunIsmi = :yeniIsim, fiyati = :yeniFiyat WHERE id = :id")
    suspend fun updateUrun(id: Int, yeniIsim: String, yeniFiyat: Double): Int

    @Query("SELECT * FROM urunler")
    suspend fun getAllUrunler(): List<Urun>

    @Query("SELECT * FROM urunler ORDER BY id DESC")
    fun getAllUrunlerFlow(): Flow<List<Urun>>


    @Query("DELETE FROM urunler WHERE id = :id")
    suspend fun deleteById(id: Int): Int

}