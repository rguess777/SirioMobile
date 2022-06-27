package com.example.sirioecommerceapp.utils

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.sirioecommerceapp.R
import java.io.IOException
import android.content.Context

class LoaderGlide(val context: Context) {

    fun loadUserPicture(image: Any, imageView: ImageView) {
        try {


            Glide
                .with(context)
                .load(Uri.parse(image.toString()))
                .centerCrop()
                .placeholder(R.drawable.defaultuser)
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    fun loadProductPicture(image: Any, imageView: ImageView) {
        try {
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}