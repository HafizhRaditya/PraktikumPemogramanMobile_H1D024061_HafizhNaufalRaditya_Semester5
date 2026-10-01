package com.pemmob1.h1d024061

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pemmob1.h1d024061.ui.screen.DaftarProdukScreen
import com.pemmob1.h1d024061.ui.screen.DetailProductScreen
import com.pemmob1.h1d024061.ui.screen.HubungiKamiScreen
import com.pemmob1.h1d024061.ui.theme.JualanTheme
import com.pemmob1.h1d024061.ui.viewmodel.ProductViewModel

// ============================================================================
// Modul Pertemuan 3 bagian I dan J - Activity baru
// Modul Pertemuan 4 bagian E      - Peta rute navigasi
// Modul Pertemuan 5 bagian K      - ViewModel dibuat di sini
// ============================================================================
// Activity adalah satu layar yang berinteraksi dengan pengguna. HomeActivity
// adalah Launcher Activity: layar pertama yang terbuka saat ikon aplikasi
// diketuk. Penandanya ada di AndroidManifest.xml, berupa intent-filter dengan
// action MAIN dan category LAUNCHER.
//
// Sejak Pertemuan 4, Activity ini memuat NavHost yang menjadi "terminal"
// seluruh perpindahan halaman aplikasi.
// ============================================================================

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JualanTheme {

                // rememberNavController membuat pengendali navigasi sekaligus
                // mengingat riwayat halaman (back stack), sehingga tombol
                // kembali bekerja dengan urutan yang benar.
                val navController = rememberNavController()

                // Modul Pertemuan 5 bagian K - membuat ViewModel.
                // viewModel() membuat ProductViewModel pada pemanggilan
                // pertama, lalu mengembalikan objek yang SAMA pada setiap
                // recomposition maupun setelah layar diputar. Karena dipanggil
                // di tingkat Activity (di luar NavHost), satu ViewModel ini
                // dipakai bersama oleh halaman daftar dan halaman detail, jadi
                // data hanya diunduh sekali.
                val productViewModel: ProductViewModel = viewModel()

                // startDestination menentukan halaman yang pertama tampil.
                NavHost(navController = navController, startDestination = "daftar_produk") {

                    composable(route = "daftar_produk") {
                        DaftarProdukScreen(
                            navController = navController,
                            viewModel = productViewModel,
                        )
                    }

                    // Rute dengan argumen: bagian {productId} diisi angka saat
                    // navigasi dipanggil, misalnya "detail/3".
                    composable(
                        route = "detail/{productId}",
                        arguments = listOf(
                            navArgument("productId") { type = NavType.IntType },
                        ),
                    ) { backStackEntry ->
                        // Argumen dibaca dari backStackEntry. Operator elvis ?:
                        // memberi nilai cadangan 0 bila argumennya kosong,
                        // supaya aplikasi tidak crash.
                        val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                        DetailProductScreen(
                            productId = productId,
                            navController = navController,
                            viewModel = productViewModel,
                        )
                    }

                    composable(route = "hubungi_kami") {
                        HubungiKamiScreen(navController = navController)
                    }
                }
            }
        }
    }
}
