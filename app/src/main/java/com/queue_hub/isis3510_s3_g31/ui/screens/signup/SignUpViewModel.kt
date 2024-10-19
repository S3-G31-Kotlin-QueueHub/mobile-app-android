package com.queue_hub.isis3510_s3_g31.ui.screens.signup

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.queue_hub.isis3510_s3_g31.data.model.User
import kotlinx.coroutines.launch

class SignUpViewModel(auth: FirebaseAuth): ViewModel() {



    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _passwordConfirmation = MutableLiveData<String>()
    val passwordConfirmation: LiveData<String> = _passwordConfirmation

    private val _phone = MutableLiveData<String>()
    val phone: LiveData<String> = _phone

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _validPassword = MutableLiveData<Boolean>()
    val validPassword: LiveData<Boolean> = _validPassword

    private val _validEmail = MutableLiveData<Boolean>()
    val validEmail: LiveData<Boolean> = _validEmail

    private val _validPhone = MutableLiveData<Boolean>()
    val validPhone: LiveData<Boolean> = _validPhone

    private val _signUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> = _signUpState

    fun signUp(auth: FirebaseAuth, db: FirebaseFirestore) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            try {


                if((_validEmail.value == true) && (_validPassword.value == true)){
                    val emailValue = _email.value.orEmpty()
                    val passwordValue = _password.value.orEmpty()

                    if(!isValidPasswordConfirmation(password.value.orEmpty(), passwordConfirmation.value.orEmpty())){
                        _signUpState.value = SignUpState.Error("Passwords do not match")
                        return@launch
                    }
                    if(_validPhone.value == false){
                        _signUpState.value = SignUpState.Error("Invalid phone number")
                        return@launch
                    }

                    auth.createUserWithEmailAndPassword(emailValue, passwordValue)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                if (task.isSuccessful) {

                                    val userId = auth.currentUser?.uid

                                    val user = User(
                                        name = name.value.orEmpty(),
                                        email = email.value.orEmpty(),
                                        phone = phone.value.orEmpty(),
                                        isAdmin = false,
                                        createdAt = Timestamp.now()
                                    )

                                    userId?.let { uid ->
                                        db.collection("users")
                                            .document(uid)
                                            .set(user)
                                            .addOnSuccessListener {
                                                _signUpState.value = SignUpState.Success
                                            }
                                            .addOnFailureListener { e ->
                                                _signUpState.value =
                                                    SignUpState.Error("Error saving user data: ${e.message}")
                                            }
                                    }
                                }
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

    fun onSignUpPasswordConfirmationChange(passwordConfirmation: String) {
        _passwordConfirmation.value = passwordConfirmation
        _validPassword.value = isValidPassword(passwordConfirmation)
    }

    fun onSignUpEmailChange(email: String) {
        _email.value = email
        _validEmail.value = isValidEmail(email)
    }

    fun onSignUpPhoneChange(phone: String) {
        _phone.value = phone
        _validPhone.value = isValidPhone(phone)
    }

    fun onSignUpNameChange(name: String) {
        _name.value = name
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    private fun isValidPasswordConfirmation(password: String, passwordConfirmation: String): Boolean {
        return password == passwordConfirmation
    }

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPhone(phone: String): Boolean {
        return phone.length == 10 && phone.all { it.isDigit() }
    }
}

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}