package id.ac.stmi.stayease

import android.app.Activity
import android.content.Intent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.text.NumberFormat
import java.util.Locale

object Util {
    fun rupiah(nilai: Int): String {
        val format = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"))
        return "Rp " + format.format(nilai)
    }

    fun kode(id: Int): String = "#STAY-" + id.toString().padStart(4, '0')
}

// Navigasi bawah (Eksplor / Pesan / Pesanan Saya) dipakai bersama oleh 3 activity
object NavHelper {
    const val EKSPLOR = 0
    const val PESAN = 1
    const val PESANAN = 2

    fun setup(activity: Activity, aktif: Int) {
        val biru = ContextCompat.getColor(activity, R.color.blue)
        val abu = ContextCompat.getColor(activity, R.color.text_muted)

        val tab = listOf(
            Triple(R.id.navEksplor, R.id.imgNavEksplor, R.id.txtNavEksplor),
            Triple(R.id.navPesan, R.id.imgNavPesan, R.id.txtNavPesan),
            Triple(R.id.navPesanan, R.id.imgNavPesanan, R.id.txtNavPesanan)
        )
        val tujuan = listOf(
            MainActivity::class.java,
            BookingActivity::class.java,
            PesananActivity::class.java
        )

        for (i in tab.indices) {
            val warna = if (i == aktif) biru else abu
            activity.findViewById<ImageView>(tab[i].second).setColorFilter(warna)
            activity.findViewById<TextView>(tab[i].third).setTextColor(warna)

            activity.findViewById<LinearLayout>(tab[i].first).setOnClickListener {
                if (i == aktif) return@setOnClickListener
                val intent = Intent(activity, tujuan[i])
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                activity.startActivity(intent)
                if (i != EKSPLOR) activity.finish()
            }
        }
    }
}
