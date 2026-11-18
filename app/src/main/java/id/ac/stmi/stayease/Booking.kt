package id.ac.stmi.stayease

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// Tabel 2: booking (berelasi ke tabel hotel lewat hotelId)
@Entity(
    tableName = "booking",
    foreignKeys = [
        ForeignKey(
            entity = Hotel::class,
            parentColumns = ["id"],
            childColumns = ["hotelId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("hotelId")]
)
data class Booking(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val hotelId: Int,
    val namaTamu: String,
    val noWa: String,
    val checkIn: String,
    val checkOut: String,
    val jumlahMalam: Int,
    val jumlahTamu: Int,
    val totalHarga: Int,
    val status: String
)
