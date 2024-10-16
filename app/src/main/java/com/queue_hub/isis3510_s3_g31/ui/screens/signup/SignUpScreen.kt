package com.queue_hub.isis3510_s3_g31.ui.screens.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
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
import com.queue_hub.isis3510_s3_g31.navigation.Login
import com.queue_hub.isis3510_s3_g31.navigation.Main

@Composable
fun SignUpScreen(viewModel: SignUpViewModel, navController: NavController, auth: FirebaseAuth){
    val signUpState by viewModel.signUpState.observeAsState(SignUpState.Idle)
    LaunchedEffect(signUpState) {
        when (signUpState) {
            is SignUpState.Success -> {
                navController.navigate(Main)
            }
            else -> {} // No hacer nada para otros estados
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .padding(20.dp) ) {

        SignUp(Modifier.align(Alignment.Center), viewModel, navController, signUpState, auth)


    }
}

@Composable
fun SignUp(
    modifier: Modifier,
    viewModel: SignUpViewModel,
    navController: NavController,
    signUpState: SignUpState,
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
        when (signUpState) {
            is SignUpState.Loading -> {
                CircularProgressIndicator()
            }
            is SignUpState.Error -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = (signUpState as SignUpState.Error).message,
                        color = colorScheme.error,
                    )
                }

            }
            else -> {}
        }
        Spacer(modifier = Modifier.padding(16.dp))
        EmailField(email) { viewModel.onSignUpEmailChange(it) }
        Spacer(modifier = Modifier.padding(4.dp))
        PasswordField(password) { viewModel.onSignUpPasswordChange(it) }
        Spacer(modifier = Modifier.padding(8.dp))
        SignUpButton(viewModel, auth)

        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = stringResource(R.string.SignInText),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.padding(8.dp))
        LoginButton(navController)
    }
}

@Composable
fun LoginButton(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(Login)
        }
    ){
        Text(
            text = stringResource(R.string.sign_in)
        )
    }
}

@Composable
fun SignUpButton(viewModel: SignUpViewModel, auth: FirebaseAuth){
    Button(
        onClick = {
            viewModel.signUp(auth)
        },
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = stringResource(R.string.sign_up)
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

