package com.nvvi9.tutnews.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?) {
    url?.let {
        Glide.with(context)
            .load(it)
            .into(this)
    }
}

@BindingAdapter("authors")
fun TextView.setAuthors(authorsList: List<String>?) {
    authorsList?.takeIf { it.isNotEmpty() }?.let { authors ->
        visibility = View.VISIBLE
        authors.foldIndexed(StringBuilder()) { i, acc, str ->
            acc.apply {
                append(str)
                if (i != authorsList.size - 1) {
                    append('\n')
                }
            }
        }.toString().let {
            text = it
        }
    } ?: run { visibility = View.GONE }
}

@BindingAdapter("date")
fun TextView.setDate(date: Date?) {
    date?.let {
        text = DateFormat.getDateInstance(SimpleDateFormat.LONG, Locale("ru")).format(it)
    }
}