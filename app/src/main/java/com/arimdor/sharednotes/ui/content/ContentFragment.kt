package com.arimdor.sharednotes.ui.content


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
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.arimdor.sharednotes.R
import com.arimdor.sharednotes.app.MyApplication
import com.arimdor.sharednotes.repository.entity.Content
import com.arimdor.sharednotes.ui.login.LoginActivity
import com.arimdor.sharednotes.util.CameraUtil
import com.arimdor.sharednotes.util.Constants
import java.io.File
import java.io.IOException

class ContentFragment : Fragment() {

    private lateinit var contentAdapter: ContentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var fbtnAddContent: FloatingActionButton
    private lateinit var fbtnAddContentPhoto: FloatingActionButton
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var txtNoteTitleNote: TextView
    private lateinit var idSection: String
    private lateinit var viewModel: ContentViewModel
    private val contents: MutableList<Content> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_content, container, false)

        viewModel = ViewModelProviders.of(this).get(ContentViewModel::class.java)
        sharedPreferences = activity!!.getSharedPreferences("preferences", Context.MODE_PRIVATE)

        idSection = activity?.intent!!.getStringExtra("idSection")
        activity?.title = activity?.intent!!.getStringExtra("titleBook")

        txtNoteTitleNote = view.findViewById(R.id.txtSectionTitleNote)
        fbtnAddContent = view.findViewById(R.id.fbtnAddContent)
        fbtnAddContentPhoto = view.findViewById(R.id.fbtnAddContentPhoto)
        recyclerView = view.findViewById(R.id.recyclerViewContent)

        txtNoteTitleNote.text = activity?.intent!!.getStringExtra("titleSection")

        fbtnAddContent.setOnClickListener {
            showAlertDialog("Agregar nueva nota", "Agrega un contenido a la nota", idSection)
        }
        fbtnAddContentPhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }

        setupRecyclerNote()

        viewModel.idSection = idSection
        viewModel.loadCotents()
        viewModel.getContents().observe(this, Observer { notes ->
            Log.d("test", notes?.size.toString())
            this.contents.clear()
            this.contents.addAll(0, notes!!)
            contentAdapter.notifyDataSetChanged()
            recyclerView.scheduleLayoutAnimation()
        })

        Log.d("test", "Bundle = ${savedInstanceState.toString()}")
        val tempphotoUri = savedInstanceState?.getString("photoUri")
        if (!tempphotoUri.isNullOrEmpty()) {
            MyApplication.photoUri = tempphotoUri!!
        }

        return view
    }

    private fun setupRecyclerNote() {
        val animationRecycler = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_animation_load)

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        contentAdapter = ContentAdapter(context!!, contents, viewModel)
        recyclerView.hasFixedSize()
        recyclerView.adapter = contentAdapter
        recyclerView.layoutAnimation = animationRecycler
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("test", "onSaveInstanceState() from fragment")
        if (viewModel.photoUri.isNotEmpty()) {
            outState.putString("photoUri", viewModel.photoUri)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (viewModel.photoUri.isEmpty() && !MyApplication.photoUri.isEmpty()) {
                viewModel.photoUri = MyApplication.photoUri
                MyApplication.photoUri = ""
            }
            viewModel.addContent(viewModel.photoUri, idSection, Constants.TYPE_IMAGE)
            viewModel.photoUri = ""
        } else Toast.makeText(context, "No se pudo guardar la foto :(", Toast.LENGTH_SHORT).show()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun dispatchTakePictureIntent() {
        var photoFile: File? = null
        val cameraUtil = CameraUtil()
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(activity!!.packageManager) != null) {
            try {
                photoFile = cameraUtil.createImageFile(activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
            } catch (ex: IOException) {
                Log.e("test", ex.message)
            }

            val photoURI = FileProvider.getUriForFile(context!!, "com.arimdor.android.fileprovider", photoFile!!)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            viewModel.photoUri = photoFile.toString()
            activity!!.startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE)
        }
    }

    // Options Menu
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.update_all -> {
                viewModel.loadCotents(idSection)
                return true
            }
            R.id.delete_all -> {
                viewModel.removeAllContents(idSection)
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
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(title)
        builder.setMessage(message)

        val viewInflater = LayoutInflater.from(context).inflate(R.layout.dialog_create_book, null)
        builder.setView(viewInflater)

        val input = viewInflater.findViewById(R.id.txtAddBookTitle) as EditText

        builder.setPositiveButton("Add") { dialog, which ->
            val title = input.text.toString().trim { it <= ' ' }
            if (title.isNotEmpty()) {
                viewModel.addContent(title, idSection)
            } else {
                Toast.makeText(context, "The name is required to create a new Book", Toast.LENGTH_SHORT).show()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

}
