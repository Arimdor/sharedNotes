package com.arimdor.sharednotes.ui.content

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.util.Log
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
import com.arimdor.sharednotes.repository.entity.Content
import com.arimdor.sharednotes.ui.photo.PhotoActivity
import com.arimdor.sharednotes.util.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

class ContentAdapter(
        private val context: Context,
        private val contents: MutableList<Content>,
        private val viewModel: ContentViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var lastPosition = -1
    private val dateFormat = SimpleDateFormat("dd/MM/yyy", Locale.getDefault())

    override fun getItemViewType(position: Int): Int {
        return contents[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.TYPE_IMAGE) {
            ContentAdapter.ViewHolderPhoto(LayoutInflater.from(parent.context).inflate(R.layout.list_view_content_item_photo, parent, false))
        } else {
            ContentAdapter.ViewHolderText(LayoutInflater.from(parent.context).inflate(R.layout.list_view_content_item_text, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return contents.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            Constants.TYPE_TEXT -> {
                initHolderText(holder as ContentAdapter.ViewHolderText, position)
            }
            Constants.TYPE_IMAGE -> {
                initHolderPhoto(holder as ContentAdapter.ViewHolderPhoto, position)
            }
        }

        if (position > lastPosition) {
            val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_scrolling)
            holder.itemView.startAnimation(animation)
            lastPosition = position
        }
    }

    private fun initHolderText(holder: ContentAdapter.ViewHolderText, position: Int) {
        holder.lblContent.text = contents[position].content
        holder.lblContentDate.text = dateFormat.format(contents[position].creationDate)
        holder.bindEventToItem(contents[position], viewModel)
    }

    private fun initHolderPhoto(holder: ContentAdapter.ViewHolderPhoto, position: Int) {
        Glide.with(context)
                .load(contents[position].content)
                .apply(RequestOptions().transforms(CenterCrop()))
                .into(holder.imgPhoto)
        holder.lblPhotoDate.text = dateFormat.format(contents[position].creationDate)
        holder.bindEventToItem(contents[position])
        Log.d("test", contents[position].content)
    }

    class ViewHolderText(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lblContent = itemView.findViewById<TextView>(R.id.lblContent)!!
        val lblContentDate = itemView.findViewById<TextView>(R.id.lblNoteDate)!!

        fun bindEventToItem(content: Content, viewModel: ContentViewModel) {
            itemView.setOnLongClickListener {
                setupPopupMenu(it.context, it, content, viewModel)
                true
            }
        }

        private fun setupPopupMenu(context: Context, view: View, content: Content, viewModel: ContentViewModel) {
            val menu = PopupMenu(context, view)
            menu.inflate(R.menu.contex_menu_note)
            menu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.delete_note_menu -> {
                        viewModel.removeContent(content.id)
                        true
                    }
                    R.id.edit_note_menu -> {
                        showAlertDialog(context, "Editar nota", "Editar el título de la nota", content, viewModel)
                        true
                    }
                    else -> false
                }
            }
            menu.show()
        }

        // Add Note Dialog
        private fun showAlertDialog(context: Context, title: String?, message: String?, content: Content, viewModel: ContentViewModel) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)

            val viewInflater = LayoutInflater.from(context).inflate(R.layout.dialog_create_book, null)
            builder.setView(viewInflater)

            val input = viewInflater.findViewById(R.id.txtAddBookTitle) as EditText
            input.setText(content.content, TextView.BufferType.EDITABLE)

            builder.setPositiveButton("Actualizar") { dialog, which ->
                val contentValue = input.text.toString().trim { it <= ' ' }
                if (contentValue.isNotEmpty()) {
                    viewModel.updateContent(content.id, contentValue)
                } else {
                    Toast.makeText(context, "El titulo no puede estar vacío", Toast.LENGTH_SHORT).show()
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    class ViewHolderPhoto(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto = itemView.findViewById<ImageView>(R.id.imgNotePhoto)!!
        val lblPhotoDate = itemView.findViewById<TextView>(R.id.lblNotePhotoDate)!!

        private fun setupPopupMenu(context: Context, view: View) {
            val menu = PopupMenu(context, view)
            menu.inflate(R.menu.contex_menu_note)
            menu.show()
        }

        fun bindEventToItem(content: Content) {
            itemView.setOnClickListener {
                val intent = Intent(it.context, PhotoActivity::class.java)
                intent.putExtra("photoUri", content.content)
                it.context.startActivity(intent)
            }
            itemView.setOnLongClickListener {
                setupPopupMenu(it.context, it)
                true
            }
        }
    }
}