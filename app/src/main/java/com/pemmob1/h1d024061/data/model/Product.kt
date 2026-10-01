package com.pemmob1.h1d024061.data.model

// ============================================================================
// Modul Pertemuan 3 bagian C - Membuat Data Class Product
// ============================================================================
// Satu produk UMKM. Relasi ke kategori disimpan dua kali dengan tujuan
// berbeda:
//   - category_id -> angka, dipakai untuk MENYARING produk per kategori
//   - category    -> objek lengkap, dipakai untuk MENAMPILKAN nama kategori
//                    pada label kartu produk. Boleh null.
//
// Pertemuan 5: objek ini diisi Gson dari products.json. JSON itu tidak punya
// kunci "category", sehingga category bernilai null sampai ProductViewModel
// memasangkannya dengan kategori yang id-nya sama.
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
    // Nama berkas gambar produk. Sejak Pertemuan 5 nilainya datang dari API:
    // "dummy_product" berarti pakai gambar bawaan di res/drawable, selain itu
    // (contoh "produk.jpeg") gambar diunduh dari folder img/ di server.
    val img: String,
)
