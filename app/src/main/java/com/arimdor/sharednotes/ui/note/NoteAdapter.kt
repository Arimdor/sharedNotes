package com.arimdor.sharednotes.ui.note

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.arimdor.sharednotes.R
import com.arimdor.sharednotes.repository.entity.Note
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(
        private val context: Context,
        private val notes: MutableList<Note>
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var lastPosition = -1
    private val dateFormat = SimpleDateFormat("dd/MM/yyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_view_note_item, parent, false)
        return NoteAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteAdapter.ViewHolder, position: Int) {
        holder.lblNoteContent.text = notes[position].content
        holder.lblNoteDate.text = dateFormat.format(notes[position].creationDate)

        if (position > lastPosition) {
            val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_scrolling)
            holder.itemView.startAnimation(animation)
            lastPosition = position
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lblNoteContent= itemView.findViewById<TextView>(R.id.lblNoteContent)!!
        val lblNoteDate= itemView.findViewById<TextView>(R.id.lblNoteDate)!!
    }

}