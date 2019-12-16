package com.example.instaprofile.Activities

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ValueEventListenerAdapter(val  handler: (DataSnapshot) -> Unit) : ValueEventListener {
    private val TAG = "ValueEvenListenerAdapt"

    override fun onCancelled(error: DatabaseError) {
        Log.e(TAG, "onCancelled:", error.toException())
    }

    override fun onDataChange(data: DataSnapshot) {
        handler(data)
    }
}

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, text, duration).show()
}