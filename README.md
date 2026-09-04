# Identitas

Nama  : Hafizh Naufal Raditya
NIM   : H1D024061
Shift : A

# Pertemuan 1 — Membuat Project & Dasar Compose

Aplikasi **Jualan**, dibangun dengan Kotlin dan Jetpack Compose sesuai modul
Pertemuan 1.

- Application name : `Jualan`
- Package name     : `com.pemmob1.h1d024061`
- Min SDK          : 24
- Target SDK       : 37

# Screenshot

## Display Pertemuan 1

_Menyusul — screenshot aplikasi yang berjalan di perangkat._

# Yang Diimplementasikan

Layar utama dibangun lewat satu fungsi `@Composable` bernama
`LayoutTentangJualan()`, berisi:

| Komponen | Penerapan |
|---|---|
| `Column` | `fillMaxSize()`, `padding(16.dp)`, `horizontalAlignment = CenterHorizontally` |
| `Box` | `size(100.dp)`, `clip(CircleShape)`, `background(Color.Gray)`, konten rata tengah |
| `Text` dalam Box | tulisan "Jualan" berwarna putih dan tebal |
| `Spacer` | pemberi jarak antar elemen, `height(24.dp)` dan `height(16.dp)` |
| `Text` | judul "Tentang Jualan" dan paragraf deskripsi aplikasi |
| `Row` | `fillMaxWidth()`, `background(Color(0xFFE0E0E0))`, `padding(16.dp)` |
| `Modifier.weight()` | "Misi Kami:" `weight(1f)` dan "Memajukan UMKM Lokal" `weight(2f)`, sehingga ruang terbagi 1/3 dan 2/3 |

Dua urutan `Modifier` yang ditekankan modul diterapkan persis:

- Pada `Box`, `.clip(CircleShape)` dipanggil **sebelum** `.background()`, sehingga
  warna abu-abu hanya mengisi area yang sudah dipotong bulat.
- Pada `Row`, `.background()` dipanggil **sebelum** `.padding()`, sehingga area
  padding ikut terwarnai.

# Menjalankan

```bash
./gradlew :app:assembleDebug
```

Atau buka folder ini di Android Studio lalu tekan **Run**.

Tampilan juga dapat dilihat tanpa perangkat melalui `@Preview` pada
`MainActivity.kt` — buka berkasnya, lalu klik **Split** di pojok kanan atas editor.
