package com.queue_hub.isis3510_s3_g31.ui.screens.signup

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SignUpViewModel(auth: FirebaseAuth): ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _validPassword = MutableLiveData<Boolean>()
    val validPassword: LiveData<Boolean> = _validPassword

    private val _validEmail = MutableLiveData<Boolean>()
    val validEmail: LiveData<Boolean> = _validEmail

    private val _signUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> = _signUpState

    fun signUp(auth: FirebaseAuth) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            try {
                if((_validEmail.value == true) && (_validPassword.value == true)){
                    val emailValue = _email.value.orEmpty()
                    val passwordValue = _password.value.orEmpty()
                    auth.createUserWithEmailAndPassword(emailValue, passwordValue)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                _signUpState.value = SignUpState.Success
                            } else {
                                _signUpState.value = SignUpState.Error("This email has already register, please sign in or try with a different email.")
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            }
                        }
                }else{
                    _signUpState.value = SignUpState.Error("A valid email and a password larger than 6 digits is needed")
                }
            } catch (e: Exception) {
                _signUpState.value = SignUpState.Error("Sign Up error: ${e.message}")
            }
        }
    }




    fun onSignUpPasswordChange(password: String) {
        _password.value = password
        _validPassword.value = isValidPassword(password)
    }

    fun onSignUpEmailChange(email: String) {
        _email.value = email
        _validEmail.value = isValidEmail(email)
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}