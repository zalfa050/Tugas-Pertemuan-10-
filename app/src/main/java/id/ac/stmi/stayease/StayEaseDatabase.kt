package id.ac.stmi.stayease

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Hotel::class, Booking::class], version = 1, exportSchema = false)
abstract class StayEaseDatabase : RoomDatabase() {
    abstract fun hotelDao(): HotelDao
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: StayEaseDatabase? = null

        fun getDatabase(context: Context): StayEaseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StayEaseDatabase::class.java,
                    "stayease_db"
                )
                    .allowMainThreadQueries()
                    .build()

                // Isi data awal hotel hanya jika tabel masih kosong
                if (instance.hotelDao().count() == 0) {
                    instance.hotelDao().insertAll(dataAwalHotel())
                }
                INSTANCE = instance
                instance
            }
        }

        private fun dataAwalHotel() = listOf(
            Hotel(nama = "Grand Aston Heritage", kota = "Yogyakarta", tipeKamar = "Deluxe Room",
                harga = 450000, rating = 4.9, fasilitas = "WiFi, Sarapan, Kolam Renang"),
            Hotel(nama = "Katamaran Resort & Spa", kota = "Bali", tipeKamar = "Ocean Suite",
                harga = 650000, rating = 4.8, fasilitas = "Dekat Pantai, WiFi"),
            Hotel(nama = "Borobudur View Inn", kota = "Magelang", tipeKamar = "Family Room",
                harga = 380000, rating = 4.7, fasilitas = "WiFi, Sarapan, Parkir"),
            Hotel(nama = "Neo Urban Stay", kota = "Bandung", tipeKamar = "Standard Room",
                harga = 320000, rating = 4.6, fasilitas = "WiFi, Parkir"),
            Hotel(nama = "Jakarta Central Hotel", kota = "Jakarta", tipeKamar = "Superior Room",
                harga = 520000, rating = 4.5, fasilitas = "WiFi, Sarapan, Gym")
        )
    }
}
