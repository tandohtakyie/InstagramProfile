package com.example.instaprofile.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.instaprofile.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(0) {
    private val TAG = "HomeActivity"
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.d(TAG, "onCreate")
        setupBottomNavigation()

        mAuth = FirebaseAuth.getInstance()
//        mAuth.signOut()
//        auth.signInWithEmailAndPassword("admin@insta.com", "admin123")
//            .addOnCompleteListener{
//                if(it.isSuccessful){
//                    Log.d(TAG, "sign in: success")
//                }else{
//                    Log.e(TAG, "sign in: failed", it.exception)
//                }
//            }
        sign_out_text.setOnClickListener{
            mAuth.signOut()
        }

        mAuth.addAuthStateListener {
            if (it.currentUser == null){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(mAuth.currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
