package id.ac.stmi.stayease

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PesananActivity : AppCompatActivity() {

    private lateinit var database: StayEaseDatabase
    private lateinit var adapter: BookingAdapter
    private lateinit var tvKosong: TextView
    private lateinit var tvBadgePesanan: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pesanan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = StayEaseDatabase.getDatabase(this)
        tvKosong = findViewById(R.id.tvKosong)
        tvBadgePesanan = findViewById(R.id.tvBadgePesanan)
        val rvBooking = findViewById<RecyclerView>(R.id.rvBooking)

        adapter = BookingAdapter(emptyList()) { booking -> tampilkanMenu(booking) }
        rvBooking.layoutManager = LinearLayoutManager(this)
        rvBooking.adapter = adapter

        NavHelper.setup(this, NavHelper.PESANAN)
    }

    override fun onResume() {
        super.onResume()
        muatDataBooking()
    }

    private fun muatDataBooking() {
        // Mengambil data booking + nama hotel (JOIN) dari Room
        val data = database.bookingDao().getAllWithHotel()
        adapter.setData(data)
        tvBadgePesanan.text = "${data.size} pesanan"
        tvKosong.visibility = if (data.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun tampilkanMenu(b: BookingWithHotel) {
        val opsi = arrayOf("Tandai Selesai", "Batalkan Pesanan", "Hapus Pesanan")
        AlertDialog.Builder(this)
            .setTitle("${Util.kode(b.id)} - ${b.namaHotel}")
            .setItems(opsi) { _, which ->
                when (which) {
                    0 -> database.bookingDao().updateStatus(b.id, "Selesai")      // UPDATE
                    1 -> database.bookingDao().updateStatus(b.id, "Dibatalkan")   // UPDATE
                    2 -> database.bookingDao().deleteById(b.id)                   // DELETE
                }
                muatDataBooking()
            }
            .setNegativeButton("Tutup", null)
            .show()
    }
}
