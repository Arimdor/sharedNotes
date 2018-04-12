package com.arimdor.sharednotes.ui.note

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.arimdor.sharednotes.R
import com.arimdor.sharednotes.repository.entity.Note
import com.arimdor.sharednotes.ui.login.LoginActivity
import com.arimdor.sharednotes.util.CameraUtil
import com.arimdor.sharednotes.util.Constants
import java.io.File
import java.io.IOException

class NoteActivity : AppCompatActivity() {

    private lateinit var noteAdapter: NoteAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var fbtnAddNote: FloatingActionButton
    private lateinit var fbtnAddNotePhoto: FloatingActionButton
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var idSection: String
    private val viewModel by lazy { ViewModelProviders.of(this).get(NoteViewModel::class.java) }
    private val notes: MutableList<Note> = ArrayList()
    private var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)

        idSection = intent.getStringExtra("idSection")

        fbtnAddNote = findViewById(R.id.fbtnAddNote)
        fbtnAddNotePhoto = findViewById(R.id.fbtnAddNotePhoto)
        fbtnAddNote.setOnClickListener {
            showAlertDialog("Agregar nueva nota", "Agrega un contenido a la nota", idSection)
        }
        fbtnAddNotePhoto.setOnClickListener {
            dispatchTakePictureIntent(photoFile)
        }

        setupRecyclerNote()

        viewModel.loadNotes(idSection)
        viewModel.getNotes().observe(this, Observer { notes ->
            Log.d("test", notes?.size.toString())
            this.notes.clear()
            this.notes.addAll(0, notes!!)
            noteAdapter.notifyDataSetChanged()
            recyclerView.scheduleLayoutAnimation()
        })

        viewModel.tempPhoto.observe(this, Observer { tempPhoto ->

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            viewModel.addNote(photoFile!!.absolutePath, idSection, 1)
            photoFile = null
        }
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode != Activity.RESULT_OK) {
            photoFile?.delete()
            photoFile = null
        }
    }

    private fun dispatchTakePictureIntent(photo: File?) {
        val cameraUtil = CameraUtil()
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(packageManager) != null) {

            try {
                photoFile = cameraUtil.createImageFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES))
            } catch (ex: IOException) {
                Log.e("test", ex.message)
            }

            Log.d("test", photoFile!!.absolutePath)
            val photoURI = FileProvider.getUriForFile(this, "com.arimdor.android.fileprovider", photoFile!!)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE)
        }
    }


    private fun setupRecyclerNote() {
        recyclerView = findViewById(R.id.recyclerViewNote)
        val animationRecycler = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_animation_load)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        noteAdapter = NoteAdapter(this, notes)
        recyclerView.hasFixedSize()
        recyclerView.adapter = noteAdapter
        recyclerView.layoutAnimation = animationRecycler
    }

    private fun logOut() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        sharedPreferences.edit().putBoolean("loged", false).apply()
        startActivity(intent)
    }

    private fun logOutAndForget() {
        sharedPreferences.edit().clear().apply()
        logOut()
    }

    // Options Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.update_all -> {
                viewModel.loadNotes(idSection)
                return true
            }
            R.id.delete_all -> {
                viewModel.removeAllNotes(idSection)
                return true
            }
            R.id.menu_logout -> {
                logOut()
                return true
            }
            R.id.menu_forget -> {
                logOutAndForget()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showAlertDialog(title: String?, message: String?, idSection: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)

        val viewInflater = LayoutInflater.from(this).inflate(R.layout.dialog_create_book, null)
        builder.setView(viewInflater)

        val input = viewInflater.findViewById(R.id.txtAddBookTitle) as EditText

        builder.setPositiveButton("Add") { dialog, which ->
            val title = input.text.toString().trim { it <= ' ' }
            if (title.isNotEmpty()) {
                viewModel.addNote(title, idSection)
            } else {
                Toast.makeText(applicationContext, "The name is required to create a new Book", Toast.LENGTH_SHORT).show()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
}
