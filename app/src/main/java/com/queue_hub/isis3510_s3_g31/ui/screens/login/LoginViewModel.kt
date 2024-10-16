import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val usersRepository: UsersRepository): ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _validUser = MutableLiveData<Boolean>()
    val validUser: LiveData<Boolean> = _validUser

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun authenticateUsers(auth: FirebaseAuth) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val emailValue = _email.value.orEmpty()
            val passwordValue = _password.value.orEmpty()

            if(validUser.value != true){
                if(emailValue == "" && passwordValue == ""){
                    _loginState.value = LoginState.Error("Please fill the email and password field")
                }else if(passwordValue == ""){
                    _loginState.value = LoginState.Error("Please fill the password field")
                }else if(emailValue == ""){
                    _loginState.value = LoginState.Error("Please fill the email field")
                }else{
                    _loginState.value = LoginState.Error("Your sign in credentials are invalid, please check and try again")
                }
            }else{
                try {
                    auth.signInWithEmailAndPassword(emailValue, passwordValue)
                        .addOnCompleteListener{ task ->
                            if (task.isSuccessful) {
                                _loginState.value = LoginState.Success
                            } else {
                                _loginState.value = LoginState.Error("Your sign in credentials are incorrect, please check and try again")
                            }
                        }
                } catch (e: Exception) {
                    _loginState.value = LoginState.Error("Authentication Error: ${e.message}")
                }
            }
        }
    }

    fun onLoginChange(email: String, password: String) {
        _email.value = email
        _password.value = password
        _validUser.value = isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}