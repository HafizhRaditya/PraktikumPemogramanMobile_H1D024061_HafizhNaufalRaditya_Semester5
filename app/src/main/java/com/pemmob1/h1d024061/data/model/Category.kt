package com.pemmob1.h1d024061.data.model

// ============================================================================
// Modul Pertemuan 3 bagian B - Membuat Data Class Category
// ============================================================================
// Kata kunci `data` membuat Kotlin otomatis menyediakan fungsi pendukung
// (equals, hashCode, toString, copy) yang di Java harus ditulis manual.
//
// Semua properti memakai `val`, jadi nilainya tidak bisa diubah setelah objek
// dibuat (immutable). Tanda `?` menandai properti yang BOLEH bernilai null.
//
// Nama `products_count` sengaja memakai snake_case sesuai modul, mengikuti
// gaya penamaan kolom pada data yang biasa dikirim oleh API/database.
// ============================================================================

data class Category(
    // Nomor identitas unik kategori. Wajib diisi.
    val id: Int,
    // Nama kategori, contoh "Makanan". Wajib diisi.
    val name: String,
    // Penjelasan kategori. Boleh kosong (null).
    val description: String?,
    // Jumlah produk dalam kategori. Boleh null bila belum dihitung.
    val products_count: Int?,
)
