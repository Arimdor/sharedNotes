package com.arimdor.sharednotes.ui.book

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
import com.arimdor.sharednotes.repository.entity.Book
import com.arimdor.sharednotes.ui.note.NoteActivity
import java.text.SimpleDateFormat
import java.util.*

class BookAdapter(
        private val context: Context,
        private val books: MutableList<Book>,
        private val viewModel: BookViewModel
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private var lastPosition = -1
    private val dateFormat = SimpleDateFormat("dd/MM/yyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_view_book_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.lblTitle.text = books[position].title
        holder.lblCountNotes.text = "${books[position].notes.size} Notes"
        holder.lblDate.text = dateFormat.format(books[position].creationDate)

        if (position > lastPosition) {
            val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_scrolling)
            holder.itemView.startAnimation(animation)
            lastPosition = position
        }
        holder.bindEventsToItem(books[position], viewModel)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lblTitle = itemView.findViewById<TextView>(R.id.lblBookTitle)!!
        val lblCountNotes = itemView.findViewById<TextView>(R.id.lblCountNotes)!!
        val lblDate = itemView.findViewById<TextView>(R.id.lblBookDate)!!
        private val options = itemView.findViewById<ImageView>(R.id.noteOptions)!!

        fun bindEventsToItem(book: Book, viewModel: BookViewModel) {
            itemView.setOnClickListener {
                val intent = Intent(it.context, NoteActivity::class.java)
                intent.putExtra("idBook", book.id)
                intent.putExtra("bookTitle", book.title)
                it.context.startActivity(intent)
            }
            itemView.setOnLongClickListener {
                setupPopupMenu(it.context, it, book, viewModel)
                true
            }
            options.setOnClickListener {
                setupPopupMenu(it.context, it, book, viewModel)
            }
        }

        private fun setupPopupMenu(context: Context, view: View, book: Book, viewModel: BookViewModel) {
            val menu = PopupMenu(context, view)
            menu.inflate(R.menu.context_menu_book)
            menu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.delete_book_menu -> {
                        viewModel.deleteBook(book.id)
                        true
                    }
                    R.id.edit_book_menu -> {
                        showAlertDialog(context, "Editar nueva libreta", "Editar el título de la libreta", book, viewModel)
                        true
                    }
                    else -> false
                }
            }
            menu.show()
        }

        // Add Book Dialog
        private fun showAlertDialog(context: Context, title: String?, message: String?, book: Book, viewModel: BookViewModel) {

            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)

            val viewInflater = LayoutInflater.from(context).inflate(R.layout.dialog_create_book, null)
            builder.setView(viewInflater)

            val input = viewInflater.findViewById(R.id.txtAddBookTitle) as EditText
            input.setText(book.title, TextView.BufferType.EDITABLE)

            builder.setPositiveButton("Actualizar") { dialog, which ->
                val title = input.text.toString().trim { it <= ' ' }
                if (title.isNotEmpty()) {
                    viewModel.updateBook(book.id, title)
                } else {
                    Toast.makeText(context, "El titulo no puede estar vacío", Toast.LENGTH_SHORT).show()
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
}