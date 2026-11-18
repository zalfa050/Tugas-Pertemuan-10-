package id.ac.stmi.stayease

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HotelDao {
    @Insert
    fun insertAll(hotels: List<Hotel>)

    @Query("SELECT * FROM hotel ORDER BY rating DESC")
    fun getAll(): List<Hotel>

    @Query("SELECT * FROM hotel WHERE nama LIKE '%' || :kata || '%' OR kota LIKE '%' || :kata || '%' ORDER BY rating DESC")
    fun cari(kata: String): List<Hotel>

    @Query("SELECT COUNT(*) FROM hotel")
    fun count(): Int
}
