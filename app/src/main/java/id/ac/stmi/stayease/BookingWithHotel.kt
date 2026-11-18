package id.ac.stmi.stayease

// Hasil JOIN tabel booking + hotel (bukan tabel, hanya penampung hasil query)
data class BookingWithHotel(
    val id: Int,
    val namaHotel: String,
    val kota: String,
    val namaTamu: String,
    val checkIn: String,
    val checkOut: String,
    val jumlahMalam: Int,
    val jumlahTamu: Int,
    val totalHarga: Int,
    val status: String
)
