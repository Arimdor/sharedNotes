package com.arimdor.sharednotes.ui.book

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
import com.arimdor.sharednotes.repository.entity.Book
import com.arimdor.sharednotes.ui.login.LoginActivity

class BookActivity : AppCompatActivity() {

    private lateinit var sectionAdapter: BookAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var fbtnAddBook: FloatingActionButton
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel by lazy { ViewModelProviders.of(this).get(BookViewModel::class.java) }
    private var books: MutableList<Book> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        fbtnAddBook = findViewById(R.id.fbtnAddBook)

        fbtnAddBook.setOnClickListener {
            showAlertDialog("Agregar nueva libreta", "Agrega un titulo a la nueva libreta")
        }

        setupRecyclerBoards()

        viewModel.getBooks().observe(this, Observer { books ->
            this.books.clear()
            this.books.addAll(0, books!!)
            sectionAdapter.notifyDataSetChanged()
            recyclerView.scheduleLayoutAnimation()
        })
    }

    private fun setupRecyclerBoards() {
        recyclerView = findViewById(R.id.recyclerViewBook)
        val animationRecycler = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_animation_load)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        sectionAdapter = BookAdapter(this, books)
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
                viewModel.loadBooks()
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

    // Add Book Dialog
    private fun showAlertDialog(title: String?, message: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)

        val viewInflater = LayoutInflater.from(this).inflate(R.layout.dialog_create_book, null)
        builder.setView(viewInflater)

        val input = viewInflater.findViewById(R.id.txtAddBookTitle) as EditText

        builder.setPositiveButton("Add") { dialog, which ->
            val title = input.text.toString().trim { it <= ' ' }
            if (title.isNotEmpty()) {
                viewModel.addBooks(title)
            } else {
                Toast.makeText(applicationContext, "The name is required to create a new Book", Toast.LENGTH_SHORT).show()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

}
