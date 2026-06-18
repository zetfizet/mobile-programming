package com.example.si_akademik_its.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.si_akademik_its.ui.theme.*

data class JadwalItem(val hari: String, val jam: String, val ruang: String, val matkul: String, val kelas: String, val dosen: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JadwalScreen(onBack: () -> Unit) {
    val jadwal = listOf(
        JadwalItem("Kamis", "15:30-17:20", "TW1-203", "UG4901 - Agama Islam", "32", "Miqdarul Khoir Syarofit, Lc., M.Pd.I"),
        JadwalItem("Kamis", "11:00-12:50", "TW1-306", "UG4914 - Bahasa Inggris", "29", "Hermanto, S.S., M.Pd."),
        JadwalItem("Rabu", "07:00-08:50", "TIF 113", "EF4602 - Interaksi Manusia & Komputer", "B", "Dr. Anny Yuniarti, S.Kom., M.Comp.Sc."),
        JadwalItem("Rabu", "15:30-17:20", "TW1-703", "UG4915 - Kewirausahaan Berbasis Teknologi", "27", "Yanurita Dwi Hapsari, S.Si., M.Sc.")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jadwal Kuliah", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ItsBiruTua, titleContentColor = ItsWhite, navigationIconContentColor = ItsWhite)
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(ItsSurface).padding(padding).padding(16.dp)) {
            item {
                Text("Informatika - Semester Genap 2025", color = ItsTextSecondary, modifier = Modifier.padding(bottom = 16.dp))
            }
            items(jadwal.size) { index ->
                val item = jadwal[index]
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = ItsWhite),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(item.hari, fontWeight = FontWeight.Bold, color = ItsBiruTuaLambang)
                            Text(item.jam, color = ItsTextSecondary)
                        }
                        Text(item.matkul, fontWeight = FontWeight.Bold, color = ItsTextPrimary, fontSize = 15.sp, modifier = Modifier.padding(vertical = 4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Ruang: ${item.ruang}", fontSize = 12.sp)
                            Text("Kelas: ${item.kelas}", fontSize = 12.sp)
                        }
                        Text("Dosen: ${item.dosen}", fontSize = 12.sp, color = ItsTextSecondary, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
    }
}
