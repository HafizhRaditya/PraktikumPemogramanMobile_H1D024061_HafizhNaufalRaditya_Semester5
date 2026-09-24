package com.pemmob1.h1d024061.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pemmob1.h1d024061.R
import com.pemmob1.h1d024061.data.dummy.DummyData
import com.pemmob1.h1d024061.data.model.Product
import com.pemmob1.h1d024061.ui.theme.JualanTheme
import kotlinx.coroutines.delay

// ============================================================================
// Modul Pertemuan 4 bagian D - Membuat Tampilan Detail Produk
// ============================================================================
// Halaman detail satu produk. Sama seperti daftar produk, halaman ini dibagi
// dua: DetailProductScreen (stateful, memegang data) dan
// StatelessDetailProduct (stateless, hanya menggambar).
//
// Halaman ini juga menunjukkan cara data berpindah antarlayar: id produk
// dikirim lewat rute "detail/{productId}", lalu dipakai untuk mencari produk
// yang sesuai di DummyData.
// ============================================================================

/**
 * Versi STATEFUL.
 *
 * @param productId id produk yang dikirim dari halaman daftar produk.
 * @param navController pengendali navigasi, dipakai untuk tombol kembali.
 */
@Composable
fun DetailProductScreen(productId: Int, navController: NavController?) {

    // Context dibutuhkan untuk memunculkan Toast saat tombol keranjang ditekan.
    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(true) }
    var product by remember { mutableStateOf<Product?>(null) }
    // Jumlah beli memakai rememberSaveable supaya angka yang sudah dipilih
    // tidak kembali ke 1 ketika layar diputar. Dipakai mutableIntStateOf,
    // bukan mutableStateOf seperti di modul, karena lint menyarankannya untuk
    // angka: versi Int menghindari autoboxing (membungkus Int menjadi objek).
    var quantity by rememberSaveable { mutableIntStateOf(1) }

    // LaunchedEffect dengan kunci productId: blok ini dijalankan sekali saat
    // halaman dibuka, dan diulang hanya bila id produknya berganti.
    // delay(1000) meniru lambatnya server sehingga indikator loading terlihat.
    LaunchedEffect(productId) {
        isLoading = true
        delay(1000)
        // find mengembalikan produk pertama yang cocok, atau null bila tidak
        // ada. Karena itu tipe product adalah Product? (nullable).
        product = DummyData.products.find { it.id == productId }
        isLoading = false
    }

    StatelessDetailProduct(
        product = product,
        isLoading = isLoading,
        quantity = quantity,
        onQuantityChange = { quantity = it },
        onBackClick = { navController?.popBackStack() },
        onAddToCartClick = {
            Toast.makeText(context, "Dimasukkan: $quantity", Toast.LENGTH_SHORT).show()
        },
    )
}

/**
 * Versi STATELESS: menerima data jadi, tidak menyimpan apa pun.
 *
 * @param product boleh null, yaitu ketika data belum ditemukan.
 * @param quantity jumlah beli yang sedang dipilih.
 * @param onQuantityChange dipanggil saat tombol - atau + ditekan.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessDetailProduct(
    product: Product?, isLoading: Boolean, quantity: Int,
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

        // Tiga kemungkinan kondisi layar: sedang memuat, data ketemu, dan
        // data tidak ketemu. Ketiganya ditulis sebagai percabangan biasa.
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (product != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    // verticalScroll membuat isi halaman bisa digulir bila
                    // tingginya melebihi layar.
                    .verticalScroll(rememberScrollState()),
            ) {
                val imageRes = if (product.img == "dummy_product") {
                    R.drawable.dummy_product
                } else {
                    R.drawable.dummy_product
                }

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                )

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
        } else {
            // Kondisi terakhir: id yang dikirim tidak cocok dengan produk mana
            // pun. Lebih baik memberi tahu pengguna daripada layar kosong.
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Produk tidak ditemukan.")
            }
        }
    }
}

/** Pratinjau halaman detail dengan produk contoh dari DummyData. */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDetailProduct() {
    JualanTheme {
        StatelessDetailProduct(
            product = DummyData.products[0],
            isLoading = false,
            quantity = 1,
            onQuantityChange = {},
            onBackClick = {},
            onAddToCartClick = {},
        )
    }
}
