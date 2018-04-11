package com.arimdor.sharednotes.ui.section

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.arimdor.sharednotes.R
import com.arimdor.sharednotes.repository.entity.Section
import com.arimdor.sharednotes.ui.login.LoginActivity

class SectionActivity : AppCompatActivity() {

    private lateinit var sectionAdapter: SectionAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var fbtnAddSection: FloatingActionButton
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var idBook: String
    private val viewModel by lazy { ViewModelProviders.of(this).get(SectionViewModel::class.java) }
    private var sections: MutableList<Section> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section)

        sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)

        idBook = intent.getStringExtra("idBook")
        val bookTitle = intent.getStringExtra("bookTitle")

        this.title = bookTitle
        fbtnAddSection = findViewById(R.id.fbtnAddSection)
        fbtnAddSection.setOnClickListener {
            showAlertDialog("Agregar nueva sección", "Agrega un titulo a la nueva sección", idBook)
        }

        setupRecyclerBoards()

        viewModel.loadSections(idBook)
        viewModel.getSections().observe(this, Observer { sections ->
            this.sections.clear()
            this.sections.addAll(0, sections!!)
            sectionAdapter.notifyDataSetChanged()
            recyclerView.scheduleLayoutAnimation()
        })
    }

    private fun setupRecyclerBoards() {
        recyclerView = findViewById(R.id.recyclerViewSection)
        val animationRecycler = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_animation_load)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        sectionAdapter = SectionAdapter(this, sections)
        recyclerView.hasFixedSize()
        recyclerView.adapter = sectionAdapter
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
                viewModel.loadSections(idBook)
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
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)

        val viewInflater = LayoutInflater.from(this).inflate(R.layout.dialog_create_book, null)
        builder.setView(viewInflater)

        val input = viewInflater.findViewById(R.id.txtAddBookTitle) as EditText

        builder.setPositiveButton("Add") { dialog, which ->
            val title = input.text.toString().trim { it <= ' ' }
            if (title.isNotEmpty()) {
                viewModel.addSection(title, idBook)
            } else {
                Toast.makeText(applicationContext, "The name is required to create a new Book", Toast.LENGTH_SHORT).show()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
}
