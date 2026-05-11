package com.siridhanya.hub.ui.health

import android.view.*
import androidx.recyclerview.widget.*
import com.siridhanya.hub.data.entities.HealthBenefit
import com.siridhanya.hub.data.entities.MilletType
import com.siridhanya.hub.databinding.ItemHealthBenefitBinding

class HealthAdapter : ListAdapter<HealthBenefit, HealthAdapter.VH>(DIFF) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<HealthBenefit>() {
            override fun areItemsTheSame(a: HealthBenefit, b: HealthBenefit) = a.id == b.id
            override fun areContentsTheSame(a: HealthBenefit, b: HealthBenefit) = a == b
        }
    }
    inner class VH(private val b: ItemHealthBenefitBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(h: HealthBenefit) {
            val milletEnum = try { MilletType.valueOf(h.milletType) } catch(e: Exception) { null }
            b.tvIcon.text        = h.iconEmoji
            b.tvTitle.text       = h.title
            b.tvMilletName.text  = "${milletEnum?.emoji ?: "🌾"} ${milletEnum?.kannadaName ?: h.milletType}"
            b.tvCondition.text   = h.condition
            b.tvDescription.text = h.description
            b.tvSource.text      = "📚 Source: ${h.source}"

            // Expand/collapse
            var expanded = false
            b.tvDescription.maxLines = 3
            b.root.setOnClickListener {
                expanded = !expanded
                b.tvDescription.maxLines = if (expanded) Int.MAX_VALUE else 3
                b.tvSource.visibility = if (expanded) View.VISIBLE else View.GONE
            }
            b.tvSource.visibility = View.GONE
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemHealthBenefitBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))
}
