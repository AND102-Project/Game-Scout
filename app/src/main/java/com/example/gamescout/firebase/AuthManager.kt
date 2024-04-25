package com.example.gamescout.firebase

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

class AuthManager {

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }


    fun signInWithGoogle(activity: Activity): Intent {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            //AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
    }


    fun signInWithEmailPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign-in success, update UI with the signed-in user's information
                    val user = firebaseAuth.currentUser
                    Timber.d("signInWithEmailPassword:success, user: $user")
                    onSuccess() // Call the success callback
                } else {
                    // If sign-in fails, display a message to the user.
                    Timber.e("signInWithEmailPassword:failure, ${task.exception}")
                    onFailure() // Call the failure callback
                }
            }
    }

    fun createUserWithEmailAndPassword(email: String,
                                       password: String,
                                       onSuccess: () -> Unit,
                                       onFailure: () -> Unit){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Timber.d(TAG, "createUserWithEmail:success")
                val user = firebaseAuth.currentUser
                onSuccess() // Call the success callback
            } else {
                // If sign in fails, display a message to the user.
                Timber.e("createUserWithEmailPassword:failure, ${task.exception}")
                onFailure() // Call the failure callback
            }
        }
    }


    fun signOut() {
        firebaseAuth.signOut()
        Timber.d("User signed out from Firebase")
    }

    companion object {
        const val RC_SIGN_IN = 123
    }

    // Other authentication methods...
}
