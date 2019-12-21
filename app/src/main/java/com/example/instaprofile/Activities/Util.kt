package com.example.instaprofile.Activities

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.instaprofile.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ValueEventListenerAdapter(val handler: (DataSnapshot) -> Unit) : ValueEventListener {
    private val TAG = "ValueEvenListenerAdapt"

    override fun onCancelled(error: DatabaseError) {
        Log.e(TAG, "onCancelled:", error.toException())
    }

    override fun onDataChange(data: DataSnapshot) {
        handler(data)
    }
}

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

fun ImageView.loadUserPhoto(photoUrl: String?) {
    if (!(context as Activity).isDestroyed) {
        Glide.with(this).load(photoUrl).fallback(R.drawable.person).into(this)
    }
}

fun Editable.toStringOrNull(): String? {
    val str = toString()
    return if (str.isEmpty()) null else str
}

@GlideModule
class CustomGlideModule : AppGlideModule()