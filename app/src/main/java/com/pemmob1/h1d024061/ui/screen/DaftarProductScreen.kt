package com.pemmob1.h1d024061.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pemmob1.h1d024061.R
import com.pemmob1.h1d024061.data.dummy.DummyData
import com.pemmob1.h1d024061.data.model.Category
import com.pemmob1.h1d024061.data.model.Product
import com.pemmob1.h1d024061.ui.theme.JualanTheme
import kotlinx.coroutines.delay

// ============================================================================
// Modul Pertemuan 3 bagian E sampai H - Dynamic Lists with Lazy Layouts
// Modul Pertemuan 4 bagian C dan F  - Recomposition, pencarian, dan Menu Action
// ============================================================================
// Halaman daftar produk UMKM: kolom pencarian, deretan kategori yang bisa
// digeser ke samping (LazyRow), dan kisi produk dua kolom (LazyVerticalGrid).
//
// "Lazy" berarti hanya item yang sedang terlihat di layar yang digambar. Item
// yang sudah keluar layar dibuang dari memori, sehingga daftar berisi ribuan
// produk pun tetap ringan. Column/Row biasa akan menggambar SEMUA item
// sekaligus, termasuk yang tidak terlihat.
// ============================================================================

/**
 * Modul Pertemuan 3 bagian E - kartu untuk satu produk.
 *
 * @param product data produk yang ditampilkan.
 * @param onClick aksi saat kartu ditekan. Bertipe `() -> Unit`, yaitu fungsi
 *        tanpa masukan dan tanpa nilai kembalian (Lambda / Higher-Order
 *        Function). Kartu ini tidak tahu apa yang akan terjadi; pemanggilnya
 *        yang menentukan, misalnya membuka halaman detail.
 */
@Composable
fun ProductItemCard(product: Product, onClick: () -> Unit) {
    // E.2 - Card sebagai wadah kartu produk
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        // E.3 - Column dan Box di dalam Card
        Column(modifier = Modifier.padding(12.dp)) {

            // if-else di Kotlin adalah EKSPRESI: hasilnya bisa langsung
            // disimpan ke variabel tanpa membuat variabel kosong lebih dulu.
            // Semua produk dummy memakai gambar yang sama, jadi kedua cabang
            // mengembalikan gambar cadangan yang sama.
            val imageRes = if (product.img == "dummy_product") R.drawable.dummy_product else R.drawable.dummy_product

            Box(modifier = Modifier.fillMaxWidth()) {

                // E.4 - Gambar produk berbentuk persegi (rasio 1:1)
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White),
                    contentScale = ContentScale.Fit,
                )

                // Label kategori di pojok kanan atas gambar. Karena category
                // bertipe Category? (boleh null), label hanya digambar bila
                // datanya ada.
                if (product.category != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.secondary),
                    ) {
                        Text(
                            text = product.category.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        )
                    }
                }
            }

            // E.5 - Spacer dan Text, di luar Box
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                // Nama yang terlalu panjang dipotong dengan "..." supaya
                // tinggi semua kartu dalam satu baris tetap sama.
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Rp ${product.price}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

/**
 * Modul Pertemuan 3 bagian F - satu tombol kategori.
 *
 * @param isSelected penanda apakah kategori ini sedang dipilih. Nilai inilah
 *        yang menentukan warna kartu.
 */
@Composable
fun CategoryItem(category: Category, isSelected: Boolean, onClick: () -> Unit) {
    // Kategori terpilih memakai warna utama yang mencolok; yang lain memakai
    // warna netral yang lebih pudar.
    val containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = Modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
    ) {
        Text(
            text = category.name,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontWeight = FontWeight.Medium,
        )
    }
}

/**
 * Modul Pertemuan 4 bagian C - halaman daftar produk, versi STATEFUL.
 *
 * Tugasnya hanya memegang state dan menyiapkan data:
 *   - kategori yang dipilih dan kata kunci pencarian
 *   - status memuat (isLoading) dan hasil penyaringan (filteredProducts)
 *
 * Seluruh urusan menggambar diserahkan ke StatelessDaftarProduct.
 *
 * @param navController pengendali navigasi. Nullable supaya @Preview tetap
 *        bisa memanggil fungsi ini tanpa NavController sungguhan.
 */
@Composable
fun DaftarProdukScreen(navController: NavController? = null) {

    // rememberSaveable: pilihan kategori dan kata kunci pencarian tetap ada
    // setelah layar diputar. isLoading dan filteredProducts cukup remember,
    // karena keduanya dihitung ulang oleh LaunchedEffect di bawah.
    var selectedCategoryId by rememberSaveable { mutableStateOf(DummyData.categories.firstOrNull()?.id) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var filteredProducts by remember { mutableStateOf(emptyList<Product>()) }

    // ------------------------------------------------------------------
    // Modul Pertemuan 4 bagian C.2 - LaunchedEffect (proses asinkron)
    // ------------------------------------------------------------------
    // LaunchedEffect menjalankan coroutine di dalam Composable. Blok ini
    // dijalankan ulang setiap kali salah satu kuncinya berubah, yaitu ketika
    // pengguna berganti kategori atau mengetik di kolom pencarian.
    //
    // delay(1000) adalah suspend function: ia menunda coroutine ini saja,
    // bukan membekukan layar. Di sini dipakai untuk meniru lambatnya
    // pengambilan data dari server, supaya indikator loading terlihat.
    LaunchedEffect(selectedCategoryId, searchQuery) {
        isLoading = true

        delay(1000)

        val filteredByCategory = if (selectedCategoryId != null) {
            DummyData.products.filter { it.category_id == selectedCategoryId }
        } else {
            DummyData.products
        }

        // Penyaringan bertingkat: hasil saringan kategori disaring lagi dengan
        // kata kunci. ignoreCase = true agar "kripik" dan "Kripik" sama saja.
        filteredProducts = if (searchQuery.isBlank()) {
            filteredByCategory
        } else {
            filteredByCategory.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }

        isLoading = false
    }

    // Modul bagian C.10 - memanggil versi stateless. Data turun lewat
    // parameter, kejadian naik lewat lambda.
    StatelessDaftarProduct(
        categories = DummyData.categories,
        selectedCategoryId = selectedCategoryId,
        onCategorySelected = { selectedCategoryId = it },
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        isLoading = isLoading,
        products = filteredProducts,
        // Alamat rute dibentuk dengan menyisipkan id produk, lalu dibaca lagi
        // oleh NavHost di HomeActivity sebagai argumen productId.
        onProductClick = { product -> navController?.navigate("detail/${product.id}") },
        onContactUsClick = { navController?.navigate("hubungi_kami") },
    )
}

/**
 * Modul Pertemuan 4 bagian C.3 - halaman daftar produk, versi STATELESS.
 *
 * Fungsi ini tidak menyimpan data sama sekali; ia hanya menggambar apa yang
 * diberikan dan melaporkan sentuhan pengguna ke atas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessDaftarProduct(
    categories: List<Category>,
    selectedCategoryId: Int?,
    onCategorySelected: (Int) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isLoading: Boolean,
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    onContactUsClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Daftar Produk UMKM",
                        fontWeight = FontWeight.Bold,
                    )
                },
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.cart_icon),
                        contentDescription = "Keranjang belanja",
                    )

                    // --------------------------------------------------------
                    // Modul Pertemuan 4 bagian F - Menu Action di AppBar
                    // --------------------------------------------------------
                    // State kecil milik menu ini sendiri: cukup remember,
                    // karena menu yang sedang terbuka tidak perlu bertahan
                    // saat layar diputar.
                    var expanded by remember { mutableStateOf(false) }

                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.more_vert_icon),
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("Hubungi Kami") },
                            onClick = {
                                expanded = false
                                onContactUsClick()
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.mail_icon),
                                    contentDescription = "Email",
                                )
                            },
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {

            // Modul bagian C.5 - kolom pencarian. Nilainya datang dari induk,
            // dan setiap ketukan huruf dilaporkan lewat onSearchQueryChange,
            // yang membuat LaunchedEffect berjalan lagi.
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                label = { Text("Cari produk...") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            Text(
                text = "Kategori Produk",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp),
            )

            // LazyRow: daftar mendatar yang bisa digeser ke samping.
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(categories, key = { it.id }) { category ->
                    CategoryItem(
                        category = category,
                        isSelected = category.id == selectedCategoryId,
                        onClick = { onCategorySelected(category.id) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Daftar Produk",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            // ----------------------------------------------------------------
            // Modul bagian C.7 dan C.8 - tiga kemungkinan tampilan
            // ----------------------------------------------------------------
            // Inilah wujud UI deklaratif: layar tidak diperintah "sembunyikan
            // grid, tampilkan spinner". Kita cukup mendeskripsikan tampilan
            // untuk setiap kondisi, lalu Compose yang menggantinya sendiri.
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Mencari data...")
                    }
                }
            } else {
                if (products.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Produk tidak ditemukan.")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(products, key = { it.id }) { product ->
                            // Kartu produk tidak lagi memunculkan Toast:
                            // sentuhan diteruskan ke atas, supaya induk yang
                            // memutuskan untuk pindah ke halaman detail.
                            ProductItemCard(product = product) {
                                onProductClick(product)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Pratinjau dengan @Preview
// Tampil di panel Android Studio tanpa perlu menjalankan aplikasi.
// ---------------------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun PreviewProduct() {
    JualanTheme {
        ProductItemCard(product = DummyData.products[0], onClick = {})
    }
}

/**
 * Pratinjau kategori. Sesuai gambar 20 modul Pertemuan 3, tombol kategori
 * harus berada tepat di tengah layar: Box selebar dan setinggi layar dengan
 * contentAlignment Center.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCategory() {
    JualanTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CategoryItem(category = DummyData.categories[0], isSelected = true, onClick = {})
        }
    }
}

/**
 * Pratinjau halaman penuh dalam tema TERANG dan GELAP sekaligus. Versi
 * stateless yang dipakai, supaya pratinjau tidak ikut menunggu delay(1000).
 */
@Preview(name = "Light", showSystemUi = true)
@Preview(
    name = "Dark",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
fun PreviewDaftarProduk() {
    JualanTheme {
        StatelessDaftarProduct(
            categories = DummyData.categories,
            selectedCategoryId = DummyData.categories.first().id,
            onCategorySelected = {},
            searchQuery = "",
            onSearchQueryChange = {},
            isLoading = false,
            products = DummyData.products.filter { it.category_id == 1 },
            onProductClick = {},
            onContactUsClick = {},
        )
    }
}
