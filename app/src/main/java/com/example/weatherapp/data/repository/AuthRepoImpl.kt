package com.example.weatherapp.data.repository

import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.utils.await
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : UserRepo {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun signInUser(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }

    override suspend fun signUpUser(
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(UserProfileChangeRequest.Builder().build())?.await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }

    override suspend fun signInWithGoogle(credential: AuthCredential): Resource<AuthResult> {

        return try {
            val result = firebaseAuth.signInWithCredential(credential).await()
            Resource.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e)
        }

    }


    override fun logout() {
        firebaseAuth.signOut()
    }
}

interface UserRepo {
    val currentUser: FirebaseUser?
    suspend fun signInWithGoogle(credential: AuthCredential): Resource<AuthResult>
    suspend fun signInUser(email: String, password: String): Resource<FirebaseUser>
    suspend fun signUpUser(email: String, password: String): Resource<FirebaseUser>
    fun logout()
}