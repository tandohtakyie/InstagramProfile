package com.example.instaprofile.Activities

import android.os.Bundle
import android.util.Log
import com.example.instaprofile.R

class SearchActivity : BaseActivity(1) {
    private val TAG = "SearchActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupBottomNavigation()
        Log.d(TAG, "onCreate")
    }
}
