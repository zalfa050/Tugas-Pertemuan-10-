package id.ac.stmi.stayease

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingActivity : AppCompatActivity() {

    private lateinit var database: StayEaseDatabase
    private lateinit var daftarHotel: List<Hotel>

    private lateinit var spHotel: Spinner
    private lateinit var etCheckIn: EditText
    private lateinit var etCheckOut: EditText
    private lateinit var tvInfoHotel: TextView
    private lateinit var tvTamu: TextView
    private lateinit var tvRincian: TextView
    private lateinit var tvTotal: TextView

    private val format = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("id-ID"))
    private val calIn: Calendar = Calendar.getInstance()
    private val calOut: Calendar = Calendar.getInstance()
    private var jumlahTamu = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_booking)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = StayEaseDatabase.getDatabase(this)
        daftarHotel = database.hotelDao().getAll()

        spHotel = findViewById(R.id.spHotel)
        etCheckIn = findViewById(R.id.etCheckIn)
        etCheckOut = findViewById(R.id.etCheckOut)
        tvInfoHotel = findViewById(R.id.tvInfoHotel)
        tvTamu = findViewById(R.id.tvTamu)
        tvRincian = findViewById(R.id.tvRincian)
        tvTotal = findViewById(R.id.tvTotal)
        val etNama = findViewById<EditText>(R.id.etNama)
        val etWa = findViewById<EditText>(R.id.etWa)
        val btnKurang = findViewById<Button>(R.id.btnKurang)
        val btnTambah = findViewById<Button>(R.id.btnTambah)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)

        // Isi pilihan hotel ke Spinner
        val namaHotel = daftarHotel.map { it.nama }
        spHotel.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, namaHotel)

        // Jika dibuka dari tombol "Pesan" di daftar hotel, pilih hotel tersebut
        val idHotel = intent.getIntExtra("ID_HOTEL", -1)
        val posisi = daftarHotel.indexOfFirst { it.id == idHotel }
        if (posisi >= 0) spHotel.setSelection(posisi)

        spHotel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) = hitung()
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        // Tanggal awal: check-in hari ini, check-out besok
        calOut.add(Calendar.DAY_OF_YEAR, 1)
        tampilkanTanggal()

        etCheckIn.setOnClickListener {
            pilihTanggal(calIn) {
                if (!calOut.after(calIn)) {
                    calOut.timeInMillis = calIn.timeInMillis
                    calOut.add(Calendar.DAY_OF_YEAR, 1)
                }
            }
        }
        etCheckOut.setOnClickListener { pilihTanggal(calOut) {} }

        btnKurang.setOnClickListener {
            if (jumlahTamu > 1) {
                jumlahTamu--
                tvTamu.text = jumlahTamu.toString()
            }
        }
        btnTambah.setOnClickListener {
            if (jumlahTamu < 5) {
                jumlahTamu++
                tvTamu.text = jumlahTamu.toString()
            }
        }

        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val wa = etWa.text.toString().trim()
            val malam = hitungMalam()

            if (daftarHotel.isEmpty()) {
                Toast.makeText(this, "Data hotel belum tersedia!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (nama.isEmpty()) {
                Toast.makeText(this, "Nama tamu wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (wa.isEmpty()) {
                Toast.makeText(this, "Nomor WhatsApp wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (malam < 1) {
                Toast.makeText(this, "Tanggal check-out harus setelah check-in!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hotel = daftarHotel[spHotel.selectedItemPosition]

            // Proses Insert ke tabel booking (Room)
            val bookingBaru = Booking(
                hotelId = hotel.id,
                namaTamu = nama,
                noWa = wa,
                checkIn = format.format(calIn.time),
                checkOut = format.format(calOut.time),
                jumlahMalam = malam,
                jumlahTamu = jumlahTamu,
                totalHarga = hotel.harga * malam,
                status = "Terkonfirmasi"
            )
            database.bookingDao().insert(bookingBaru)

            Toast.makeText(this, "Booking berhasil disimpan!", Toast.LENGTH_SHORT).show()
            startActivity(android.content.Intent(this, PesananActivity::class.java))
            finish()
        }

        NavHelper.setup(this, NavHelper.PESAN)
    }

    private fun pilihTanggal(cal: Calendar, setelahPilih: () -> Unit) {
        DatePickerDialog(
            this,
            { _, tahun, bulan, hari ->
                cal.set(tahun, bulan, hari)
                setelahPilih()
                tampilkanTanggal()
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun tampilkanTanggal() {
        etCheckIn.setText(format.format(calIn.time))
        etCheckOut.setText(format.format(calOut.time))
        hitung()
    }

    private fun hitungMalam(): Int {
        // Samakan jam ke 00:00 supaya selisih hari akurat
        fun awalHari(c: Calendar): Long {
            val x = c.clone() as Calendar
            x.set(Calendar.HOUR_OF_DAY, 0)
            x.set(Calendar.MINUTE, 0)
            x.set(Calendar.SECOND, 0)
            x.set(Calendar.MILLISECOND, 0)
            return x.timeInMillis
        }
        val selisih = awalHari(calOut) - awalHari(calIn)
        return ((selisih + 43_200_000L) / 86_400_000L).toInt()
    }

    private fun hitung() {
        if (daftarHotel.isEmpty()) return
        val hotel = daftarHotel[spHotel.selectedItemPosition]
        val malam = hitungMalam()
        val total = if (malam > 0) hotel.harga * malam else 0
        tvInfoHotel.text = "${hotel.tipeKamar} • ${hotel.kota} • ${Util.rupiah(hotel.harga)}/malam"
        tvRincian.text = "$malam Malam × ${Util.rupiah(hotel.harga)}"
        tvTotal.text = "Total: ${Util.rupiah(total)}"
    }
}
