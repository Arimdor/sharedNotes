package com.arimdor.sharednotes.ui.note

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.arimdor.sharednotes.R
import com.arimdor.sharednotes.repository.entity.Note
import com.arimdor.sharednotes.ui.content.ContentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(
        private val context: Context,
        private val notes: MutableList<Note>,
        private val titleBook: String,
        private val viewModel: NoteViewModel
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
        holder.lblResumen.text = viewModel.generateResumeText(notes[position])
        Glide.with(context)
                .load(viewModel.generateResumeImage(notes[position]))
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imageNote)

        if (position > lastPosition) {
            val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_scrolling)
            holder.itemView.startAnimation(animation)
            lastPosition = position
        }
        holder.bindEventsToItem(notes[position], titleBook, viewModel)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lblTitle = itemView.findViewById<TextView>(R.id.lblNoteTitle)!!
        val lblDate = itemView.findViewById<TextView>(R.id.lblNoteDate)!!
        val lblResumen = itemView.findViewById<TextView>(R.id.lblNoteResumen)!!
        val imageNote = itemView.findViewById<ImageView>(R.id.imgNote)!!

        fun bindEventsToItem(note: Note, titleBook: String, viewModel: NoteViewModel) {

            itemView.setOnLongClickListener {
                setupPopupMenu(it.context, it, note, viewModel)
                true
            }

            itemView.setOnClickListener {
                val intent = Intent(it.context, ContentActivity::class.java)
                intent.putExtra("idSection", note.id)
                intent.putExtra("titleSection", note.title)
                intent.putExtra("titleBook", titleBook)
                it.context.startActivity(intent)
            }
        }

        private fun setupPopupMenu(context: Context, view: View, note: Note, viewModel: NoteViewModel) {
            val menu = PopupMenu(context, view)
            menu.inflate(R.menu.contex_menu_note)
            menu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.delete_note_menu -> {
                        viewModel.removeNote(note.id)
                        true
                    }
                    R.id.edit_note_menu -> {
                        showAlertDialog(context, "Editar nota", "Editar el título de la nota", note, viewModel)
                        true
                    }
                    else -> false
                }
            }
            menu.show()
        }

        // Add Note Dialog
        private fun showAlertDialog(context: Context, title: String?, message: String?, note: Note, viewModel: NoteViewModel) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)

            val viewInflater = LayoutInflater.from(context).inflate(R.layout.dialog_create_book, null)
            builder.setView(viewInflater)

            val input = viewInflater.findViewById(R.id.txtAddBookTitle) as EditText
            input.setText(note.title, TextView.BufferType.EDITABLE)

            builder.setPositiveButton("Actualizar") { dialog, which ->
                val title = input.text.toString().trim { it <= ' ' }
                if (title.isNotEmpty()) {
                    viewModel.updateNote(note.id, title)
                } else {
                    Toast.makeText(context, "El titulo no puede estar vacío", Toast.LENGTH_SHORT).show()
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
}