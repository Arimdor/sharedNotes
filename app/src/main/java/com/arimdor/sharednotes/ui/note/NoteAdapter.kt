package com.arimdor.sharednotes.ui.note

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.arimdor.sharednotes.R
import com.arimdor.sharednotes.repository.entity.Note
import com.arimdor.sharednotes.util.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(
        private val context: Context,
        private val notes: MutableList<Note>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var lastPosition = -1
    private val dateFormat = SimpleDateFormat("dd/MM/yyy", Locale.getDefault())

    override fun getItemViewType(position: Int): Int {
        return notes[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.TYPE_IMAGE) {
            NoteAdapter.ViewHolderPhoto(LayoutInflater.from(parent.context).inflate(R.layout.list_view_note_item_photo, parent, false))
        } else {
            NoteAdapter.ViewHolderText(LayoutInflater.from(parent.context).inflate(R.layout.list_view_note_item_text, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            Constants.TYPE_TEXT -> {
                initHolderText(holder as NoteAdapter.ViewHolderText, position)
            }
            Constants.TYPE_IMAGE -> {
                initHolderPhoto(holder as NoteAdapter.ViewHolderPhoto, position)
            }
        }

        if (position > lastPosition) {
            val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_scrolling)
            holder.itemView.startAnimation(animation)
            lastPosition = position
        }
    }

    private fun initHolderText(holder: NoteAdapter.ViewHolderText, position: Int) {
        holder.lblNoteContent.text = notes[position].content
        holder.lblNoteDate.text = dateFormat.format(notes[position].creationDate)
    }

    private fun initHolderPhoto(holder: NoteAdapter.ViewHolderPhoto, position: Int) {
        Glide.with(context)
                .load(File(notes[position].content))
                .apply(RequestOptions().transforms(CenterCrop()))
                .into(holder.imgNotePhoto)
        holder.lblNotePhotoDate.text = dateFormat.format(notes[position].creationDate)
    }

    class ViewHolderText(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lblNoteContent = itemView.findViewById<TextView>(R.id.lblNoteContent)!!
        val lblNoteDate = itemView.findViewById<TextView>(R.id.lblNoteDate)!!
    }

    class ViewHolderPhoto(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgNotePhoto = itemView.findViewById<ImageView>(R.id.imgNotePhoto)!!
        val lblNotePhotoDate = itemView.findViewById<TextView>(R.id.lblNotePhotoDate)!!
    }

}