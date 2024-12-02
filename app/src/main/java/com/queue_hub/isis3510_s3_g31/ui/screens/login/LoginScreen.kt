package com.queue_hub.isis3510_s3_g31.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
import com.queue_hub.isis3510_s3_g31.ui.navigation.Main
import com.queue_hub.isis3510_s3_g31.ui.navigation.SignUp

@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController, auth: FirebaseAuth) {
    val loginState by viewModel.loginState.observeAsState(LoginState.Idle)

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            navController.navigate(Main) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(20.dp)
    ) {
        Login(
            modifier = Modifier.align(Alignment.Center),
            viewModel = viewModel,
            navController = navController,
            loginState = loginState
        )
    }
}

@Composable
fun Login(
    modifier: Modifier,
    viewModel: LoginViewModel,
    navController: NavController,
    loginState: LoginState
) {
    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HeaderImage()
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.header_text),
            style = MaterialTheme.typography.titleMedium,
        )

        when (loginState) {
            is LoginState.Loading -> CircularProgressIndicator()
            is LoginState.Error -> {
                Text(
                    text = (loginState as LoginState.Error).message,
                    color = colorScheme.error,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
            else -> Spacer(modifier = Modifier.height(12.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 15.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.login),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            EmailField(email) { viewModel.onLoginChange(it, password) }
            Spacer(modifier = Modifier.height(8.dp))
            PasswordField(password) { viewModel.onLoginChange(email, it) }
            Spacer(modifier = Modifier.height(24.dp))
            LoginButton(viewModel)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.signUpText),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        SignUpButton(navController)
    }
}

@Composable
fun LoginButton(viewModel: LoginViewModel) {
    Button(
        onClick = {
            viewModel.authenticateUsers()
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
        onValueChange = { onTextFieldChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        shape = RectangleShape,
        placeholder = { Text(text = stringResource(id = R.string.password)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        maxLines = 1,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painter = if (passwordVisible)
                        painterResource(id = R.drawable.baseline_visibility_off_24)
                    else
                        painterResource(id = R.drawable.round_visibility_24),
                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                )
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor =  Color.Transparent,
            focusedContainerColor =  Color.Transparent,
        )
    )
}

@Composable
fun EmailField(email: String, onTextFieldChange: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = { onTextFieldChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        shape = RectangleShape,
        placeholder = { Text(text = stringResource(id = R.string.email)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
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

