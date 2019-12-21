package com.example.instaprofile.util

import android.app.Activity
import android.net.Uri
import com.example.instaprofile.Activities.showToast
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class FirebaseHelper(private val activity: Activity) {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val mStorage: StorageReference = FirebaseStorage.getInstance().reference

     fun StorageReference.uploadUserPhoto(
        uid: String,
        photo: Uri,
        onSuccess: (UploadTask.TaskSnapshot) -> Unit
    ) {
        child("users/$uid/photo").putFile(photo).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess(it.result!!)
            } else {
                activity.showToast(it.exception!!.message!!)
            }
        }
    }

     fun DatabaseReference.updateUserPhoto(
        uid: String,
        photoUri: String,
        onSuccess: () -> Unit
    ) {
        child("users/$uid/photo").setValue(photoUri).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                activity.showToast(it.exception!!.message!!)
            }
        }
    }

     fun DatabaseReference.updateUser(
        uid: String,
        updates: Map<String, Any?>,
        onSuccess: () -> Unit
    ) {
        child("users").child(uid).updateChildren(updates)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    activity.showToast(it.exception!!.message!!)
                }
            }
    }

     fun FirebaseUser.updateEmail(email: String, onSuccess: () -> Unit) {
        updateEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                activity.showToast(it.exception!!.message!!)
            }
        }
    }

     fun FirebaseUser.reauthenticate(credential: AuthCredential, onSuccess: () -> Unit) {
        reauthenticate(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                activity.showToast(it.exception!!.message!!)
            }
        }
    }

    fun currentUserReference() = mDatabase.child("users").child(mAuth.currentUser!!.uid)

}
