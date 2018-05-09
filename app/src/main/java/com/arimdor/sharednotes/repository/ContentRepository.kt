package com.arimdor.sharednotes.repository

import android.arch.lifecycle.MutableLiveData
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.arimdor.sharednotes.repository.dao.ContentDao
import com.arimdor.sharednotes.repository.entity.Content
import com.arimdor.sharednotes.repository.model.ResponseModel
import com.arimdor.sharednotes.utils.Constants
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor




class ContentRepository {

    val contents = MutableLiveData<List<Content>>()
    private val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()!!
    private val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().setDateFormat("dd-MM-yyyy HH:mm:ss").create()))
            .build()
    private val sharedNotesAPI = retrofit.create(com.arimdor.sharednotes.repository.api.SharedNotesAPI::class.java)
    private val contentDao = ContentDao()

    fun createContent(content: String, idNote: String, type: Int) {
        if (type == Constants.TYPE_TEXT) {
            val call = sharedNotesAPI.addContent(idNote, content, "Arimdor")

            call.enqueue(object : Callback<ResponseModel<Content>> {
                override fun onFailure(call: Call<ResponseModel<Content>>?, t: Throwable?) {
                    Log.d("test", "Error creation content in server, " + t?.message)
                }

                override fun onResponse(call: Call<ResponseModel<Content>>?, response: Response<ResponseModel<Content>>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            Log.d("test", "OK " + response.body()?.data.toString())
                            contentDao.insertContent(response.body()!!.data, idNote)
                            contents.value = contentDao.findAllContent(idNote)
                        } else {
                            Log.d("test", "Response not successful " + response.code())
                        }
                    } else {
                        Log.d("test", "Response null")
                    }
                }
            })
        } else if (type == Constants.TYPE_MULTIMEDIA) {
            val file = File(content)
            val bitmap = BitmapFactory.decodeFile(file.path)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, FileOutputStream(file))
            Log.d("test", "Note id $idNote image url ${file.totalSpace}")

            val requestFile = RequestBody.create(MediaType.parse("*/*"), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val call = sharedNotesAPI.addContentMultimedia(body, idNote, "Arimdor")

            call.enqueue(object : Callback<ResponseModel<Content>> {
                override fun onFailure(call: Call<ResponseModel<Content>>?, t: Throwable?) {
                    Log.d("test", "Error creation content in server, " + t?.message)
                }

                override fun onResponse(call: Call<ResponseModel<Content>>?, response: Response<ResponseModel<Content>>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            Log.d("test", "OK " + response.body()?.data.toString())
                            contentDao.insertContent(response.body()!!.data, idNote)
                            contents.value = contentDao.findAllContent(idNote)
                        } else {
                            Log.d("test", "Response not successful " + response.code() + " ${response.body()?.message}")
                        }
                    } else {
                        Log.d("test", "Response null")
                    }
                }
            })
        }
    }

    fun searchAllContents(idNote: String): MutableLiveData<List<Content>> {
        Log.d("test", "idNote = $idNote")
        val call = sharedNotesAPI.getContentsByNote(idNote)
        call.enqueue(object : Callback<ResponseModel<List<Content>>> {
            override fun onFailure(call: Call<ResponseModel<List<Content>>>?, t: Throwable?) {
                Log.d("test", "Error " + t?.message.toString())
            }

            override fun onResponse(call: Call<ResponseModel<List<Content>>>?, response: Response<ResponseModel<List<Content>>>?) {
                Log.d("test", "OK Response " + response?.body().toString())
                if (response != null) {
                    response.body()?.data?.forEach {
                        contentDao.insertContent(it, idNote)
                    }
                } else {
                    Log.d("test", "Null in onResponse")
                }
            }

        })
        contents.value = contentDao.findAllContent(idNote)
        return contents
    }

    fun updateContent(idContent: String, contentValue: String) {
        contentDao.updateContent(idContent, contentValue)
    }

    fun deleteContent(idContent: String) {
        contentDao.removeContent(idContent)
    }

    fun deleteAllContents(idNote: String) {
        contentDao.removeAllContents(idNote)
    }
}