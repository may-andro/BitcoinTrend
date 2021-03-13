package com.mayandro.bitcointrend.ui.home.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mayandro.bitcointrend.R
import com.mayandro.bitcointrend.databinding.ItemStatsBinding
import com.mayandro.bitcointrend.ui.home.dashboard.uimodel.SelectorModel
import com.mayandro.bitcointrend.utils.AutoUpdatableAdapter
import kotlin.properties.Delegates

class StatsAdapter() : RecyclerView.Adapter<StatsAdapter.ViewHolder>(), AutoUpdatableAdapter {

    var dataSet: List<SelectorModel> by Delegates.observable(emptyList()) {
            _, old, new ->
        autoNotify(old, new ) { o, n ->
            o == n
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.item_stats, viewGroup,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemStatsBinding.bind(view)

        fun bind(data: SelectorModel) {
            binding.apply {
                textStatsName.text = data.title
                textStatsValue.text = data.value
            }
        }
    }
}