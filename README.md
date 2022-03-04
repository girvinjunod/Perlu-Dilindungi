# Deskripsi Aplikasi
Aplikasi Perlu Dilindungi adalah suatu aplikasi mobile berbasis Android. Pada aplikasi ini, pengguna dapat melihat daftar berita terkait pandemi COVID-19 serta membukanya, mencari faskes berdasarkan provinsi, kota, dan lokasi pengguna, melihat detail dari faskes tersebut, menyimpan bookmark dari faskes tersebut, melakukan scan QR code untuk check in, beserta deteksi temperatur suhu sekitar. Untuk melakukan navigasi antar halaman-halaman yang ada di aplikasi ini, terdapat suatu navigation bar di bagian bawah aplikasi. Aplikasi juga memiliki layout responsive yang dapat berubah sesuai mode potrait atau landscape dari perangkat.
# Cara Kerja
### Menampilkan Berita COVID-19
Aplikasi akan menampilkan daftar berita yang didapat dari API pada suatu RecyclerView. Untuk mendapatkan berita ini, dilakukan Get Request ke API dan data yang didapat pun diterjemahkan melalui adapter agar dapat diproses RecyclerView. Untuk tiap item di RecyclerView, ditampilkan headline, tanggal, dan thumbnail dari berita. Thumbnail ini dapat dari URL gambar yang ada di data. Ketika item di klik, maka akan dibuka suatu webview di dalam aplikasi yang menampilkan konten dari berita tersebut. Ini dicapai dengan membuka URL yang ada di data berita melalui webview.
### Menampilkan Daftar Faskes untuk Vaksinasi
Aplikasi akan menampilkan dua buah spinner agar pengguna dapat memilih provinsi dan kota dari faskes yang ingin dicari. Data untuk item dari spinner tersebut didapat dengan melakukan Get Request pada API. Ketika pengguna baru masuk ke halaman, langsung dilakukan Get Request untuk data provinsi dan ketika provinsi terpilih, dilakukan Get Request untuk data kota untuk provinsi terpilih. Ketika pengguna menekan tombol search, akan dilakukan Get Request ke API dengan parameter pilihan provinsi dan kota dari pengguna. Lalu, data faskes ini akan di-sort berdasarkan kedekatan koordinatnya dengan lokasi pengguna. Lokasi pengguna ini didapatkan menggunakan Google Play Services sehingga pengguna pertama harus memberikan akses lokasi. Setelah data di-sort, maka akan diambil 5 data dengan jarak terkecil lalu dimasukkan ke suatu RecyclerView yang akan menampilkan daftar faskes tersebut ke layar. Jika pengguna tidak memberikan akses lokasi, maka akan ditampilkan suatu Toast yang memberikan informasi bahwa daftar faskes tidak dapat ditampilkan tanpa adanya akses lokasi. Dibuat layout masing-masing untuk orientasi potrait dan landscape dari perangkat sehingga layout akan responsive sesuai dengan orientasi perangkat.
### Menampilkan Detail Informasi Faskes
### Menampilkan Daftar Bookmark Faskes
### Melakukan "Check-In"
# Library
| Nama     | Alasan |
| ----------- | ----------- |
| Timber      | Digunakan untuk memudahkan logging|
| Retrofit  | Melakukan HTTP Request ke API|
| Moshi  | Memudahkan parsing JSON dari API|
| Picasso  | Memudahkan load image dari URL|
| Google Play Services  | Mendapatkan lokasi pengguna|
# Screenshot Aplikasi
Screenshot aplikasi (dimasukkan dalam folder screenshot)
# Pembagian Kerja
Bryan Rinaldo - 13519103
- Menampilkan Detail Informasi Faskes
- Menampilkan Daftar Bookmark Faskes

Jeanne D'Arc Amara Hanieka - 13519082
- Membuka Google Maps dari Detail Informasi
- Melakukan "Check-In"

Girvin Junod - 13519096
- Splash Screen
- Menampilkan Berita COVID-19
- Menampilkan Daftar Faskes untuk Vaksinasi
- Styling


