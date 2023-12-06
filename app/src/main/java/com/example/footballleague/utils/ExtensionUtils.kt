package com.example.footballleague.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.example.footballleague.R


fun View.visibleOrGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.visibleOrInvisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

fun ImageView.loadSvgUrl(url: String?) {
    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(this@loadSvgUrl.context)) }
        .build()

    val request = ImageRequest.Builder(this.context)
        .placeholder(R.drawable.ic_place_holder)
        .error(R.drawable.ic_place_holder)
        .data(url)
        .target(this)
        .build()

    imageLoader.enqueue(request)
}

fun ImageView.loadUrl(url: String?) {
    Glide.with(context).load(url)
        .error(R.drawable.ic_place_holder)
        .centerCrop()
        .into(this)
}

