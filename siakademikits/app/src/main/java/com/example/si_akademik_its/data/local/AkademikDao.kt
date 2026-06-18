package com.example.myitsacademic.data.local
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface AkademikDao {
    // Mahasiswa Queries
    @Query("SELECT * FROM mahasiswa WHERE nrp = :nrp LIMIT 1")
    fun getMahasiswa(nrp: String): Flow<MahasiswaEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMahasiswa(mahasiswa: MahasiswaEntity)
    // Jadwal Queries
    @Query("SELECT * FROM jadwal_kuliah")
    fun getJadwalKuliah(): Flow<List<JadwalEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllJadwal(jadwalList: List<JadwalEntity>)
    @Query("UPDATE jadwal_kuliah SET isPresensiDone = :status WHERE id = :jadwalId")
    suspend fun updatePresensiStatus(jadwalId: Int, status: Boolean)
    @Query("DELETE FROM jadwal_kuliah")
    suspend fun clearJadwal()
    // Nilai KHS Queries
    @Query("SELECT * FROM nilai_khs")
    fun getNilaiKHS(): Flow<List<NilaiKhsEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNilai(nilaiList: List<NilaiKhsEntity>)
    @Query("DELETE FROM nilai_khs")
    suspend fun clearNilai()
}
