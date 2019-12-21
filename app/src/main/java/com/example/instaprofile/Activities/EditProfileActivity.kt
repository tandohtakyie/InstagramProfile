package com.example.instaprofile.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.instaprofile.R
import com.example.instaprofile.model.User
import com.example.instaprofile.util.CameraHelper
import com.example.instaprofile.views.PasswordDialog
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_edit_profile.*


class EditProfileActivity : AppCompatActivity(), PasswordDialog.Listener {
    private val TAG = "EditProfileActivity"
    private lateinit var mUser: User
    private lateinit var mPendingUser: User
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mStorage: StorageReference
    private lateinit var cameraHelper: CameraHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        Log.d(TAG, "onCreate")

        cameraHelper = CameraHelper(this)

        close_image.setOnClickListener { finish() }
        save_image.setOnClickListener { updateProfile() }
        change_photo_text.setOnClickListener { cameraHelper.takeCameraPicture() }

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance().reference
        val user = mAuth.currentUser

        mDatabase.child("users").child(user!!.uid)
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                mUser = it.getValue(User::class.java)!!
                name_input.setText(mUser.name, TextView.BufferType.EDITABLE)
                username_input.setText(mUser.username, TextView.BufferType.EDITABLE)
                website_input.setText(mUser.website, TextView.BufferType.EDITABLE)
                bio_input.setText(mUser.bio, TextView.BufferType.EDITABLE)
                email_input.setText(mUser.email, TextView.BufferType.EDITABLE)
                phone_input.setText(mUser.phone?.toString(), TextView.BufferType.EDITABLE)
                profile_image.loadUserPhoto(mUser.photo)
            })
    }


    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == cameraHelper.REQUEST_CODE && resultCode == RESULT_OK) {
            val uid = mAuth.currentUser!!.uid
            mStorage.child("users/$uid/photo").putFile(cameraHelper.imageUrl!!)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val downloadTask = it.result!!.metadata!!.reference!!.downloadUrl
                        downloadTask.addOnSuccessListener { uri ->
                            mDatabase.child("users/$uid/photo").setValue(uri.toString())
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        mUser = mUser.copy(photo = uri.toString())
                                        profile_image.loadUserPhoto(mUser.photo)
                                    } else {
                                        showToast(task.exception!!.message!!)
                                    }
                                }
                        }
                    } else {
                        showToast(it.exception!!.message!!)
                    }
                }
        }
    }

    private fun StorageReference.uploadUserPhoto(
        uid: String,
        photo: Uri,
        onSuccess: (UploadTask.TaskSnapshot) -> Unit
    ) {
        child("users/$uid/photo").putFile(photo).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess(it.result!!)
            } else {
                showToast(it.exception!!.message!!)
            }
        }
    }

    private fun DatabaseReference.updateUserPhoto(
        uid: String,
        photoUri: String,
        onSuccess: () -> Unit
    ) {
        child("users/$uid/photo").setValue(photoUri).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                showToast(it.exception!!.message!!)
            }
        }
    }

    private fun updateProfile() {
        mPendingUser = readInputs()
        val error = validate(mPendingUser)
        if (error == null) {
            if (mPendingUser.email == mUser.email) {
                updateUser(mPendingUser)
            } else {
                PasswordDialog().show(supportFragmentManager, "password_dialog")
            }
        } else {
            showToast(error)
        }
    }

    private fun readInputs(): User {
        return User(
            name = name_input.text.toString(),
            username = username_input.text.toString(),
            email = email_input.text.toString(),
            website = website_input.text.toStringOrNull(),
            bio = bio_input.text.toStringOrNull(),
            phone = phone_input.text.toString().toLongOrNull()
        )
    }


    override fun onPasswordConfirm(password: String) {
        if (password.isNotEmpty()) {
            val credential = EmailAuthProvider.getCredential(mUser.email, password)
            mAuth.currentUser!!.reauthenticate(credential) {
                mAuth.currentUser!!.updateEmail(mPendingUser.email) {
                    updateUser(mPendingUser)
                }
            }

        } else {
            showToast("You must enter your password!")
        }
    }

    private fun FirebaseUser.updateEmail(email: String, onSuccess: () -> Unit) {
        updateEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                showToast(it.exception!!.message!!)
            }
        }
    }


    private fun FirebaseUser.reauthenticate(credential: AuthCredential, onSuccess: () -> Unit) {
        reauthenticate(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                showToast(it.exception!!.message!!)
            }
        }
    }

    private fun updateUser(user: User) {
        val updatesMap = mutableMapOf<String, Any?>()
        if (user.name != mUser.name) updatesMap["name"] = user.name
        if (user.username != mUser.username) updatesMap["username"] = user.username
        if (user.website != mUser.website) updatesMap["website"] = user.website
        if (user.bio != mUser.bio) updatesMap["bio"] = user.bio
        if (user.email != mUser.email) updatesMap["email"] = user.email
        if (user.phone != mUser.phone) updatesMap["phone"] = user.phone

        mDatabase.updateUser(mAuth.currentUser!!.uid, updatesMap) {
            showToast("Profile updated and saved!")
            finish()
        }
    }

    private fun DatabaseReference.updateUser(
        uid: String,
        updates: Map<String, Any?>,
        onSuccess: () -> Unit
    ) {
        child("users").child(uid).updateChildren(updates)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    showToast(it.exception!!.message!!)
                }
            }
    }


    private fun validate(user: User): String? =
        when {
            user.name.isEmpty() -> "Please enter name"
            user.username.isEmpty() -> "Please enter username"
            user.email.isEmpty() -> "Please enter email"
            else -> null
        }


}


