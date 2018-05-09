package com.arimdor.sharednotes.repository

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.arimdor.sharednotes.repository.dao.BookDao
import com.arimdor.sharednotes.repository.entity.Book
import com.arimdor.sharednotes.repository.model.ResponseModel
import com.arimdor.sharednotes.utils.Constants
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BookRepository {

    val books = MutableLiveData<List<Book>>()
    private val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().setDateFormat("dd-MM-yyyy HH:mm:ss").create()))
            .build()
    private val sharedNotesAPI = retrofit.create(com.arimdor.sharednotes.repository.api.SharedNotesAPI::class.java)
    private val bookDao = BookDao()

    fun createBook(title: String) {
        val call = sharedNotesAPI.addBook(title, "Arimdor")
        call.enqueue(object : Callback<ResponseModel<Book>> {
            override fun onFailure(call: Call<ResponseModel<Book>>?, t: Throwable?) {
                Log.d("test", "Error " + t?.message)
            }

            override fun onResponse(call: Call<ResponseModel<Book>>?, response: Response<ResponseModel<Book>>?) {
                if (response != null) {
                    Log.d("test", "OK " + response.body()?.data.toString())
                    bookDao.insertBook(response.body()!!.data)
                    books.value = bookDao.findAllBooks()
                } else {
                    Log.d("test", "Null in onResponse")
                }
            }
        })
    }

    fun searchAllBooks(): MutableLiveData<List<Book>> {
        books.value = bookDao.findAllBooks()
        val call = sharedNotesAPI.getAllBooks()
        call.enqueue(object : Callback<ResponseModel<List<Book>>> {
            override fun onFailure(call: Call<ResponseModel<List<Book>>>?, t: Throwable?) {
                Log.d("test", "Error " + t?.message.toString())
            }

            override fun onResponse(call: Call<ResponseModel<List<Book>>>?, response: Response<ResponseModel<List<Book>>>?) {
                Log.d("test", "OK " + response?.body().toString())
                if (response != null) {
                    bookDao.deleteAllBook()
                    response.body()?.data?.forEach {
                        bookDao.insertBook(it)
                    }
                } else {
                    Log.d("test", "Null in onResponse")
                }
                books.value = bookDao.findAllBooks()
            }
        })
        return books
    }

    fun findBookByTitle(title: String): MutableList<Book> {
        return bookDao.findBookByTitle(title)
    }

    fun updateBook(idBook: String, title: String) {
        bookDao.updateBook(idBook, title)
    }

    fun deleteBook(idBook: String) {
        bookDao.deleteBook(idBook)
    }
}