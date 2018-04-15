package com.arimdor.sharednotes.ui.photo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.arimdor.sharednotes.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView

class PhotoFragment : Fragment() {

    private lateinit var photoView: PhotoView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo, container, false)

        val photoUri = activity?.intent!!.getStringExtra("photoUri")
        photoView = view.findViewById(R.id.photo_view)
        photoView.maximumScale = 400f
        photoView.adjustViewBounds = true

        Glide.with(context!!)
                .load(photoUri)
                .into(photoView)

        activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        return view
    }


}
