package com.arimdor.sharednotes.ui.note

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.arimdor.sharednotes.R
import com.arimdor.sharednotes.repository.entity.Note
import com.arimdor.sharednotes.ui.content.ContentActivity
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(
        private val context: Context,
        private val notes: MutableList<Note>,
        private val titleBook: String
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var lastPosition = -1
    private val dateFormat = SimpleDateFormat("dd/MM/yyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_view_note_item, parent, false)
        return NoteAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.lblTitle.text = notes[position].title
        holder.lblDate.text = dateFormat.format(notes[position].creationDate)
        holder.lblResumen.text = "Lorem ipsum dolor sit amet impera \n adipiscing elit, lorem ipsum dolor \n sed..."

        if (position > lastPosition) {
            val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_scrolling)
            holder.itemView.startAnimation(animation)
            lastPosition = position
        }
        holder.bindEventsToItem(notes[position], titleBook)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lblTitle = itemView.findViewById<TextView>(R.id.lblNoteTitle)!!
        val lblDate = itemView.findViewById<TextView>(R.id.lblNoteDate)!!
        val lblResumen = itemView.findViewById<TextView>(R.id.lblNoteResumen)!!
        //val imageSection = itemView.findViewById<ImageView>(R.id.imgSection)!!

        fun bindEventsToItem(note: Note, titleBook: String) {
            itemView.setOnClickListener {
                val intent = Intent(it.context, ContentActivity::class.java)
                intent.putExtra("idSection", note.id)
                intent.putExtra("titleSection", note.title)
                intent.putExtra("titleBook", titleBook)
                it.context.startActivity(intent)
            }
        }
    }
}