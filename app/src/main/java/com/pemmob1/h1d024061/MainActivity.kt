package com.pemmob1.h1d024061

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pemmob1.h1d024061.ui.screen.BasicInfoScreen
import com.pemmob1.h1d024061.ui.screen.HubungiKamiScreen
import com.pemmob1.h1d024061.ui.theme.JualanTheme

// ============================================================================
// Modul Pertemuan 2 bagian F - Membuat Halaman Navigasi
// ============================================================================
// MainActivity kini jauh lebih ringkas dibanding Pertemuan 1.
//
// Seluruh isi function LayoutTentangJualan sudah dipindahkan ke
// ui/screen/BasicInfoScreen.kt. Yang tersisa di sini hanya dua tugas:
// menyalakan tema, dan menjadi "peta jalan" yang menghubungkan dua halaman.
//
// onCreate() tetap bagian dari Lifecycle Activity, sama seperti Pertemuan 1.
// ============================================================================

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JualanTheme {
                AplikasiJualan()
            }
        }
    }
}

/**
 * Kerangka utama aplikasi beserta peta navigasinya.
 */
@Composable
fun AplikasiJualan() {

    // Surface bertindak sebagai kanvas dasar yang dibentangkan memenuhi
    // seluruh layar, memakai warna latar bawaan dari tema.
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {

        // rememberNavController menciptakan "pemandu jalan" yang menyimpan
        // riwayat halaman. Karena memakai penyimpanan yang tahan perubahan
        // konfigurasi, memutar layar tidak akan melempar pengguna kembali ke
        // halaman awal.
        val navController = rememberNavController()

        // NavHost mendaftarkan setiap rute ke halaman yang harus digambar.
        // Hanya satu halaman aktif dalam satu waktu.
        NavHost(
            navController = navController,
            // Halaman yang pertama kali ditampilkan saat aplikasi dibuka.
            startDestination = "basic_info",
        ) {

            composable("basic_info") {
                BasicInfoScreen(
                    // Di sinilah "tombol Hubungi Kami ditekan" diterjemahkan
                    // menjadi "pindah ke rute form_screen".
                    onNavigateToForm = { navController.navigate("form_screen") }
                )
            }

            composable("form_screen") {
                // navController diteruskan supaya tombol panah di TopBar bisa
                // memanggil popBackStack() dan kembali ke halaman sebelumnya.
                HubungiKamiScreen(navController = navController)
            }
        }
    }
}
