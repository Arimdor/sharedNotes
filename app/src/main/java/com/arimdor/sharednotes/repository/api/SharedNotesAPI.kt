package com.arimdor.sharednotes.repository.api

import com.arimdor.sharednotes.repository.entity.Book
import com.arimdor.sharednotes.repository.entity.Content
import com.arimdor.sharednotes.repository.entity.Note
import com.arimdor.sharednotes.repository.model.ResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface SharedNotesAPI {

    // Books
    @GET("books")
    fun getAllBooks(): Call<ResponseModel<List<Book>>>

    @FormUrlEncoded
    @POST("books")
    fun addBook(@Field("title") title: String,
                @Field("createdBy") createdBy: String): Call<ResponseModel<Book>>

    // Notes
    @FormUrlEncoded
    @POST("notes")
    fun addNote(@Field("title") title: String,
                @Field("createdBy") createdBy: String,
                @Field("book_id") idBook: String): Call<ResponseModel<Note>>

    // Contents
    @FormUrlEncoded
    @POST("contents")
    fun addContent(@Field("note_id") noteID: String,
                   @Field("content") content: String,
                   @Field("createdBy") createdBy: String): Call<ResponseModel<Content>>

    @Multipart
    @POST("contents/multimedia")
    fun addContentMultimedia(@Part image: MultipartBody.Part,
                             @Query("note_id") noteID: String,
                             @Query("createdBy") createdBy: String): Call<ResponseModel<Content>>

    @GET("contents")
    fun getContentsByNote(@Query("note_id") noteID: String): Call<ResponseModel<List<Content>>>

}