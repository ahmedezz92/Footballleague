package com.example.footballleague.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballleague.R
import com.example.footballleague.data.model.competitions.Competitions
import com.example.footballleague.databinding.ItemCompetitionBinding
import com.example.footballleague.utils.loadUrl
import kotlin.coroutines.coroutineContext

class CompetitionAdapter(
    private val competitionsList: List<Competitions>,
    private val onCompetitionClickListener: OnCompetitionClickListener?,
) :
    RecyclerView.Adapter<CompetitionViewHolder>() {
    constructor() : this(emptyList(), null)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompetitionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCompetitionBinding.inflate(inflater, parent, false)
        return CompetitionViewHolder(binding)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: CompetitionViewHolder, position: Int) {
        val itemData = competitionsList[position]
        holder.itemView.context
        holder.binding.tvCompetitionName.text = itemData.name
        holder.binding.tvCompetitionAreaName.text = itemData.area.name
        holder.itemView.setOnClickListener {
            onCompetitionClickListener?.onCompetitionClick(competitionsList[position])
        }
        itemData.emblem?.let {
            holder.binding.ivCompetition.loadUrl(itemData.emblem)
        }

    }

    override fun getItemCount(): Int {
        return competitionsList.size
    }

    fun addCompetition(competition: Competitions) {
        competitionsList.toMutableList().add(competition)
        notifyDataSetChanged()
    }

}

class CompetitionViewHolder(val binding: ItemCompetitionBinding) :
    RecyclerView.ViewHolder(binding.root)

interface OnCompetitionClickListener {
    fun onCompetitionClick(competition: Competitions)
}