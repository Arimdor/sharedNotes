package com.arimdor.sharednotes.ui.note

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.arimdor.sharednotes.R
import com.arimdor.sharednotes.repository.entity.Note
import com.arimdor.sharednotes.ui.content.ContentActivity
import com.arimdor.sharednotes.ui.login.LoginActivity

class NoteFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var noteAdapter: NoteAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var fbtnAddNote: FloatingActionButton
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel by lazy { ViewModelProviders.of(this).get(NoteViewModel::class.java) }
    private var notes: MutableList<Note> = ArrayList()
    private lateinit var titleBook: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_note, container, false)
        setHasOptionsMenu(true)

        viewModel.idBook = activity?.intent!!.getStringExtra("idBook")
        titleBook = activity?.intent!!.getStringExtra("bookTitle")

        activity?.title = titleBook
        sharedPreferences = activity!!.getSharedPreferences("preferences", Context.MODE_PRIVATE)

        fbtnAddNote = view.findViewById(R.id.fbtnAddNote)
        recyclerView = view.findViewById(R.id.recyclerViewNote)
        mSwipeRefreshLayout = view.findViewById(R.id.noteSwipeRefreshLayout)

        viewModel.getNotes().observe(this, Observer { notes ->
            this.notes.clear()
            notes?.forEach {
                Log.d("test", it.toString())
            }
            this.notes.addAll(0, notes!!)
            noteAdapter.notifyDataSetChanged()
            recyclerView.scheduleLayoutAnimation()
            mSwipeRefreshLayout.isRefreshing = false
        })

        fbtnAddNote.setOnClickListener {
            showAlertDialog("Agregar nueva nota", "Agrega un título a la nota", viewModel.idBook)
        }

        setupRecyclerSections(titleBook)

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadNotes()
    }

    private fun setupRecyclerSections(titleBook: String) {
        val animationRecycler = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_animation_load)

        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        noteAdapter = NoteAdapter(context!!, notes, titleBook, viewModel)
        recyclerView.hasFixedSize()
        recyclerView.adapter = noteAdapter
        recyclerView.layoutAnimation = animationRecycler
        mSwipeRefreshLayout.setOnRefreshListener(this)
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        )
    }

    override fun onRefresh() {
        viewModel.loadNotes()
        mSwipeRefreshLayout.isRefreshing = false
    }

    private fun logOut() {
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        sharedPreferences.edit().putBoolean("loged", false).apply()
        startActivity(intent)
    }

    private fun logOutAndForget() {
        sharedPreferences.edit().clear().apply()
        logOut()
    }

    // Options Menu
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.update_all -> {
                viewModel.loadNotes(viewModel.idBook)
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

    private fun showAlertDialog(title: String?, message: String?, idBook: String) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(title)
        builder.setMessage(message)

        val viewInflater = LayoutInflater.from(context!!).inflate(R.layout.dialog_create_book, null)
        builder.setView(viewInflater)

        val input = viewInflater.findViewById(R.id.txtAddBookTitle) as EditText

        builder.setPositiveButton("Aceptar") { dialog, which ->
            val titleNote = input.text.toString().trim { it <= ' ' }
            if (titleNote.isNotEmpty()) {
                val noteLive = viewModel.createNote(titleNote, "Arimdor", idBook)
                noteLive.observe(this, Observer {
                    if (it != null) {
                        val intent = Intent(context, ContentActivity::class.java)
                        Log.d("test", "Intent idNote " + it.toString())
                        intent.putExtra("idNote", it.id)
                        intent.putExtra("titleNote", titleNote)
                        intent.putExtra("titleBook", titleBook)
                        startActivity(intent)
                    } else {
                        Toast.makeText(context, "No se pudo crear la nota", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                })
            } else {
                Toast.makeText(context, "Se requiere un título para la nota", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNeutralButton("Cancelar") { dialog, which ->
            dialog.cancel()
        }
        val dialog = builder.create()
        dialog.show()
    }


}
