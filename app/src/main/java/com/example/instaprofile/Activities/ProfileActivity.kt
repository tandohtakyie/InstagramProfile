package com.example.instaprofile.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.instaprofile.R
import com.example.instaprofile.model.User
import com.example.instaprofile.util.FirebaseHelper
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity(4) {
    private val TAG = "ProfileActivity"

    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var mUser : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupBottomNavigation()
        Log.d(TAG, "onCreate")
        edit_profile_btn.setOnClickListener{
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        mFirebaseHelper = FirebaseHelper(this)
        mFirebaseHelper.currentUserReference().addValueEventListener(ValueEventListenerAdapter{
            mUser = it.getValue(User::class.java)!!
            profile_image.loadUserPhoto(mUser.photo)
            username_text.text = mUser.username
        })
    }
}
