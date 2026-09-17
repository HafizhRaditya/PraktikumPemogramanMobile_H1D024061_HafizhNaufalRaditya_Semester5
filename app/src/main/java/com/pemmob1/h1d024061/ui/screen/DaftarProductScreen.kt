package com.pemmob1.h1d024061.ui.screen

import android.content.res.Configuration
import android.widget.Toast
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pemmob1.h1d024061.R
import com.pemmob1.h1d024061.data.dummy.DummyData
import com.pemmob1.h1d024061.data.model.Category
import com.pemmob1.h1d024061.data.model.Product
import com.pemmob1.h1d024061.ui.theme.JualanTheme

// ============================================================================
// Modul Pertemuan 3 bagian E sampai H - Dynamic Lists with Lazy Layouts
// ============================================================================
// Halaman daftar produk UMKM: deretan kategori yang bisa digeser ke samping
// (LazyRow) dan kisi produk dua kolom (LazyVerticalGrid).
//
// "Lazy" berarti hanya item yang sedang terlihat di layar yang digambar. Item
// yang sudah keluar layar dibuang dari memori, sehingga daftar berisi ribuan
// produk pun tetap ringan. Column/Row biasa akan menggambar SEMUA item
// sekaligus, termasuk yang tidak terlihat.
// ============================================================================

/**
 * Modul bagian E - kartu untuk satu produk.
 *
 * @param product data produk yang ditampilkan.
 * @param onClick aksi saat kartu ditekan. Bertipe `() -> Unit`, yaitu fungsi
 *        tanpa masukan dan tanpa nilai kembalian (Lambda / Higher-Order
 *        Function). Kartu ini tidak tahu apa yang akan terjadi; pemanggilnya
 *        yang menentukan, misalnya menampilkan Toast.
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
 * Modul bagian F - satu tombol kategori.
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
 * Modul bagian H - halaman daftar produk.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarProdukScreen() {

    // H.1 - Deklarasi variabel
    //
    // State: remember + mutableStateOf mengingat id kategori yang dipilih.
    // Setiap kali nilainya berubah, Compose menggambar ulang bagian layar yang
    // memakainya (recomposition). firstOrNull() mengambil kategori pertama
    // dengan aman: bila daftar kosong hasilnya null, bukan crash.
    var selectedCategoryId by remember { mutableStateOf(DummyData.categories.firstOrNull()?.id) }

    // Context dibutuhkan untuk menampilkan Toast.
    val context = LocalContext.current
    // Nama pendek untuk daftar produk dari DummyData.
    val products = DummyData.products

    // filter hanya menyisakan produk dari kategori yang dipilih. Bila belum
    // ada kategori yang dipilih, seluruh produk ditampilkan. Variabel ini
    // dihitung ulang setiap kali selectedCategoryId berubah.
    val filteredProducts = if (selectedCategoryId != null) {
        products.filter { it.category_id == selectedCategoryId }
    } else {
        products
    }

    Scaffold(
        // H.1 - TopAppBar sesuai spesifikasi gambar 22
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
                        modifier = Modifier.padding(end = 16.dp),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { paddingValues ->

        // H.2 - Kolom utama. paddingValues berisi tinggi TopAppBar, supaya
        // isi halaman tidak tertutup olehnya.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {

            // H.3 - Tampilan kategori
            Text(
                text = "Kategori Produk",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp),
            )

            // LazyRow: daftar mendatar yang bisa digeser ke samping.
            // contentPadding memberi ruang di ujung kiri-kanan, spacedBy
            // memberi celah antarkategori.
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // items() mengulang untuk setiap kategori. key membantu
                // Compose mengenali item yang sama saat daftar berubah.
                items(DummyData.categories, key = { it.id }) { category ->
                    CategoryItem(
                        category = category,
                        isSelected = category.id == selectedCategoryId,
                        // Mengubah state di sini memicu recomposition,
                        // sehingga warna kategori dan isi grid ikut berganti.
                        onClick = { selectedCategoryId = category.id },
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Daftar Produk",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            // H.4 - Kisi produk dua kolom berdasarkan kategori terpilih
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(filteredProducts, key = { it.id }) { product ->
                    // Lambda terakhir ditulis di luar kurung (trailing
                    // lambda) dan menjadi nilai parameter onClick.
                    ProductItemCard(product = product) {
                        // Toast: pesan singkat yang muncul sekilas lalu
                        // hilang sendiri, tanpa menghalangi layar.
                        Toast.makeText(context, "Clicked: ${product.name}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Modul bagian G dan H.5 - Pratinjau dengan @Preview
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
 * Pratinjau kategori. Sesuai gambar 20, tombol kategori harus berada tepat di
 * tengah layar: Box selebar dan setinggi layar dengan contentAlignment Center.
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
 * H.5 - Pratinjau halaman penuh dalam tema TERANG dan GELAP sekaligus.
 * Dua anotasi @Preview pada satu fungsi menghasilkan dua gambar; uiMode
 * NIGHT_YES membuat isSystemInDarkTheme() di JualanTheme bernilai true.
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
        DaftarProdukScreen()
    }
}
