package id.ac.stmi.stayease

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var database: StayEaseDatabase
    private lateinit var adapter: HotelAdapter
    private lateinit var etCari: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = StayEaseDatabase.getDatabase(this)

        etCari = findViewById(R.id.etCari)
        val rvHotel = findViewById<RecyclerView>(R.id.rvHotel)

        adapter = HotelAdapter(emptyList()) { hotel ->
            val intent = Intent(this, BookingActivity::class.java)
            intent.putExtra("ID_HOTEL", hotel.id)
            startActivity(intent)
        }
        rvHotel.layoutManager = LinearLayoutManager(this)
        rvHotel.adapter = adapter

        etCari.doAfterTextChanged { muatDataHotel() }
        NavHelper.setup(this, NavHelper.EKSPLOR)
    }

    override fun onResume() {
        super.onResume()
        muatDataHotel()
    }

    private fun muatDataHotel() {
        val kata = etCari.text.toString().trim()
        // Mengambil data hotel dari Room lewat HotelDao
        val hotels = if (kata.isEmpty()) database.hotelDao().getAll() else database.hotelDao().cari(kata)
        adapter.setData(hotels)

    }
}
