package com.queue_hub.isis3510_s3_g31.ui.screens.signup

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.queue_hub.isis3510_s3_g31.data.DataLayerFacade
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val dataLayerFacade: DataLayerFacade
): ViewModel() {

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

    private val _isConnected = MutableStateFlow<Boolean>(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val _validName = MutableLiveData<Boolean>()
    val validName: LiveData<Boolean> = _validName

    init {
        checkInternetConnection()
    }

    fun signUp() {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            try {

                if((_validEmail.value == true) && (_validPassword.value == true)){
                    val nameValue = _name.value.orEmpty()

                    if (!isValidName(nameValue)) {
                        _signUpState.value = SignUpState.Error("Name must not be empty and less than 20 characters")
                        return@launch
                    }

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

                    dataLayerFacade.signUp(emailValue, passwordValue, _phone.value.orEmpty(), _name.value.orEmpty())

                    _signUpState.value = SignUpState.Success

                }else{
                    _signUpState.value = SignUpState.Error("A valid email and a password larger than 6 digits is needed")
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("email", ignoreCase = true) == true ->
                        "This email has already register, please sign in or try with a different email."
                    else -> "Please check your internet connection and try again"
                }
                _signUpState.value = SignUpState.Error(errorMessage)
                Log.e(TAG, "Error en signUp", e)
            }
        }
    }

    private fun checkInternetConnection() {
        viewModelScope.launch {
            dataLayerFacade.checkNetworkConnection().collect { isConnected ->
                _isConnected.value = isConnected
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
        _validName.value = isValidName(name)
    }

    private fun isValidName(name: String): Boolean {
        return name.isNotEmpty() && name.length <= 20
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