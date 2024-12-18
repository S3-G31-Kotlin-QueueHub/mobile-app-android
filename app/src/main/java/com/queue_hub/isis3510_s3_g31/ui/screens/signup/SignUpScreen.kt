package com.queue_hub.isis3510_s3_g31.ui.screens.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
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
import androidx.compose.runtime.collectAsState
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
import com.google.firebase.firestore.FirebaseFirestore
import com.queue_hub.isis3510_s3_g31.R
import com.queue_hub.isis3510_s3_g31.ui.navigation.Login
import com.queue_hub.isis3510_s3_g31.ui.navigation.Main
import com.queue_hub.isis3510_s3_g31.ui.screens.login.ConnectivityBanner

@Composable
fun SignUpScreen(viewModel: SignUpViewModel, navController: NavController){
    val signUpState by viewModel.signUpState.observeAsState(SignUpState.Idle)


    LaunchedEffect(signUpState) {
        when (signUpState) {
            is SignUpState.Success -> {
                navController.navigate(Main){
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }

            }
            else -> {} // No hacer nada para otros estados
        }
    }


    Box(
        Modifier
            .fillMaxSize()
            .background(colorScheme.background) // El fondo cubre toda la pantalla
    ) {

        Box(
            Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            SignUp(Modifier.align(Alignment.Center), viewModel, navController, signUpState)
        }
    }
}

@Composable
fun SignUp(
    modifier: Modifier,
    viewModel: SignUpViewModel,
    navController: NavController,
    signUpState: SignUpState
) {

    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val passwordConfirmation: String by viewModel.passwordConfirmation.observeAsState(initial = "")
    val name: String by viewModel.name.observeAsState(initial = "")
    val phone: String by viewModel.phone.observeAsState(initial = "")
    val isConnected by viewModel.isConnected.collectAsState(true)
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HeaderImage()
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = stringResource(id = R.string.header_text),
            style = MaterialTheme.typography.titleMedium,
        )

        if (!isConnected) {
            Spacer(modifier = Modifier.height(12.dp))
            ConnectivityBanner()
        }
        when (signUpState) {
            is SignUpState.Loading -> {
                CircularProgressIndicator()
            }
            is SignUpState.Error -> {
                Spacer(modifier = Modifier.height(12.dp))
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
        Spacer(modifier = Modifier.padding(8.dp))
        EmailField(email) { viewModel.onSignUpEmailChange(it) }
        Spacer(modifier = Modifier.padding(4.dp))
        NameField(name) { viewModel.onSignUpNameChange(it) }
        Spacer(modifier = Modifier.padding(4.dp))
        PhoneField(phone) { viewModel.onSignUpPhoneChange(it) }
        Spacer(modifier = Modifier.padding(4.dp))
        PasswordField(stringResource(id = R.string.password), password) { viewModel.onSignUpPasswordChange(it) }
        Spacer(modifier = Modifier.padding(4.dp))
        PasswordField(stringResource(id = R.string.confirm_password) , passwordConfirmation) { viewModel.onSignUpPasswordConfirmationChange(it) }
        Spacer(modifier = Modifier.padding(8.dp))
        SignUpButton(viewModel)

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
fun ConnectivityBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorScheme.errorContainer)
    ) {
        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ){
            Icon(
                imageVector = Icons.Rounded.Info,
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp),
                tint = colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "No internet connection",
                color = colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}

@Composable
fun LoginButton(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(Login)

        },
        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.secondary)
    ) {
        Text(
            text = stringResource(R.string.sign_in),
            color = colorScheme.onSecondary
        )
    }
}


@Composable
fun SignUpButton(viewModel: SignUpViewModel){
    Button(
        onClick = {
            viewModel.signUp()
        },
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = stringResource(R.string.sign_up)
        )
    }
}


@Composable
fun PasswordField(placeholder: String, password: String, onTextFieldChange: (String) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }

        TextField(
            shape = RectangleShape,
            value = password,
            onValueChange = {onTextFieldChange(it)},
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text= placeholder)},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done),
            singleLine = true,
            maxLines = 1,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = if (passwordVisible) painterResource(id =  R.drawable.baseline_visibility_off_24) else painterResource(id = R.drawable.round_visibility_24),
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            )
        )

}

@Composable
fun EmailField(email: String, onTextFieldChange:(String) -> Unit ) {


        TextField(
            shape = RectangleShape,
            value = email,
            onValueChange = { onTextFieldChange(it) },
            modifier = Modifier.fillMaxWidth(),
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
fun NameField(name: String, onTextFieldChange:(String) -> Unit ) {


        TextField(
            shape = RectangleShape,
            value = name,
            onValueChange = { onTextFieldChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = stringResource(id = R.string.name)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            )
        )

}

@Composable
fun PhoneField(phone: String, onTextFieldChange:(String) -> Unit ) {

        TextField(
            shape = RectangleShape,
            value = phone,
            onValueChange = { onTextFieldChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = stringResource(id = R.string.phone)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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

