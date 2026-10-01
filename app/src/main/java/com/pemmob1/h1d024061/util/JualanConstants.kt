package com.pemmob1.h1d024061.util

// ============================================================================
// Modul Pertemuan 5 bagian C - Membuat Constant
// ============================================================================
// Tempat menyimpan nilai tetap yang dipakai di banyak berkas. Dengan menaruh
// alamat server di satu tempat, pindah server cukup mengubah satu baris ini.
//
// `object` membuat JualanConstants menjadi Singleton, dan `const val` berarti
// nilainya sudah pasti sejak kompilasi (compile-time constant).
// ============================================================================

object JualanConstants {
    // Base URL: alamat dasar server. Retrofit mewajibkan alamat ini diakhiri
    // garis miring "/". Alamat lengkap sebuah data dibentuk dari
    // BASE_URL + endpoint, contohnya BASE_URL + "data/products.json".
    const val BASE_URL = "https://pemmob-if.web.app/"
}
