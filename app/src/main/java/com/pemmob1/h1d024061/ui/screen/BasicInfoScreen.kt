package com.pemmob1.h1d024061.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import com.pemmob1.h1d024061.R

// ============================================================================
// Modul Pertemuan 2 bagian E - Modifikasi Main Activity dan Membuat Screen Baru
// ============================================================================
// Halaman informasi dasar aplikasi Jualan.
//
// Seluruh isi function LayoutTentangJualan yang sebelumnya berada di
// MainActivity.kt dipindahkan ke sini. Alasannya: MainActivity sebaiknya hanya
// mengurus kerangka aplikasi dan navigasi, bukan ikut menggambar isi layar.
// ============================================================================

/**
 * @param onNavigateToForm dipanggil saat tombol "Hubungi Kami" ditekan.
 *        Nilai bawaannya kosong supaya fungsi ini tetap bisa dipanggil dari
 *        @Preview tanpa navigasi.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicInfoScreen(onNavigateToForm: () -> Unit = {}) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jualan") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // --------------------------------------------------------------
            // Modul bagian E.4 - Mengganti nama produk dengan ikon aplikasi
            // --------------------------------------------------------------
            // Sebelumnya Box ini berisi Text("Jualan") berwarna putih. Kini
            // digantikan gambar ikon aplikasi, sehingga identitas visualnya
            // konsisten dengan ikon yang muncul di layar utama ponsel.
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    // Warna latar diambil dari tema, bukan Color.Gray yang
                    // ditulis langsung. Dengan begitu ia ikut menyesuaikan
                    // saat mode gelap dinyalakan.
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                    contentDescription = "Ikon aplikasi Jualan",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(100.dp),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --------------------------------------------------------------
            // Modul bagian E.5 - Menerapkan tema Material Design
            // --------------------------------------------------------------
            // fontSize yang dulu ditulis langsung (24.sp) diganti dengan gaya
            // dari tema. Kalau nanti ukuran judul perlu diubah, cukup sunting
            // Type.kt sekali - tidak perlu menyisir seluruh layar.
            Text(
                text = "Tentang Jualan",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Jualan adalah aplikasi yang membantu pelaku usaha kecil " +
                    "mencatat barang dagangan dan memantau penjualan hariannya " +
                    "langsung dari ponsel, tanpa perlu koneksi internet.",
                style = MaterialTheme.typography.bodyLarge,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Row yang dulu berlatar Color(0xFFE0E0E0) kini dibungkus Card.
            // containerColor mengisi warna dasarnya dengan warna dari tema,
            // sehingga tidak ada lagi kode heksadesimal yang ditulis langsung
            // di tengah layar.
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                ),
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    // Modifier.weight membagi ruang secara proporsional.
                    // Total bobot 1f + 2f = 3f, sehingga teks pertama mendapat
                    // sepertiga lebar dan teks kedua dua pertiganya.
                    Text(
                        text = "Misi Kami:",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = "Memajukan UMKM Lokal",
                        modifier = Modifier.weight(2f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol menuju halaman formulir. BasicInfoScreen sendiri tidak
            // tahu apa-apa soal NavController - ia hanya melaporkan bahwa
            // tombolnya ditekan, dan MainActivity yang memutuskan artinya
            // "buka rute form_screen".
            Button(
                onClick = onNavigateToForm,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Hubungi Kami",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}
