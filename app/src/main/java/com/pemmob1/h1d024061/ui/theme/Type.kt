package com.pemmob1.h1d024061.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ============================================================================
// Modul Pertemuan 2 bagian C - Modifikasi Tipografi
// ============================================================================
// Kode bawaan Android Studio sudah dihapus, diganti skala tipografi kustom.
//
// Lima hal yang diatur di tiap gaya, sesuai penjelasan modul:
//   fontFamily    - jenis huruf. SansSerif = huruf standar Android tanpa kait
//   fontWeight    - ketebalan: Bold, SemiBold, Medium, Normal
//   fontSize      - ukuran dalam sp (BUKAN dp), supaya ikut membesar mengikuti
//                   pengaturan ukuran huruf di sistem HP pengguna
//   lineHeight    - jarak antar baris, agar paragraf tidak bertumpuk
//   letterSpacing - jarak antar huruf. Judul besar dirapatkan (nilai minus),
//                   teks kecil direnggangkan agar mudah dibaca
//
// Seluruh gaya ini diakses lewat MaterialTheme.typography, sehingga tidak
// perlu menulis fontSize satu per satu di setiap layar.
// ============================================================================

val Typography = Typography(

    // --- HEADLINE: judul halaman, elemen paling menonjol ---
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        // Dirapatkan karena huruf berukuran besar terlihat terlalu renggang
        // bila memakai jarak normal.
        letterSpacing = (-0.5).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.25).sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),

    // --- TITLE: judul kartu atau bagian, lebih kecil dari headline ---
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),

    // --- BODY: isi paragraf dan deskripsi ---
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),

    // --- LABEL: teks pada tombol, chip, dan keterangan kecil ---
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        // Direnggangkan karena huruf kecil justru lebih sulit dibaca kalau
        // jaraknya rapat.
        letterSpacing = 0.5.sp,
    ),
)
