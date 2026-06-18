package com.example.si_akademik_its.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.si_akademik_its.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiodataScreen(onBack: () -> Unit) {
    val biodataFields = listOf(
        "NRP" to "5025231245",
        "Nama" to "Rafie Zaidan Umara",
        "Jenis Kelamin" to "Laki-laki",
        "NIK" to "3578240206050001",
        "Dosen Wali" to "Dr. Wahyu Suadi, S.Kom., MM., M.Kom.",
        "Alamat" to "Jalan Florence J2/10 Pakuwon City Surabaya",
        "Telepon" to "085859190819",
        "Email" to "rafieumara@gmail.com",
        "Tanggal Lahir" to "2 Jun 2005",
        "Gol. Darah" to "B+"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil Mahasiswa", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ItsBiruTua, titleContentColor = ItsWhite, navigationIconContentColor = ItsWhite)
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(ItsSurface).padding(padding)) {
            // Header Profile
            item {
                Box(modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(ItsBiruTua, ItsBiruCover))).padding(32.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(ItsKuningTua), contentAlignment = Alignment.Center) {
                            Text("RZ", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = ItsBiruTua)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Rafie Zaidan Umara", color = ItsWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("5025231245", color = ItsWhite.copy(0.7f))
                    }
                }
            }
            // Biodata Items
            items(biodataFields.size) { index ->
                val (label, value) = biodataFields[index]
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text(label, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    Text(value, color = ItsTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1.5f))
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = ItsDivider)
            }
        }
    }
}
