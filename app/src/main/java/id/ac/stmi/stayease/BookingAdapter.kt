package id.ac.stmi.stayease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class BookingAdapter(
    private var data: List<BookingWithHotel>,
    private val onKlik: (BookingWithHotel) -> Unit
) : RecyclerView.Adapter<BookingAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvKode: TextView = v.findViewById(R.id.tvKode)
        val tvStatus: TextView = v.findViewById(R.id.tvStatus)
        val tvHotel: TextView = v.findViewById(R.id.tvHotel)
        val tvKota: TextView = v.findViewById(R.id.tvKota)
        val tvTanggal: TextView = v.findViewById(R.id.tvTanggal)
        val tvTamu: TextView = v.findViewById(R.id.tvTamu)
        val tvTotal: TextView = v.findViewById(R.id.tvTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val b = data[position]
        val ctx = holder.itemView.context
        holder.tvKode.text = Util.kode(b.id)
        holder.tvStatus.text = b.status
        when (b.status) {
            "Selesai" -> {
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_done)
                holder.tvStatus.setTextColor(ContextCompat.getColor(ctx, R.color.text_muted))
            }
            "Dibatalkan" -> {
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_cancel)
                holder.tvStatus.setTextColor(ContextCompat.getColor(ctx, R.color.red))
            }
            else -> {
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_ok)
                holder.tvStatus.setTextColor(ContextCompat.getColor(ctx, R.color.green))
            }
        }
        holder.tvHotel.text = b.namaHotel
        holder.tvKota.text = b.kota
        holder.tvTanggal.text = "${b.checkIn} - ${b.checkOut} (${b.jumlahMalam} malam)"
        holder.tvTamu.text = "${b.namaTamu} • ${b.jumlahTamu} tamu"
        holder.tvTotal.text = Util.rupiah(b.totalHarga)
        holder.itemView.setOnClickListener { onKlik(b) }
    }

    override fun getItemCount() = data.size

    fun setData(baru: List<BookingWithHotel>) {
        data = baru
        notifyDataSetChanged()
    }
}
