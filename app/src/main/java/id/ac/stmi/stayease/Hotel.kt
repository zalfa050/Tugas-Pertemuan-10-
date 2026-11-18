package id.ac.stmi.stayease

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabel 1: hotel
@Entity(tableName = "hotel")
data class Hotel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val kota: String,
    val tipeKamar: String,
    val harga: Int,
    val rating: Double,
    val fasilitas: String
)
