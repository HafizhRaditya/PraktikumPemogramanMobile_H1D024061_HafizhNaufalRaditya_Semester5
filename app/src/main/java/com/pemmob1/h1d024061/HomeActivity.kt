package com.pemmob1.h1d024061

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pemmob1.h1d024061.ui.screen.DaftarProdukScreen
import com.pemmob1.h1d024061.ui.theme.JualanTheme

// ============================================================================
// Modul Pertemuan 3 bagian I dan J - Activity baru
// ============================================================================
// Activity adalah satu layar yang berinteraksi dengan pengguna. HomeActivity
// kini menjadi Launcher Activity: layar yang pertama terbuka saat ikon
// aplikasi diketuk. Penandanya ada di AndroidManifest.xml, berupa
// intent-filter dengan action MAIN dan category LAUNCHER.
//
// Fungsi Greeting bawaan templat sudah dihapus karena tidak dipakai.
// ============================================================================

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JualanTheme {
                DaftarProdukScreen()
            }
        }
    }
}
