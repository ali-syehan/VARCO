# VARQO — GitHub Pages dan APK Android

**VARQO** adalah aplikasi HTML mandiri dengan 12 output dan database 112 nama malaikat. Paket ini berasal dari `VARQO.html` dan menjaga rumus, data, syakal, serta pembacaan Latin yang sudah ada.

Ada **dua hasil dari satu sumber** (`docs/index.html`):

- **Web di GitHub Pages** — dapat dibuka di browser, dan dapat dipasang ke layar utama Android melalui Chrome (PWA). Butuh internet saat kunjungan pertama, lalu berkas disimpan untuk penggunaan berikutnya tanpa internet.
- **APK Android** — GitHub Actions akan mengompilasi file APK dari folder `app/`, lengkap dengan HTML lokal. Setelah terinstal, proses perhitungan **tidak memerlukan internet**.

> **Penting:** Paket ini merupakan **kode sumber siap build**. File APK belum jadi sampai workflow `Buat APK VARQO` berhasil dijalankan di repository GitHub Anda. Anda tidak perlu memasang Android Studio untuk build lewat GitHub.

## 1. Struktur file

```text
VARQO_GitHub_Android/
├── .github/
│   └── workflows/
│       └── build-apk.yml           # Otomatis membuat APK di GitHub
├── app/
│   ├── build.gradle               # Konfigurasi APK Android
│   └── src/main/
│       ├── AndroidManifest.xml    # Identitas aplikasi dan izin
│       ├── java/id/alisyehan/varqo/MainActivity.java
│       └── res/
│           ├── drawable/ic_launcher.xml
│           └── values/styles.xml
├── docs/
│   ├── index.html                 # Aplikasi VARQO (inti rumus tetap)
│   ├── manifest.webmanifest       # Identitas PWA
│   ├── sw.js                      # Cache supaya web dapat offline
│   ├── .nojekyll
│   └── icons/
│       ├── icon-192.png
│       └── icon-512.png
├── build.gradle
├── settings.gradle
├── gradle.properties
├── .gitignore
└── README.md
```

## 2. Buat repository GitHub

1. Login ke https://github.com/ dan klik **+ → New repository**.
2. Isi **Repository name:** `VARQO` (tanpa spasi).
3. Pilih **Public** apabila ingin menggunakan GitHub Pages dengan akun gratis. Ingat: kode/rumus dan database dalam repository publik dapat dilihat orang lain. Anda dapat memilih **Private** untuk build APK saja; ketersediaan Pages pada repo privat bergantung paket GitHub.
4. Untuk kemudahan upload, **jangan centang** Add a README / .gitignore / license karena semua sudah dalam paket.
5. Klik **Create repository**.
6. Ekstrak ZIP yang diberikan, lalu upload **isi folder VARQO_GitHub_Android**, bukan ZIP-nya. Pertahankan folder `docs`, `app`, dan **`.github`** beserta semua subfoldernya. Jika memakai macOS Finder, tekan **Command + Shift + .** agar folder `.github` terlihat.
7. Di repo kosong pilih **uploading an existing file**. Tarik seluruh isi folder ke area unggah, lalu klik **Commit changes**. Jika antarmuka web tidak mempertahankan folder tersembunyi, pakai **GitHub Desktop** atau buat `.github/workflows/build-apk.yml` lewat tombol Add file → Create new file.

## 3. Cara membuat APK Android dari GitHub

1. Di repo `VARQO`, buka tab **Actions**.
2. Jika GitHub meminta izin menjalankan workflow, klik **I understand my workflows, go ahead and enable them**.
3. Pilih workflow **Buat APK VARQO**.
4. Klik **Run workflow**, pilih branch `main`, lalu klik tombol hijau **Run workflow**. Upload ke `main` juga akan memicu build otomatis bila file terkait berubah.
5. Buka workflow run yang sudah bertanda **centang hijau** (berhasil).
6. Gulir ke bagian **Artifacts**, klik **VARQO-APK-Android**. Browser akan mengunduh ZIP artefak hasil build.
7. **Ekstrak ZIP artefak**, di dalamnya ada **`app-debug.apk`**. Itulah file yang dikirim ke HP dan diinstal, bukan ZIP proyek awal.

### Install APK di HP Android

1. Pindahkan `app-debug.apk` ke ponsel via kabel, Drive, atau cara lain.
2. Buka APK di aplikasi File Manager.
3. Jika muncul pembatasan, buka **Pengaturan → Izinkan dari sumber ini** untuk aplikasi File Manager yang Anda pakai. Nama menu berbeda tiap merek Android.
4. Tekan **Instal** lalu cari ikon **VARQO**.
5. Setelah terinstal, gunakan aplikasi tanpa internet. Untuk cetak, pilih tombol cetak di VARQO; pada Android akan tampil dialog cetak/simpan PDF (bergantung perangkat).

**Catatan update APK:** Build di atas menggunakan **debug signing key** dari mesin GitHub. Key dapat berganti di setiap build, sehingga APK baru mungkin tidak dapat dipasang menimpa APK lama. Untuk versi pribadi, uninstall versi lama lalu instal APK baru (data dalam aplikasi tidak tersimpan sebagai riwayat). Untuk pembaruan jangka panjang atau distribusi luas, diperlukan **release keystore permanen** dan build yang ditandatangani dengan key yang sama.

## 4. Cara membuat alamat web GitHub Pages

1. Buka repo → **Settings** → menu **Pages**.
2. Pada **Build and deployment**, pilih Source **Deploy from a branch**.
3. Pilih branch `main` dan folder **`/docs`**, lalu klik **Save**.
4. Setelah proses publish selesai, buka:

   `https://USERNAME.github.io/VARQO/`

   Ganti `USERNAME` dengan username GitHub Anda. URL persis dapat dilihat dari tombol **Visit site** pada menu Pages.

5. Di Chrome Android, kunjungi URL tersebut. Menu **⋮ → Install app** atau **Tambahkan ke layar utama** dapat muncul bila Chrome menganggapnya dapat dipasang. Ini **bukan APK** melainkan aplikasi web yang dipasang dari browser.
6. Buka saat online sekali agar berkas tersimpan; sesudah itu dapat digunakan offline pada browser/profil yang sama, selama cache tidak dihapus.

Untuk Pages dari branch `main`, folder `/docs` memang merupakan pilihan resmi GitHub. Tidak perlu mengatur **Custom domain**; biarkan kosong.

## 5. Apa yang berubah dari VARQO.html?

- Inti JavaScript yang menghitung 12 output dan 112 nama malaikat **tidak diubah**.
- Dalam `docs/index.html` ditambah metadata PWA dan pendaftaran service worker pada HTTPS saja.
- Proyek Android memuat `docs/index.html` dari file dalam APK, bukan dari website, dan menyediakan jembatan cetak lokal. Tidak ada permission INTERNET di AndroidManifest.
- Tampilan Android disesuaikan agar tidak menampilkan bingkai HP tiruan di dalam layar ponsel. Pengaturan ini hanya di wrapper Android, bukan rumus.

## 6. Jika bermasalah

| Masalah | Langkah pemeriksaan |
|---|---|
| Workflow tidak muncul di tab Actions | Pastikan ada `.github/workflows/build-apk.yml` di branch `main` dan Actions diaktifkan. |
| Build merah / Failed | Buka run merah → langkah pertama yang gagal → salin log agar dapat diperiksa. |
| Tidak ada `app-debug.apk` | Pastikan langkah `Kompilasi APK siap instal` dan `Simpan hasil APK` sama-sama berhasil. |
| Pages 404 | Pastikan `docs/index.html` ada dan Pages menggunakan `main` → `/docs`. |
| Tombol Install app tidak muncul | Chrome dapat menampilkan `Tambahkan ke layar utama`; URL mesti HTTPS dan halaman sudah dibuka. |
| APK baru gagal dipasang di atas APK lama | Debug signing key berbeda. Uninstall yang lama lebih dulu, atau siapkan penandatanganan rilis permanen. |

## 7. Pemeliharaan berikutnya

Ubah sumber aplikasi pada **`docs/index.html`**. Berkas yang sama dipakai untuk Pages dan secara otomatis dibundel ke APK pada build berikutnya. Saat memperbarui web, naikkan nilai `CACHE_NAME` dalam `docs/sw.js` agar cache PWA lama dibersihkan. Untuk APK yang akan menjadi pembaruan permanen, tingkatkan `versionCode` di `app/build.gradle` dan gunakan keystore penandatanganan yang sama.

Proyek ini disiapkan untuk menghasilkan **APK instalasi langsung**; bukan paket siap unggah Play Store. Google Play memerlukan proses tambahan, termasuk penandatanganan rilis dan format AAB.
