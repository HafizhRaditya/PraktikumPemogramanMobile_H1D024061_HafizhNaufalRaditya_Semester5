package com.pemmob1.h1d024061.network

import com.pemmob1.h1d024061.data.model.Category
import com.pemmob1.h1d024061.data.model.Product
import com.pemmob1.h1d024061.util.JualanConstants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// ============================================================================
// Modul Pertemuan 5 bagian F - Membuat API Interface dan API Client
// ============================================================================
// Lapisan jaringan aplikasi. Isinya dua hal:
//   - ApiInterface : DAFTAR permintaan yang bisa dikirim ke server
//   - ApiClient    : objek Retrofit yang benar-benar MENGIRIM permintaan itu
// ============================================================================

/**
 * Kontrak komunikasi dengan server.
 *
 * Yang ditulis hanya deklarasi fungsi tanpa isi. Retrofit yang membuatkan
 * isinya saat aplikasi berjalan, berdasarkan anotasi pada tiap fungsi.
 *
 * - `@GET("...")` : metode HTTP GET (mengambil data) ke endpoint tersebut.
 *   Endpoint disambung ke BASE_URL, jadi getProducts() memanggil
 *   https://pemmob-if.web.app/data/products.json
 * - `suspend`     : fungsi berjalan di dalam coroutine, sehingga menunggu
 *   jawaban server tidak membekukan layar.
 * - Nilai kembalian `List<Category>` / `List<Product>` adalah hasil ubahan
 *   JSON menjadi objek Kotlin, yang dikerjakan konverter Gson.
 */
interface ApiInterface {

    @GET("data/categories.json")
    suspend fun getCategories(): List<Category>

    @GET("data/products.json")
    suspend fun getProducts(): List<Product>
}

/**
 * Pintu tunggal untuk memanggil API.
 *
 * `object` menjadikannya Singleton, dan `by lazy` menunda pembuatan Retrofit
 * sampai `instance` pertama kali dipakai. Setelah itu hasilnya disimpan dan
 * dipakai ulang, karena membuat objek Retrofit berkali-kali itu boros.
 */
object ApiClient {

    val instance: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Gson mencocokkan nama kunci JSON dengan nama properti data
            // class. Itu sebabnya properti ditulis category_id dan
            // products_count, persis seperti di JSON.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}
