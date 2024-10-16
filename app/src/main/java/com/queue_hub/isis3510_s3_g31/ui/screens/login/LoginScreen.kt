package com.queue_hub.isis3510_s3_g31.ui.screens.login

import LoginState
import LoginViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.queue_hub.isis3510_s3_g31.R
import com.queue_hub.isis3510_s3_g31.navigation.Detail
import com.queue_hub.isis3510_s3_g31.navigation.Main
import com.queue_hub.isis3510_s3_g31.navigation.SignUp

@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController, auth: FirebaseAuth){
    val loginState by viewModel.loginState.observeAsState(LoginState.Idle)
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                navController.navigate(Main)
            }
            else -> {} // No hacer nada para otros estados
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .padding(20.dp) ) {

        Login(Modifier.align(Alignment.Center), viewModel, navController, loginState, auth)


    }
}

@Composable
fun Login(
    modifier: Modifier,
    viewModel: LoginViewModel,
    navController: NavController,
    loginState: LoginState,
    auth: FirebaseAuth
) {

    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HeaderImage()
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = stringResource(id = R.string.login),
            style = MaterialTheme.typography.headlineLarge,
            
        )
        when (loginState) {
            is LoginState.Loading -> {
                CircularProgressIndicator()
            }
            is LoginState.Error -> {
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = (loginState as LoginState.Error).message,
                        color = colorScheme.error,
                    )
                }

            }
            else -> {}
        }
        Spacer(modifier = Modifier.padding(16.dp))
        EmailField(email) { viewModel.onLoginChange(it, password) }
        Spacer(modifier = Modifier.padding(4.dp))
        PasswordField(password) { viewModel.onLoginChange(email, it) }
        Spacer(modifier = Modifier.padding(8.dp))
        LoginButton(navController, viewModel, auth)
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = stringResource(R.string.signUpText),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.padding(8.dp))
        SignUpButton(navController)
    }
}

@Composable
fun LoginButton(navController: NavController, viewModel: LoginViewModel, auth: FirebaseAuth) {
    Button(
        onClick = {
            viewModel.authenticateUsers(auth)
        },
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = stringResource(id = R.string.login)
        )
    }
}

@Composable
fun SignUpButton(navController: NavController){
    Button(
        onClick = {
            navController.navigate(SignUp)
        },
        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.secondary)
    ){
        Text(
            text = stringResource(R.string.sign_up),
            color = colorScheme.onSecondary
        )
    }
}


@Composable
fun PasswordField(password: String, onTextFieldChange: (String) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = password,
        onValueChange = {onTextFieldChange(it)},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text= stringResource(id = R.string.password) )},
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done),
        singleLine = true,
        maxLines = 1,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Done else Icons.Default.Face,
                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                )
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colorScheme.onPrimary,
            focusedContainerColor = colorScheme.onPrimary,
        )
    )
}

@Composable
fun EmailField(email: String, onTextFieldChange:(String) -> Unit ) {

    TextField(
        value = email,
        onValueChange = { onTextFieldChange(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text= stringResource(id = R.string.email) )},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colorScheme.onPrimary,
            focusedContainerColor = colorScheme.onPrimary,
        )
    )
}



@Composable
fun HeaderImage() {
    val logoImage = painterResource(R.drawable.queuehub_logo)
    Image(
        painter = logoImage,
        contentDescription = "Banner image",
    )
}

