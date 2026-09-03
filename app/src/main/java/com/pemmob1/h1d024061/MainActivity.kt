package com.pemmob1.h1d024061

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pemmob1.h1d024061.ui.theme.JualanTheme

class MainActivity : ComponentActivity() {

    // ========================================================================
    // Modul bagian J.j - Modifikasi function onCreate()
    // ========================================================================
    // onCreate() adalah bagian dari Lifecycle (siklus hidup) Activity: fungsi
    // ini dijalankan sistem Android saat halaman pertama kali diciptakan.
    // super.onCreate() menjalankan instruksi bawaan sistem lebih dulu agar
    // siklus hidup dasarnya berjalan stabil sebelum kode kita ditambahkan.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Membuat aplikasi tampil sampai menembus area status bar (jam dan
        // baterai di atas) serta navigation bar di bawah.
        enableEdgeToEdge()

        // setContent adalah blok tempat UI disusun memakai fungsi @Composable.
        setContent {
            // JualanTheme mengatur palet warna, tipografi, dan bentuk komponen
            // agar seragam di seluruh aplikasi. Namanya menyesuaikan nama
            // project, yaitu "Jualan".
            JualanTheme {
                // Scaffold adalah kerangka tata letak standar Material Design.
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Memanggil fungsi @Composable yang sudah kita buat.
                    // innerPadding diteruskan agar isi layar tidak tertutup
                    // status bar, mengikuti pola yang dipakai template bawaan
                    // Android Studio saat memanggil Greeting().
                    LayoutTentangJualan(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// ============================================================================
// Function bawaan template Android Studio.
// Modul meminta function baru dideklarasikan DI BAWAH function Greeting,
// jadi function ini sengaja dipertahankan.
// ============================================================================
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

// ============================================================================
// Modul bagian J.a - Mendeklarasikan Function LayoutTentangJualan()
// ============================================================================
// Anotasi @Composable memberi tahu Android bahwa ini bukan fungsi Kotlin
// biasa, melainkan fungsi khusus yang bertugas menggambar antarmuka ke layar.
// Karena itu, di dalamnya kita boleh memanggil komponen Compose lain.
//
// Nama fungsi wajib diawali huruf kapital (PascalCase) karena ia bertindak
// layaknya sebuah komponen UI utuh, bukan sekadar fungsi penghitung.
@Composable
fun LayoutTentangJualan(modifier: Modifier = Modifier) {

    // ------------------------------------------------------------------
    // Modul bagian J.b - Menambahkan Column
    // ------------------------------------------------------------------
    // Column menumpuk elemen-elemen di dalamnya secara VERTIKAL, dari atas
    // ke bawah.
    //   .fillMaxSize()  -> mengambil seluruh ruang yang tersedia dari induknya
    //   .padding(16.dp) -> jarak 16dp di keempat sisi, supaya isi layar tidak
    //                      menempel ke ujung layar ponsel
    //   horizontalAlignment = Alignment.CenterHorizontally
    //                   -> semua elemen di dalamnya rata tengah secara mendatar
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --------------------------------------------------------------
        // Modul bagian J.c - Menambahkan Box
        // --------------------------------------------------------------
        // Kalau Column menyusun elemen ke bawah, Box menumpuk elemen dari
        // belakang ke depan (seperti lapisan/layer).
        //   .size(100.dp)          -> lebar dan tinggi sama-sama 100dp (persegi)
        //   .clip(CircleShape)     -> memotong persegi tadi jadi lingkaran
        //   .background(Color.Gray)-> warna latar abu-abu
        //
        // PENTING soal urutan Modifier: .background dipanggil SESUDAH .clip,
        // sehingga abu-abunya hanya mengisi area yang sudah dipotong bulat.
        // Kalau urutannya dibalik, yang terlihat adalah kotak abu-abu utuh.
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            // Mengatur posisi isi Box supaya tepat di tengah, vertikal
            // maupun horizontal.
            contentAlignment = Alignment.Center
        ) {
            // ----------------------------------------------------------
            // Modul bagian J.d - Menambahkan Text di dalam Box
            // ----------------------------------------------------------
            // Text adalah komponen dasar Compose untuk mencetak tulisan.
            Text(
                text = "Jualan",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // --------------------------------------------------------------
        // Modul bagian J.e - Menambahkan Spacer di bawah Box
        // --------------------------------------------------------------
        // Spacer adalah komponen transparan yang tugasnya murni mengambil
        // ruang kosong. Di sini tingginya 24dp.
        Spacer(modifier = Modifier.height(24.dp))

        // --------------------------------------------------------------
        // Modul bagian J.f - Text "Tentang Jualan" lalu Spacer lagi
        // --------------------------------------------------------------
        Text(
            text = "Tentang Jualan",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --------------------------------------------------------------
        // Modul bagian J.g - Konten Text tentang aplikasi lalu Spacer
        // --------------------------------------------------------------
        Text(
            text = "Jualan adalah aplikasi yang membantu pelaku usaha kecil " +
                    "mencatat barang dagangan dan memantau penjualan hariannya " +
                    "langsung dari ponsel, tanpa perlu koneksi internet."
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --------------------------------------------------------------
        // Modul bagian J.h - Menambahkan Row
        // --------------------------------------------------------------
        // Row menyusun elemen di dalamnya secara HORIZONTAL, kiri ke kanan.
        //   .fillMaxWidth()               -> mengisi penuh lebar layar
        //   .background(Color(0xFFE0E0E0))-> abu-abu terang lewat kode heksa.
        //                                    Awalan 0xFF berarti 100% solid
        //                                    alias tidak transparan.
        //   .padding(16.dp)               -> jarak dalam 16dp di semua sisi
        //
        // PENTING soal urutan Modifier: .padding ditulis SESUDAH .background,
        // sehingga area padding ikut terwarnai abu-abu. Kalau dibalik,
        // bagian pinggirnya tidak ikut berwarna.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0E0E0))
                .padding(16.dp)
        ) {
            // ----------------------------------------------------------
            // Modul bagian J.i - Menambahkan Text di dalam Row
            // ----------------------------------------------------------
            // Modifier.weight() membagi ruang secara proporsional dan HANYA
            // bisa dipakai di dalam Row atau Column.
            // Total bobot = 1f + 2f = 3f, sehingga:
            //   teks pertama mendapat 1/3 (sekitar 33%) lebar Row
            //   teks kedua   mendapat 2/3 (sekitar 66%) lebar Row
            // Huruf f menandakan tipe data Float (angka desimal di Kotlin).
            Text(
                text = "Misi Kami:",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Memajukan UMKM Lokal",
                modifier = Modifier.weight(2f)
            )
        }
    }
}

// ============================================================================
// @Preview memungkinkan tampilan dilihat langsung di Android Studio tanpa
// menjalankan aplikasi di HP atau emulator. Klik "Split" di pojok kanan atas
// editor untuk melihatnya.
// ============================================================================
@Preview(showBackground = true)
@Composable
fun LayoutTentangJualanPreview() {
    JualanTheme {
        LayoutTentangJualan()
    }
}
