package com.pemmob1.h1d024061.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pemmob1.h1d024061.R
import com.pemmob1.h1d024061.ui.theme.JualanTheme
import kotlinx.coroutines.launch

// ============================================================================
// Modul Pertemuan 2 bagian D  - Membuat Screen Baru
// Modul Pertemuan 4 bagian B  - Modifikasi Form Hubungi Kami
// ============================================================================
// Halaman formulir "Hubungi Kami".
//
// Pertemuan 4 memecah halaman ini menjadi DUA fungsi, dan inilah inti materi
// pertemuan ini:
//
//   1. HubungiKamiScreen()            -> STATEFUL. Memegang seluruh data dan
//      logika validasi, lalu menurunkannya lewat parameter.
//   2. StatelessFormHubungiKami()     -> STATELESS. Hanya menggambar, tidak
//      menyimpan data apa pun, sehingga mudah diuji dan dipakai ulang.
//
// Pemisahan itu bernama STATE HOISTING, dan aliran datanya mengikuti pola
// UNIDIRECTIONAL DATA FLOW (UDF): data turun lewat parameter biasa (email,
// isAgreed), kejadian naik lewat lambda (onEmailChange, onAgreedChange).
// ============================================================================

/**
 * Versi STATEFUL: pemilik seluruh state formulir.
 *
 * @param navController pemandu perpindahan halaman. Tipenya nullable (`?`)
 *        supaya fungsi ini tetap bisa dipanggil dari @Preview tanpa perlu
 *        menyediakan NavController sungguhan.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubungiKamiScreen(navController: NavController?) {

    // ------------------------------------------------------------------
    // Modul Pertemuan 4 bagian B.1 - Deklarasi Variabel
    // ------------------------------------------------------------------
    // mutableStateOf membuat data yang bisa memicu REKOMPOSISI: setiap kali
    // isinya berubah, Compose menggambar ulang bagian layar yang membacanya.
    //
    // remember          -> nilai bertahan selama rekomposisi, TETAPI hilang
    //                      saat Activity dibuat ulang (layar diputar).
    // rememberSaveable  -> nilai ikut disimpan ke SavedInstanceState, jadi
    //                      tetap ada setelah layar diputar. Dipakai untuk tipe
    //                      pesan dan centang persetujuan, seperti di modul.
    //
    // Kata kunci `by` adalah Property Delegation: variabel bisa dibaca dan
    // diisi seperti variabel biasa, tanpa menulis .value berulang kali.
    var emailText by remember { mutableStateOf("") }
    var messageText by remember { mutableStateOf("") }
    var problemType by rememberSaveable { mutableStateOf(PILIH_TIPE_PESAN) }
    var isAgreed by rememberSaveable { mutableStateOf(false) }

    // Uri = alamat berkas gambar yang dipilih pengguna. Bertipe nullable
    // karena pada awalnya belum ada gambar yang dipilih.
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // ------------------------------------------------------------------
    // Modul Pertemuan 4 bagian B.2 - Variabel validasi
    // ------------------------------------------------------------------
    // Ditulis sebagai `val` biasa, bukan state: nilainya dihitung ulang
    // sendiri setiap rekomposisi karena bergantung pada state di atas.
    val isEmailValid = emailText.contains("@") && emailText.isNotBlank()
    val isMessageValid = messageText.length >= 10
    val isFormValid = isEmailValid && isMessageValid && isAgreed &&
        problemType != PILIH_TIPE_PESAN

    // Snackbar butuh dua hal: penampung antrean pesan, dan jalur coroutine
    // karena showSnackbar adalah suspend function.
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Hubungi Kami") },
                navigationIcon = {
                    IconButton(
                        // popBackStack mengembalikan pengguna ke halaman
                        // sebelumnya, yaitu daftar produk.
                        onClick = { navController?.popBackStack() },
                    ) {
                        Icon(
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
    ) { paddingValues ->

        // ------------------------------------------------------------------
        // Modul Pertemuan 4 bagian B.12 - Pemanggilan fungsi stateless
        // ------------------------------------------------------------------
        // Semua state diturunkan sebagai nilai, dan setiap perubahan kembali
        // ke atas lewat lambda. Layar di bawah sama sekali tidak menyimpan
        // data - itulah yang membuatnya "stateless".
        StatelessFormHubungiKami(
            modifier = Modifier.padding(paddingValues),
            email = emailText,
            onEmailChange = { emailText = it },
            isEmailValid = isEmailValid,
            message = messageText,
            onMessageChange = { messageText = it },
            isMessageValid = isMessageValid,
            problemType = problemType,
            onProblemTypeChange = { problemType = it },
            isAgreed = isAgreed,
            onAgreedChange = { isAgreed = it },
            imageUri = imageUri,
            onImagePicked = { imageUri = it },
            isFormValid = isFormValid,
            onSubmit = {
                scope.launch {
                    snackbarHostState.showSnackbar("Pesan Terkirim!")
                }
            },
        )
    }
}

/** Teks awal dropdown, sekaligus penanda "pengguna belum memilih apa pun". */
private const val PILIH_TIPE_PESAN = "Pilih Tipe Pesan"

/**
 * Versi STATELESS: hanya menggambar formulir.
 *
 * Perhatikan pola parameternya yang selalu berpasangan: satu nilai untuk
 * ditampilkan (email), satu lambda untuk melaporkan perubahan (onEmailChange).
 * Fungsi ini tidak pernah mengubah data sendiri, hanya melapor ke atas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessFormHubungiKami(
    modifier: Modifier = Modifier,
    email: String, onEmailChange: (String) -> Unit, isEmailValid: Boolean,
    message: String, onMessageChange: (String) -> Unit, isMessageValid: Boolean,
    problemType: String, onProblemTypeChange: (String) -> Unit,
    isAgreed: Boolean, onAgreedChange: (Boolean) -> Unit,
    imageUri: Uri?, onImagePicked: (Uri?) -> Unit,
    isFormValid: Boolean, onSubmit: () -> Unit,
) {

    // ------------------------------------------------------------------
    // Modul Pertemuan 4 bagian B.4 - Launcher PhotoPicker
    // ------------------------------------------------------------------
    // rememberLauncherForActivityResult mendaftarkan "pintu" ke galeri foto
    // dan mengingatnya supaya tidak dibuat ulang setiap rekomposisi.
    // Kontrak PickVisualMedia memakai pemilih foto bawaan sistem, sehingga
    // aplikasi tidak perlu meminta izin penuh ke seluruh penyimpanan.
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> onImagePicked(uri) },
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        Text(
            text = "Hubungi Kami",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Start),
        )

        // ------------------------------------------------------------------
        // Modul Pertemuan 4 bagian B.8 - Email beserta validasinya
        // ------------------------------------------------------------------
        // isError mewarnai bingkai menjadi merah, supportingText menampilkan
        // pesan di bawah kolom. Keduanya membaca nilai yang sama, jadi warna
        // dan pesan mustahil bertentangan.
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email Anda") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.mail_icon),
                    contentDescription = "Email",
                )
            },
            // Pesan kesalahan baru muncul setelah pengguna mengetik sesuatu,
            // bukan saat kolom masih kosong sejak awal.
            isError = email.isNotEmpty() && !isEmailValid,
            supportingText = {
                if (email.isNotEmpty() && !isEmailValid) Text("Format Email Salah")
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
        )

        // ------------------------------------------------------------------
        // Modul Pertemuan 4 bagian B.9 - Dropdown tipe pesan
        // ------------------------------------------------------------------
        // expanded menentukan menu sedang terbuka atau tertutup; options berisi
        // daftar pilihannya.
        var expanded by remember { mutableStateOf(false) }
        val options = listOf("Pertanyaan", "Keluhan", "Saran")

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            OutlinedTextField(
                // readOnly: pengguna memilih dari daftar, tidak mengetik sendiri.
                readOnly = true,
                value = problemType,
                onValueChange = { },
                label = { Text("Tipe Pesan") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                // menuAnchor mengaitkan posisi menu ke kolom teks ini.
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption) },
                        onClick = {
                            // Pilihan dilaporkan ke induk, lalu menu ditutup.
                            onProblemTypeChange(selectionOption)
                            expanded = false
                        },
                    )
                }
            }
        }

        // Kolom pesan. Tingginya ditambah supaya cukup untuk kalimat panjang.
        OutlinedTextField(
            value = message,
            onValueChange = onMessageChange,
            label = { Text("Pesan") },
            isError = message.isNotEmpty() && !isMessageValid,
            supportingText = {
                if (!isMessageValid) Text("Pesan minimal 10 karakter")
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
        )

        // Tombol pembuka galeri. PickVisualMediaRequest membatasi pilihan
        // hanya ke gambar, bukan video.
        OutlinedButton(
            onClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_check),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Unggah Bukti (Screenshot / Foto)")
        }

        // ------------------------------------------------------------------
        // Modul Pertemuan 4 bagian B.10 - Konfirmasi berkas dan persetujuan
        // ------------------------------------------------------------------
        // Percabangan di dalam Composable: Card hanya digambar bila pengguna
        // sudah benar-benar memilih gambar (imageUri tidak null).
        if (imageUri != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_check),
                        contentDescription = "File",
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // lastPathSegment mengambil bagian terakhir alamat berkas,
                    // yaitu nama berkasnya saja.
                    Text("File terpilih: ${imageUri.lastPathSegment}")
                }
            }
        }

        // Checkbox stateless: status centang dibaca dari isAgreed, dan setiap
        // sentuhan dilaporkan ke induk lewat onAgreedChange.
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = isAgreed, onCheckedChange = onAgreedChange)
            Text("Saya menyetujui syarat & ketentuan")
        }

        // ------------------------------------------------------------------
        // Modul Pertemuan 4 bagian B.11 - Tombol kirim
        // ------------------------------------------------------------------
        // enabled = isFormValid: tombol baru hidup setelah email benar, pesan
        // minimal 10 karakter, tipe pesan dipilih, dan syarat dicentang.
        Button(
            onClick = onSubmit,
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.send_icon),
                    contentDescription = "Send",
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Kirim Pesan", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

/**
 * Pratinjau formulir. Karena versi stateless tidak butuh ViewModel maupun
 * NavController, data contoh cukup ditulis langsung di sini.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewFormHubungiKami() {
    JualanTheme {
        StatelessFormHubungiKami(
            email = "hafizh@unsoed.ac.id",
            onEmailChange = {},
            isEmailValid = true,
            message = "Halo, saya ingin bertanya soal ketersediaan produk.",
            onMessageChange = {},
            isMessageValid = true,
            problemType = "Pertanyaan",
            onProblemTypeChange = {},
            isAgreed = true,
            onAgreedChange = {},
            imageUri = null,
            onImagePicked = {},
            isFormValid = true,
            onSubmit = {},
        )
    }
}
