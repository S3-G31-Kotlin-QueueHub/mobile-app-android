import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import kotlinx.coroutines.launch


class LoginViewModel(val usersRepository: UsersRepository): ViewModel(){

    private val _email = MutableLiveData<String>()
    var email : LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    var password : LiveData<String> = _password

    private val _validUser = MutableLiveData<Boolean>()
    val validUser: LiveData<Boolean> = _validUser

    fun auth(){
        viewModelScope.launch {

            val isValid = usersRepository.authUser("nicoastri@gmail.com","password")

        }
    }
    fun onLoginChange(email: String, password: String){
        _email.value = email
        _password.value = password
        _validUser.value = isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidPassword(password: String): Boolean {
        //TODO: VALIDAR PASSWORD
        return true
    }

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()


}