package com.jean.cuidemonosaqp.modules.auth.ui.register

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jean.cuidemonosaqp.R
import com.jean.cuidemonosaqp.modules.auth.ui.register.RegisterViewModel
import com.jean.cuidemonosaqp.shared.theme.CuidemonosAQPTheme

@Composable
fun RegisterScreenHost(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel,
    navController: NavController?=null,
) {
    val launcherDniPhoto = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) viewModel.onDniImageSelected(it)
    }

    val launcherProfilePhoto = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) viewModel.onProfileImageSelected(it)
    }

    val firstName by viewModel.firstName.collectAsStateWithLifecycle()
    val lastName by viewModel.lastName.collectAsStateWithLifecycle()
    val dni by viewModel.dni.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val confirmPassword by viewModel.confirmPassword.collectAsStateWithLifecycle()
    val address by viewModel.address.collectAsStateWithLifecycle()
    val phoneNumber by viewModel.phoneNumber.collectAsStateWithLifecycle()
    val dniImageUri by viewModel.dniImageUri.collectAsStateWithLifecycle()
    val profileImageUri by viewModel.profileImageUri.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.registerSuccess.collect {
            navController?.popBackStack() // ← vuelve al login
        }
    }

    RegisterScreen(
        firstName = firstName,
        onFirstNameChanged = viewModel::onFirstNameChange,
        lastName = lastName,
        onLastNameChanged = viewModel::onLastNameChange,
        dni = dni,
        onDniChanged = viewModel::onDniChange,
        email = email,
        onEmailChanged = viewModel::onEmailChange,
        password = password,
        onPasswordChanged = viewModel::onPasswordChange,
        confirmPassword = confirmPassword,
        onConfirmPasswordChanged = viewModel::onConfirmPasswordChange,
        address = address,
        onAddressChanged = viewModel::onAddressChange,
        phoneNumber = phoneNumber,
        onPhoneNumberChanged = viewModel::onPhoneNumberChange,
        launcherDniPhoto = launcherDniPhoto,
        launcherProfilePhoto = launcherProfilePhoto,
        dniImageUri = dniImageUri,
        profileImageUri = profileImageUri,
        onRegisterButtonClick = {
            viewModel.onClickRegisterButton(context)
        }
    )
}

@Composable
fun RegisterScreen(
    firstName: String,
    onFirstNameChanged: (String) -> Unit,
    lastName: String,
    onLastNameChanged: (String) -> Unit,
    dni: String,
    onDniChanged: (String) -> Unit,
    email: String,
    onEmailChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChanged: (String) -> Unit,
    address: String,
    onAddressChanged: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChanged: (String) -> Unit,
    onRegisterButtonClick: () -> Unit,
    launcherDniPhoto: ManagedActivityResultLauncher<String, Uri?>,
    launcherProfilePhoto: ManagedActivityResultLauncher<String, Uri?>,
    dniImageUri: Uri?,
    profileImageUri: Uri?,

    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = firstName,
                onValueChange = onFirstNameChanged,
                label = { Text("Nombre") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = onLastNameChanged,
                label = { Text("Apellido") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true
            )

            OutlinedTextField(
                value = dni,
                onValueChange = onDniChanged,
                label = { Text("DNI") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChanged,
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChanged,
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChanged,
                label = { Text("Confirmar contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            OutlinedTextField(
                value = address,
                onValueChange = onAddressChanged,
                label = { Text("Dirección") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                singleLine = true
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChanged,
                label = { Text("Número telefónico") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )

            OutlinedButton(
                onClick = { launcherDniPhoto.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar foto del DNI")
            }

            dniImageUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Foto del DNI",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 8.dp)
                )
            }

            OutlinedButton(
                onClick = { launcherProfilePhoto.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar foto de perfil")
            }

            profileImageUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = onRegisterButtonClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = stringResource(R.string.auth_button_register))
            }
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview(){
}

