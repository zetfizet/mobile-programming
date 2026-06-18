package com.example.si_akademik_its.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.si_akademik_its.ui.theme.*

data class SiaMenu(val label: String, val icon: ImageVector)

@Composable
fun DashboardScreen(onLogout: () -> Unit, onNavigateToBiodata: () -> Unit, onNavigateToJadwal: () -> Unit) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    val menuItems = listOf(
        SiaMenu("Biodata", Icons.Default.Person),
        SiaMenu("Transkrip", Icons.AutoMirrored.Filled.List),
        SiaMenu("Jadwal", Icons.Default.DateRange),
        SiaMenu("FRS", Icons.Default.Edit),
        SiaMenu("IPD", Icons.Default.Star),
        SiaMenu("Kurikulum", Icons.Default.Info),
        SiaMenu("Biaya", Icons.Default.ThumbUp),
        SiaMenu("Surat", Icons.Default.Email)
    )

    Column(modifier = Modifier.fillMaxSize().background(ItsSurface)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(ItsBiruTua, ItsBiruCover)))
                .padding(24.dp)
                .statusBarsPadding()
        ) {
            Column {
                Text("myITS Academic", color = ItsWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Portal Terintegrasi", color = ItsKuningTua, fontSize = 14.sp)
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Text("Modul Akademik", fontWeight = FontWeight.Bold, color = ItsBiruTua, fontSize = 16.sp) }
            
            items(menuItems.size) { index ->
                val item = menuItems[index]
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { 
                        when(item.label) {
                            "Biodata" -> onNavigateToBiodata()
                            "Jadwal" -> onNavigateToJadwal()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ItsWhite),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(item.icon, null, tint = ItsBiruTuaLambang)
                        Text(item.label, modifier = Modifier.padding(start = 16.dp), fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.Gray)
                    }
                }
            }
            
            item {
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
                ) {
                    Text("Keluar")
                }
            }
        }
    }
}
