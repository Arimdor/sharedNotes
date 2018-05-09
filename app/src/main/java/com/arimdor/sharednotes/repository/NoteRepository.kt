package com.arimdor.sharednotes.repository

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.arimdor.sharednotes.repository.dao.NoteDao
import com.arimdor.sharednotes.repository.entity.Note
import com.arimdor.sharednotes.repository.model.ResponseModel
import com.arimdor.sharednotes.utils.Constants
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NoteRepository {

    val note = MutableLiveData<Note>()
    private val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().setDateFormat("dd-MM-yyyy HH:mm:ss").create()))
            .build()
    private val sharedNotesAPI = retrofit.create(com.arimdor.sharednotes.repository.api.SharedNotesAPI::class.java)
    private val noteDao = NoteDao()

    fun createNote(title: String, createdBy: String, idBook: String): MutableLiveData<Note> {
        val call = sharedNotesAPI.addNote(title, createdBy, idBook)
        call.enqueue(object : Callback<ResponseModel<Note>> {
            override fun onFailure(call: Call<ResponseModel<Note>>?, t: Throwable?) {
                Log.d("test", "Error creation note in server, " + t?.message)
            }

            override fun onResponse(call: Call<ResponseModel<Note>>?, response: Response<ResponseModel<Note>>?) {
                if (response != null) {
                    if (response.isSuccessful) {
                        noteDao.insertNote(response.body()!!.data, idBook)
                        note.value = response.body()!!.data
                    } else {
                        Log.d("test", "Response not successful " + response.code())
                    }
                } else {
                    Log.d("test", "Response null")
                }
            }
        })
        return note
    }

    fun findNoteByID(idNote: String): Note? {
        return noteDao.findNoteByID(idNote)
    }

    fun updateNote(idNote: String, title: String) {
        noteDao.updateNote(idNote, title)
    }

    fun removeNote(idNote: String) {
        noteDao.removeNote(idNote)
    }

    fun searchAllNotes(idBook: String): MutableList<Note> {
        return noteDao.findAllNotesInBook(idBook)
    }
}