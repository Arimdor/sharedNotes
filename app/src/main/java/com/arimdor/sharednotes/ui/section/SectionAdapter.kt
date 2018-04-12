package com.arimdor.sharednotes.ui.section

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.arimdor.sharednotes.R
import com.arimdor.sharednotes.repository.entity.Section
import com.arimdor.sharednotes.ui.note.NoteActivity
import java.text.SimpleDateFormat
import java.util.*

class SectionAdapter(
        private val context: Context,
        private val sections: MutableList<Section>
) : RecyclerView.Adapter<SectionAdapter.ViewHolder>() {

    private var lastPosition = -1
    private val dateFormat = SimpleDateFormat("dd/MM/yyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_view_section_item, parent, false)
        return SectionAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return sections.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.lblTitle.text = sections[position].title
        holder.lblDate.text = dateFormat.format(sections[position].creationDate)
        holder.lblResumen.text = "Lorem ipsum dolor sit amet impera \n adipiscing elit, lorem ipsum dolor \n sed do eiusmod..."

        if (position > lastPosition) {
            val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_scrolling)
            holder.itemView.startAnimation(animation)
            lastPosition = position
        }
        holder.bindEventsToItem(sections[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lblTitle = itemView.findViewById<TextView>(R.id.lblSectionTitle)!!
        val lblDate = itemView.findViewById<TextView>(R.id.lblSetionDate)!!
        val lblResumen = itemView.findViewById<TextView>(R.id.lblSectionResumen)!!
        val imageSection = itemView.findViewById<ImageView>(R.id.imgSection)!!

        fun bindEventsToItem(section: Section) {
            itemView.setOnClickListener {
                val intent = Intent(it.context, NoteActivity::class.java)
                intent.putExtra("idSection", section.id)
                it.context.startActivity(intent)
            }
        }
    }
}