package com.pemmob1.h1d024061.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pemmob1.h1d024061.R
import kotlinx.coroutines.launch

// ============================================================================
// Modul Pertemuan 2 bagian D - Membuat Screen Baru
// ============================================================================
// Halaman formulir "Hubungi Kami".
//
// Ini adalah Top-Level Function: fungsi yang dideklarasikan langsung di dalam
// berkas tanpa dibungkus class. Di Kotlin hal itu diperbolehkan, dan justru
// jadi kebiasaan umum saat menulis Composable.
// ============================================================================

/**
 * @param navController pemandu perpindahan halaman. Tipenya nullable (`?`)
 *        supaya fungsi ini tetap bisa dipanggil dari @Preview tanpa perlu
 *        menyediakan NavController sungguhan.
 */
// @OptIn memberi izin eksplisit memakai API Material 3 yang masih berlabel
// eksperimental, dalam hal ini TopAppBar.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubungiKamiScreen(navController: NavController?) {

    // ------------------------------------------------------------------
    // Modul bagian D.8 - Deklarasi Variabel
    // ------------------------------------------------------------------
    // mutableStateOf menciptakan data yang dapat memicu pembaruan layar:
    // setiap kali isinya berubah, Compose menggambar ulang bagian yang
    // memakainya. Proses menggambar ulang itu disebut REKOMPOSISI.
    //
    // remember menjaga agar teks yang sudah diketik tidak hilang saat
    // rekomposisi terjadi. Tanpa remember, tulisan pengguna akan terhapus
    // setiap kali layar diperbarui.
    //
    // Kata kunci `by` adalah pintasan Kotlin agar variabel ini bisa dibaca
    // dan diubah seperti variabel biasa, tanpa menulis .value berulang kali.
    var emailText by remember { mutableStateOf("") }
    var pesanText by remember { mutableStateOf("") }

    // ------------------------------------------------------------------
    // Modul bagian G - Membuat SnackBar
    // ------------------------------------------------------------------
    // snackbarHostState adalah "manajer pengontrol" yang menyimpan antrean
    // pesan dan mengatur kapan Snackbar tampil. Dibungkus remember supaya
    // statusnya tidak ter-reset saat rekomposisi.
    val snackbarHostState = remember { SnackbarHostState() }

    // scope adalah jalur eksekusi di latar belakang. Menampilkan Snackbar
    // adalah suspend function - ia menahan waktu beberapa detik - sehingga
    // tidak boleh dijalankan langsung di antarmuka utama. Kalau dipaksakan,
    // aplikasi akan macet dan layar berhenti merespons sentuhan.
    val scope = rememberCoroutineScope()

    // ------------------------------------------------------------------
    // Modul bagian D.9 - Membuat Scaffold
    // ------------------------------------------------------------------
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            // --------------------------------------------------------------
            // Modul bagian D.10 - Membuat Topbar
            // --------------------------------------------------------------
            TopAppBar(
                title = { Text("Hubungi Kami") },
                navigationIcon = {
                    IconButton(
                        // popBackStack mengembalikan pengguna ke halaman
                        // sebelumnya, yaitu BasicInfoScreen.
                        onClick = { navController?.popBackStack() }
                    ) {
                        Icon(
                            // painterResource memanggil gambar ikon yang
                            // tersimpan di res/drawable.
                            painter = painterResource(id = R.drawable.back_icon),
                            contentDescription = "Kembali ke halaman sebelumnya",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { innerPadding ->

        // ------------------------------------------------------------------
        // Modul bagian D.11 - Membuat Column
        // ------------------------------------------------------------------
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            // --------------------------------------------------------------
            // Modul bagian D.12 - Menambahkan Teks dan OutlinedTextField
            // --------------------------------------------------------------
            Text(
                text = "Kirim Pesan kepada Kami",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = "Ada pertanyaan atau masukan? Isi formulir di bawah ini " +
                    "dan kami akan segera membalasnya.",
                style = MaterialTheme.typography.bodyMedium,
            )

            // Kotak isian pertama: email, dilengkapi ikon surat di sisi kiri.
            // Setiap huruf yang diketik langsung ditangkap dan disimpan ke
            // dalam variabel emailText lewat onValueChange.
            OutlinedTextField(
                value = emailText,
                onValueChange = { emailText = it },
                label = { Text("Email Anda") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.mail_icon),
                        contentDescription = null,
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
            )

            // Kotak isian kedua: pesan. Sengaja ditarik lebih tinggi (120.dp)
            // agar pengguna punya ruang cukup untuk menulis kalimat panjang.
            OutlinedTextField(
                value = pesanText,
                onValueChange = { pesanText = it },
                label = { Text("Pesan") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
            )

            // --------------------------------------------------------------
            // Modul bagian D.13 - Menambahkan Button
            // Modul bagian G.2 - Memanggil showSnackbar pada Button
            // --------------------------------------------------------------
            Button(
                onClick = {
                    // showSnackbar adalah suspend function, jadi WAJIB
                    // dibungkus scope.launch agar dijalankan di latar
                    // belakang. Tanpa itu, kode ini bahkan tidak akan
                    // ter-compile.
                    scope.launch {
                        snackbarHostState.showSnackbar("Pesan Terkirim")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                // Isi tombol disusun dengan Row supaya ikon dan teks berjajar
                // mendatar, dan diseimbangkan tepat di tengah.
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.send_icon),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        text = "Kirim Pesan",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        }
    }
}
