package com.pemmob1.h1d024061.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob1.h1d024061.data.model.Category
import com.pemmob1.h1d024061.data.model.Product
import com.pemmob1.h1d024061.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ============================================================================
// Modul Pertemuan 5 bagian G - Membuat View Model
// ============================================================================
// ViewModel adalah lapisan di antara data (API) dan tampilan (Composable).
// Ia memegang state layar dan bertahan hidup saat layar diputar, sehingga data
// tidak diunduh ulang hanya karena konfigurasi berubah.
//
// Alurnya satu arah (Unidirectional Data Flow):
//   API -> ViewModel mengubah uiState -> Composable membaca uiState -> layar
// ============================================================================

/**
 * Semua kemungkinan kondisi layar yang datanya berasal dari server.
 *
 * `sealed interface` membatasi turunannya hanya pada tiga kondisi di bawah.
 * Karena daftarnya tertutup, `when` di sisi UI dipaksa menangani ketiganya dan
 * tidak butuh cabang `else`.
 */
sealed interface ProductUiState {

    /** Permintaan sedang berjalan, belum ada jawaban. Tidak membawa data. */
    object Loading : ProductUiState

    /** Server menjawab dengan benar; membawa data siap tampil. */
    data class Success(
        val categories: List<Category>,
        val products: List<Product>,
    ) : ProductUiState

    /** Permintaan gagal; membawa pesan untuk ditampilkan ke pengguna. */
    data class Error(val message: String) : ProductUiState
}

class ProductViewModel : ViewModel() {

    // Pola "backing property": _uiState bisa diubah, tetapi private sehingga
    // hanya ViewModel yang boleh menulis. uiState versi publik bertipe
    // StateFlow (hanya-baca), jadi Composable tidak bisa mengubahnya langsung.
    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    // init berjalan sekali saat ViewModel dibuat, jadi data langsung diminta
    // begitu aplikasi dibuka.
    init {
        fetchData()
    }

    private fun fetchData() {
        // viewModelScope: coroutine otomatis dibatalkan ketika ViewModel
        // dihancurkan, sehingga tidak ada permintaan yang menggantung.
        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading

            // Urusan jaringan bisa gagal kapan saja (tidak ada sinyal, server
            // mati, JSON rusak). try-catch mengubah kegagalan itu menjadi
            // state Error, bukan membuat aplikasi crash.
            try {
                val categoriesResponse = ApiClient.instance.getCategories()
                val productsResponse = ApiClient.instance.getProducts()

                // JSON produk hanya membawa category_id (angka). Di sini tiap
                // produk dipasangkan dengan objek kategorinya, supaya UI bisa
                // menampilkan nama kategori. copy() membuat salinan produk
                // dengan satu properti diganti, karena data class immutable.
                val mappedProducts = productsResponse.map { product ->
                    val matchedCategory = categoriesResponse.find { it.id == product.category_id }
                    product.copy(category = matchedCategory)
                }

                _uiState.value = ProductUiState.Success(
                    categories = categoriesResponse,
                    products = mappedProducts,
                )
            } catch (e: Exception) {
                _uiState.value = ProductUiState.Error(
                    message = "Gagal memuat data: ${e.localizedMessage}",
                )
            }
        }
    }
}
