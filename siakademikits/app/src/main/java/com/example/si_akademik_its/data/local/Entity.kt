package com.example.myitsacademic.data.local
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "mahasiswa")
data class MahasiswaEntity(
    @PrimaryKey val nrp: String,
    val nama: String,
    val departemen: String,
    val ipk: Double,
    val totalSks: Int,
    val statusAkademik: String, // Aktif, Cuti
    val dosenWali: String
)
@Entity(tableName = "jadwal_kuliah")
data class JadwalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val kodeMk: String,
    val namaMk: String,
    val kelas: String,
    val hari: String,
    val jamMulai: String,
    val jamSelesai: String,
    val ruang: String,
    val dosen: String,
    val sks: Int,
    var isPresensiDone: Boolean = false // Menyimpan status presensi mahasiswa
)
@Entity(tableName = "nilai_khs")
data class NilaiKhsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val semester: Int,
    val kodeMk: String,
    val namaMk: String,
    val nilaiHuruf: String,
    val nilaiAngka: Double,
    val sks: Int
)
