package com.pemmob1.h1d024061.data.model

// ============================================================================
// Modul Pertemuan 3 bagian C - Membuat Data Class Product
// ============================================================================
// Satu produk UMKM. Relasi ke kategori disimpan dua kali dengan tujuan
// berbeda:
//   - category_id -> angka, dipakai untuk MENYARING produk per kategori
//   - category    -> objek lengkap, dipakai untuk MENAMPILKAN nama kategori
//                    pada label kartu produk. Boleh null.
// ============================================================================

data class Product(
    val id: Int,
    val category_id: Int,
    val category: Category?,
    val name: String,
    val description: String?,
    // Double karena harga bisa memuat pecahan, contoh 15000.0
    val price: Double,
    val stock: Int,
    // Nama berkas gambar produk di res/drawable, contoh "dummy_product"
    val img: String,
)
