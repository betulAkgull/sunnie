package com.example.weatherapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.repository.UserRepo
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: UserRepo) : ViewModel() {

    private var _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState>
        get() = _authState

    val currentUser: FirebaseUser?
        get() = repo.currentUser

    fun signInUser(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = repo.signInUser(email, password)

            if (result is Resource.Success) {
                _authState.value = AuthState.Data(result.data)
            } else if (result is Resource.Error) {
                _authState.value = AuthState.Error(result.throwable)
            }
        }
    }

    fun signUpUser(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = repo.signUpUser(email, password)

            if (result is Resource.Success) {
                _authState.value = AuthState.Data(result.data)
            } else if (result is Resource.Error) {
                _authState.value = AuthState.Error(result.throwable)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
        }
    }

}

sealed interface AuthState {
    object Loading : AuthState
    data class Data(val user: FirebaseUser) : AuthState
    data class Error(val throwable: Throwable) : AuthState
}