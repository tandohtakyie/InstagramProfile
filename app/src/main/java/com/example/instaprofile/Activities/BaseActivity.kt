package com.example.instaprofile.Activities

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.instaprofile.R
import kotlinx.android.synthetic.main.bottom_navigation_view.*

abstract class BaseActivity(private val navNumber: Int) : AppCompatActivity(){
    private val TAG = "BaseActivity"
    fun setupBottomNavigation(){
        bottom_navigation_view.setOnNavigationItemReselectedListener {
            val nextActivity = when (it.itemId) {
                R.id.nav_item_home -> HomeActivity::class.java
                R.id.nav_item_search -> SearchActivity::class.java
                R.id.nav_item_share -> ShareActivity::class.java
                R.id.nav_item_likes -> LikesActivity::class.java
                R.id.nav_item_profile -> ProfileActivity::class.java
                else -> {
                    Log.e(TAG, "unknown navigation item clicked")
                    null
                }
            }
            if (nextActivity != null) {
                val intent = Intent(this, nextActivity)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
                overridePendingTransition(0,0)
                true
            } else {
                false
            }
        }
        bottom_navigation_view.menu.getItem(navNumber).isChecked = true
    }
}