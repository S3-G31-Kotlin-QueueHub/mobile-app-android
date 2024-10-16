import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun auth() {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val isValid = usersRepository.authUser(_email.value ?: "", _password.value ?: "")
                println("Valid: $isValid")
                if (isValid) {
                    _loginState.value = LoginState.Success
                } else {
                    _loginState.value = LoginState.Error("Invalid Credentials")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error de autenticación: ${e.message}")
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