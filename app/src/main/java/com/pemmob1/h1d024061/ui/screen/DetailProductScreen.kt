package com.pemmob1.h1d024061.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pemmob1.h1d024061.R
import com.pemmob1.h1d024061.data.model.Category
import com.pemmob1.h1d024061.data.model.Product
import com.pemmob1.h1d024061.ui.theme.JualanTheme
import com.pemmob1.h1d024061.ui.viewmodel.ProductUiState
import com.pemmob1.h1d024061.ui.viewmodel.ProductViewModel
import com.pemmob1.h1d024061.util.JualanConstants.BASE_URL

// ============================================================================
// Modul Pertemuan 4 bagian D - Membuat Tampilan Detail Produk
// Modul Pertemuan 5 bagian H dan J - Data dari ViewModel dan gambar Coil
// ============================================================================
// Halaman detail satu produk. Sama seperti daftar produk, halaman ini dibagi
// dua: DetailProductScreen (stateful, membaca ViewModel) dan
// StatelessDetailProduct (stateless, hanya menggambar).
//
// Halaman ini juga menunjukkan cara data berpindah antarlayar: yang dikirim
// lewat rute "detail/{productId}" hanya id-nya. Data lengkap produk dicari
// lagi di ViewModel yang sama dengan halaman daftar, jadi tidak ada unduhan
// kedua.
// ============================================================================

/**
 * Versi STATEFUL.
 *
 * @param productId id produk yang dikirim dari halaman daftar produk.
 * @param navController pengendali navigasi, dipakai untuk tombol kembali.
 * @param viewModel sumber data produk, dikirim dari HomeActivity.
 */
@Composable
fun DetailProductScreen(productId: Int, navController: NavController?, viewModel: ProductViewModel) {

    // Context dibutuhkan untuk memunculkan Toast saat tombol keranjang ditekan.
    val context = LocalContext.current

    // Jumlah beli memakai rememberSaveable supaya angka yang sudah dipilih
    // tidak kembali ke 1 ketika layar diputar. Dipakai mutableIntStateOf,
    // bukan mutableStateOf seperti di modul, karena lint menyarankannya untuk
    // angka: versi Int menghindari autoboxing (membungkus Int menjadi objek).
    var quantity by rememberSaveable { mutableIntStateOf(1) }

    // Modul Pertemuan 5 bagian H: state dibaca dari ViewModel. LaunchedEffect
    // dan delay(1000) yang dulu meniru lambatnya server sudah dihapus, karena
    // sekarang status memuat datang dari permintaan jaringan yang sungguhan.
    val uiState by viewModel.uiState.collectAsState()

    // Surface memberi warna latar dan warna teks sesuai tema untuk tampilan
    // Loading, Error, dan "tidak ditemukan" yang tidak memakai Scaffold.
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

        when (val state = uiState) {

            is ProductUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is ProductUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Error: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }

            is ProductUiState.Success -> {
                // find mengembalikan produk pertama yang cocok, atau null bila
                // tidak ada. Karena itu tipe product adalah Product?.
                val product = state.products.find { it.id == productId }

                if (product == null) {
                    // Id yang dikirim tidak cocok dengan produk mana pun.
                    // Lebih baik memberi tahu pengguna daripada layar kosong.
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Produk tidak ditemukan.")
                    }
                } else {
                    StatelessDetailProduct(
                        product = product,
                        quantity = quantity,
                        onQuantityChange = { quantity = it },
                        onBackClick = { navController?.popBackStack() },
                        onAddToCartClick = {
                            Toast.makeText(context, "Membeli sebanyak $quantity", Toast.LENGTH_SHORT).show()
                        },
                    )
                }
            }
        }
    }
}

/**
 * Versi STATELESS: menerima data jadi, tidak menyimpan apa pun.
 *
 * Parameter isLoading dari Pertemuan 4 sudah dihapus: urusan memuat dan gagal
 * kini ditangani `when` di DetailProductScreen, jadi fungsi ini hanya dipanggil
 * ketika datanya sudah ada.
 *
 * @param product produk yang ditampilkan.
 * @param quantity jumlah beli yang sedang dipilih.
 * @param onQuantityChange dipanggil saat tombol - atau + ditekan.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessDetailProduct(
    product: Product?, quantity: Int,
    onQuantityChange: (Int) -> Unit, onBackClick: () -> Unit, onAddToCartClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Produk") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_icon),
                            contentDescription = "Back",
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

        if (product != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    // verticalScroll membuat isi halaman bisa digulir bila
                    // tingginya melebihi layar.
                    .verticalScroll(rememberScrollState()),
            ) {
                // Modul Pertemuan 5 bagian J - sumber gambar. Bila nama
                // berkasnya "dummy_product", dipakai gambar bawaan aplikasi;
                // selain itu gambar diunduh dari folder img/ di server.
                // BASE_URL sudah diakhiri "/", jadi tidak ditambah garis
                // miring lagi (kalau dobel, server menjawab dengan redirect).
                val imageModel: Any = if (product.img == "dummy_product") {
                    R.drawable.dummy_product
                } else {
                    BASE_URL + "img/${product.img}"
                }

                Box(modifier = Modifier.fillMaxWidth()) {

                    // AsyncImage (Coil) mengunduh gambar di background. Karena
                    // gambar yang sama sudah diunduh di halaman daftar, di
                    // sini Coil mengambilnya dari cache.
                    AsyncImage(
                        model = imageModel,
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White),
                        contentScale = ContentScale.Fit,
                    )

                    // Label kategori di pojok kanan atas gambar. Objek
                    // category diisi ViewModel lewat product.copy(...), bukan
                    // berasal dari JSON produk.
                    if (product.category != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.secondary),
                        ) {
                            Text(
                                text = product.category.name,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            )
                        }
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Rp ${product.price}",
                        style = MaterialTheme.typography.titleLarge,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Deskripsi", fontWeight = FontWeight.Bold)
                    // Elvis operator ?: memberi teks kosong bila deskripsi
                    // bernilai null, sehingga aplikasi tidak crash.
                    Text(text = product.description ?: "")
                    Text(text = "Stok: ${product.stock}")

                    Spacer(modifier = Modifier.height(24.dp))

                    // Pengatur jumlah beli. Tombol dinonaktifkan di batasnya:
                    // tidak bisa kurang dari 1, dan tidak bisa melebihi stok.
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Jumlah Beli")

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FilledTonalIconButton(
                                onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                                enabled = quantity > 1,
                            ) { Text("-") }

                            Text(
                                text = quantity.toString(),
                                modifier = Modifier.padding(horizontal = 16.dp),
                            )

                            FilledTonalIconButton(
                                onClick = { if (quantity < product.stock) onQuantityChange(quantity + 1) },
                                enabled = quantity < product.stock,
                            ) { Text("+") }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onAddToCartClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = product.stock > 0 && quantity > 0,
                    ) {
                        Text("Tambah ke Keranjang")
                    }
                }
            }
        }
    }
}

/**
 * Pratinjau halaman detail.
 *
 * Modul Pertemuan 5 bagian B: DummyData sudah dihapus, dan pratinjau tidak
 * bisa memanggil API. Karena itu dipakai satu produk contoh yang hanya hidup
 * di pratinjau ini.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDetailProduct() {
    JualanTheme {
        StatelessDetailProduct(
            product = Product(
                id = 1,
                category_id = 1,
                category = Category(1, "Makanan", "Aneka Makanan Lokal", 5),
                name = "Kripik Singkong",
                description = "Kripik gurih",
                price = 15000.0,
                stock = 50,
                img = "dummy_product",
            ),
            quantity = 1,
            onQuantityChange = {},
            onBackClick = {},
            onAddToCartClick = {},
        )
    }
}
