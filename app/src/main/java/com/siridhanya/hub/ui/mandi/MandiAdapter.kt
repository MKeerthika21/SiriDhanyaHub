package com.siridhanya.hub.ui.mandi

import android.graphics.Color
import android.view.*
import androidx.recyclerview.widget.*
import com.siridhanya.hub.data.entities.MandiPrice
import com.siridhanya.hub.databinding.ItemMandiPriceBinding

class MandiAdapter : ListAdapter<MandiPrice, MandiAdapter.VH>(DIFF) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<MandiPrice>() {
            override fun areItemsTheSame(a: MandiPrice, b: MandiPrice) = a.id == b.id
            override fun areContentsTheSame(a: MandiPrice, b: MandiPrice) = a == b
        }
    }
    inner class VH(private val b: ItemMandiPriceBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(p: MandiPrice) {
            val milletEnum = try { com.siridhanya.hub.data.entities.MilletType.valueOf(p.milletType) } catch(e: Exception) { null }
            b.tvMilletName.text  = "${milletEnum?.emoji ?: "🌾"} ${milletEnum?.displayName ?: p.milletType}"
            b.tvKannadaName.text = milletEnum?.kannadaName ?: ""
            b.tvCity.text        = "📍 ${p.city}"
            b.tvMarket.text      = p.marketName
            b.tvCurrentPrice.text = "₹${p.pricePerKg}/kg"
            b.tvTrend.text       = p.trendEmoji()
            b.tvWeekHigh.text    = "High: ₹${p.weekHigh}"
            b.tvWeekLow.text     = "Low:  ₹${p.weekLow}"
            b.tvLastUpdated.text = "Updated: ${p.lastUpdated}"
            b.tvCurrentPrice.setTextColor(Color.parseColor(p.trendColor()))
            val range = p.weekHigh - p.weekLow
            val progress = if (range > 0) ((p.pricePerKg - p.weekLow) / range * 100).toInt() else 50
            b.priceRangeBar.progress = progress.coerceIn(0, 100)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemMandiPriceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))
}
