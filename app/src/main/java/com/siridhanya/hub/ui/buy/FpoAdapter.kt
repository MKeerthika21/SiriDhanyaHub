package com.siridhanya.hub.ui.buy

import android.view.*
import androidx.recyclerview.widget.*
import com.siridhanya.hub.data.entities.FpoContact
import com.siridhanya.hub.databinding.ItemFpoBinding

class FpoAdapter(private val onCallClick: (String) -> Unit) :
    ListAdapter<FpoContact, FpoAdapter.VH>(DIFF) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<FpoContact>() {
            override fun areItemsTheSame(a: FpoContact, b: FpoContact) = a.id == b.id
            override fun areContentsTheSame(a: FpoContact, b: FpoContact) = a == b
        }
    }
    inner class VH(private val b: ItemFpoBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(f: FpoContact) {
            b.tvFpoName.text      = f.name
            b.tvDistrict.text     = "📍 ${f.district}"
            b.tvLocation.text     = f.location
            b.tvPhone.text        = "📞 ${f.phone}"
            b.tvPriceRange.text   = f.priceRange
            b.tvDescription.text  = f.description
            b.tvCertified.text    = if (f.certified) "✅ Certified Organic" else "🌿 Natural Farming"
            b.tvMillets.text      = "Millets: ${f.millets.replace(",", " • ")}"
            b.btnCall.setOnClickListener { onCallClick(f.phone) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemFpoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))
}
