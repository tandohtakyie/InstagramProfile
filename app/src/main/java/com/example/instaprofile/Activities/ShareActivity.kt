package com.example.instaprofile.Activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.instaprofile.R
import com.example.instaprofile.util.CameraHelper
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : BaseActivity(2) {
    private val TAG = "ShareActivity"
    private lateinit var mCameraHelper: CameraHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        setupBottomNavigation()
        Log.d(TAG, "onCreate")

        mCameraHelper = CameraHelper(this)
        mCameraHelper.takeCameraPicture()

        back_image.setOnClickListener{finish()}
    }


    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == mCameraHelper.REQUEST_CODE && resultCode == RESULT_OK){
            Glide.with(this).load(mCameraHelper.imageUrl).centerCrop().into(post_image)
        }
    }
}
