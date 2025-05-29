package com.jean.cuidemonosaqp.features.auth.presentation.register

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jean.cuidemonosaqp.shared.presentation.theme.CuidemonosAQPTheme

@Composable
private fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val dniPhotoPicker = rememberLauncherForActivityResult(GetContent()) { uri: Uri? ->
        onAction(RegisterAction.OnDniPhotoSelected(uri))
    }
    val profilePhotoPicker = rememberLauncherForActivityResult(GetContent()) { uri: Uri? ->
        onAction(RegisterAction.OnProfilePhotoSelected(uri))
    }

    Column(
        modifier = modifier
            .padding(20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = state.name,
            onValueChange = { onAction(RegisterAction.OnNameChanged(it)) },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { onAction(RegisterAction.OnEmailChanged(it)) },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { onAction(RegisterAction.OnPasswordChanged(it)) },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = { onAction(RegisterAction.OnConfirmPasswordChanged(it)) },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = state.address,
            onValueChange = { onAction(RegisterAction.OnAddressChanged(it)) },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = state.phoneNumber,
            onValueChange = { onAction(RegisterAction.OnPhoneNumberChanged(it)) },
            label = { Text("Número de teléfono") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(Modifier.height(20.dp))

        Text(text = "Foto de DNI")
        Spacer(Modifier.height(5.dp))

        if (state.dniPhotoUri != null) {
            Image(
                painter = rememberAsyncImagePainter(state.dniPhotoUri),
                contentDescription = "Foto DNI",
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
        }
        Button(
            onClick = { dniPhotoPicker.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (state.dniPhotoUri == null) "Seleccionar foto DNI" else "Cambiar foto DNI")
        }

        Spacer(Modifier.height(20.dp))

        Text(text = "Foto de perfil")
        Spacer(Modifier.height(5.dp))

        if (state.profilePhotoUri != null) {
            Image(
                painter = rememberAsyncImagePainter(state.profilePhotoUri),
                contentDescription = "Foto perfil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Button(
            onClick = { profilePhotoPicker.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (state.profilePhotoUri == null) "Seleccionar foto perfil" else "Cambiar foto perfil")
        }

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = { onAction(RegisterAction.OnRegisterClicked) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
    }
}

@Composable
fun RegisterScreenHost() {
    val viewModel: RegisterViewModel = viewModel()
    val state = viewModel.state.collectAsState()

    RegisterScreen(
        state = state.value,
        onAction = viewModel::onAction
    )
}

@Composable
@Preview(showBackground = true)
fun RegisterScreenPreview() {
    CuidemonosAQPTheme(dynamicColor = false) {
        RegisterScreen(state = RegisterState(), onAction = {})
    }
}