package id.ac.stmi.stayease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HotelAdapter(
    private var data: List<Hotel>,
    private val onPesan: (Hotel) -> Unit
) : RecyclerView.Adapter<HotelAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvNama: TextView = v.findViewById(R.id.tvNama)
        val tvKota: TextView = v.findViewById(R.id.tvKota)
        val tvRating: TextView = v.findViewById(R.id.tvRating)
        val tvFasilitas: TextView = v.findViewById(R.id.tvFasilitas)
        val tvHarga: TextView = v.findViewById(R.id.tvHarga)
        val btnPesan: Button = v.findViewById(R.id.btnPesan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_hotel, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val h = data[position]
        holder.tvNama.text = h.nama
        holder.tvKota.text = "${h.kota}, Indonesia"
        holder.tvRating.text = h.rating.toString()
        holder.tvFasilitas.text = h.fasilitas
        holder.tvHarga.text = "${Util.rupiah(h.harga)} /malam"
        holder.btnPesan.setOnClickListener { onPesan(h) }
    }

    override fun getItemCount() = data.size

    fun setData(baru: List<Hotel>) {
        data = baru
        notifyDataSetChanged()
    }
}
