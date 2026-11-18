package id.ac.stmi.stayease

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookingDao {
    @Insert
    fun insert(booking: Booking)

    @Query("UPDATE booking SET status = :status WHERE id = :id")
    fun updateStatus(id: Int, status: String)

    @Query("DELETE FROM booking WHERE id = :id")
    fun deleteById(id: Int)

    @Query(
        "SELECT b.id AS id, h.nama AS namaHotel, h.kota AS kota, b.namaTamu AS namaTamu, " +
                "b.checkIn AS checkIn, b.checkOut AS checkOut, b.jumlahMalam AS jumlahMalam, " +
                "b.jumlahTamu AS jumlahTamu, b.totalHarga AS totalHarga, b.status AS status " +
                "FROM booking b INNER JOIN hotel h ON b.hotelId = h.id ORDER BY b.id DESC"
    )
    fun getAllWithHotel(): List<BookingWithHotel>
}
