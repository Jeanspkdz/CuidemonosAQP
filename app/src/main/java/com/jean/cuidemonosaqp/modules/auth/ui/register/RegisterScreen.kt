@file:OptIn(ExperimentalMaterial3Api::class)

package com.jean.cuidemonosaqp.modules.auth.ui.register

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.jean.cuidemonosaqp.R
import com.jean.cuidemonosaqp.shared.components.PasswordTextField
import com.jean.cuidemonosaqp.shared.theme.CuidemonosAQPTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun RegisterScreenHost(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    // Estados del ViewModel
    val dni by viewModel.dni.collectAsStateWithLifecycle()
    val firstName by viewModel.firstName.collectAsStateWithLifecycle()
    val lastName by viewModel.lastName.collectAsStateWithLifecycle()
    val dniExtension by viewModel.dniExtension.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val confirmPassword by viewModel.confirmPassword.collectAsStateWithLifecycle()
    val phone by viewModel.phone.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val address by viewModel.address.collectAsStateWithLifecycle()
    val reputationStatusId by viewModel.reputationStatusId.collectAsStateWithLifecycle()
    val profilePhotoUri by viewModel.profilePhotoUri.collectAsStateWithLifecycle()
    val dniPhotoUri by viewModel.dniPhotoUri.collectAsStateWithLifecycle()
    val registerState by viewModel.registerState.collectAsStateWithLifecycle()
    val passwordsMatch by viewModel.passwordsMatch.collectAsStateWithLifecycle()
    val locationSelected by viewModel.locationSelected.collectAsStateWithLifecycle() // NUEVO
    val addressLatitude by viewModel.addressLatitude.collectAsStateWithLifecycle() // NUEVO
    val addressLongitude by viewModel.addressLongitude.collectAsStateWithLifecycle() // NUEVO

    val context = LocalContext.current

    // Selectores de imagen
    val profilePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> viewModel.onProfilePhotoSelected(uri) }

    val dniPhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> viewModel.onDniPhotoSelected(uri) }

    // Manejar los estados del registro
    LaunchedEffect(registerState) {
        when {
            registerState.success -> {
                Toast.makeText(context, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
                onRegisterSuccess()
            }
            registerState.error != null -> {
                Toast.makeText(context, registerState.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    RegisterScreen(
        dni = dni,
        onDniChanged = viewModel::onDniChanged,
        firstName = firstName,
        onFirstNameChanged = viewModel::onFirstNameChanged,
        lastName = lastName,
        onLastNameChanged = viewModel::onLastNameChanged,
        dniExtension = dniExtension,
        onDniExtensionChanged = viewModel::onDniExtensionChanged,
        password = password,
        onPasswordChanged = viewModel::onPasswordChanged,
        confirmPassword = confirmPassword,
        onConfirmPasswordChanged = viewModel::onConfirmPasswordChanged,
        passwordsMatch = passwordsMatch,
        phone = phone,
        onPhoneChanged = viewModel::onPhoneChanged,
        email = email,
        onEmailChanged = viewModel::onEmailChanged,
        address = address,
        onAddressChanged = viewModel::onAddressChanged,
        reputationStatusId = reputationStatusId,
        onReputationStatusIdChanged = viewModel::onReputationStatusIdChanged,
        profilePhotoUri = profilePhotoUri,
        onPickProfilePhoto = { profilePhotoPicker.launch("image/*") },
        dniPhotoUri = dniPhotoUri,
        onPickDniPhoto = { dniPhotoPicker.launch("image/*") },
        onRegisterButtonClick = viewModel::onRegisterClicked,
        onLoginClick = onNavigateToLogin,
        isLoading = registerState.isLoading,
        // NUEVOS parámetros para el mapa
        locationSelected = locationSelected,
        addressLatitude = addressLatitude,
        addressLongitude = addressLongitude,
        onLocationSelected = viewModel::onLocationSelected,
        onClearLocation = viewModel::clearLocation,
        modifier = modifier
    )
}

@Composable
fun RegisterScreen(
    dni: String,
    onDniChanged: (String) -> Unit,
    firstName: String,
    onFirstNameChanged: (String) -> Unit,
    lastName: String,
    onLastNameChanged: (String) -> Unit,
    dniExtension: String,
    onDniExtensionChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChanged: (String) -> Unit,
    passwordsMatch: Boolean,
    phone: String,
    onPhoneChanged: (String) -> Unit,
    email: String,
    onEmailChanged: (String) -> Unit,
    address: String,
    onAddressChanged: (String) -> Unit,
    reputationStatusId: String,
    onReputationStatusIdChanged: (String) -> Unit,
    profilePhotoUri: Uri?,
    onPickProfilePhoto: () -> Unit,
    dniPhotoUri: Uri?,
    onPickDniPhoto: () -> Unit,
    onRegisterButtonClick: () -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean = false,
    // NUEVOS parámetros
    locationSelected: Boolean,
    addressLatitude: Double?,
    addressLongitude: Double?,
    onLocationSelected: (Double, Double) -> Unit,
    onClearLocation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMapDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        // Botón de volver en la parte superior izquierda
        IconButton(
            onClick = onLoginClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
                .zIndex(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Volver al login",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 20.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "Logo App",
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                    Text(
                        text = stringResource(R.string.register_subtitle),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.inverseSurface.copy(
                                alpha = 0.7f
                            )
                        )
                    )
                }
            }

            // Campos de texto
            item {
                Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    // DNI
                    Column {
                        Text(
                            text = stringResource(R.string.register_dni_label),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        OutlinedTextField(
                            value = dni,
                            onValueChange = onDniChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.register_dni_placeholder),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            singleLine = true,
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Nombre
                    Column {
                        Text(
                            text = stringResource(R.string.register_firstname_label),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = onFirstNameChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.register_firstname_placeholder),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            singleLine = true,
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Apellido
                    Column {
                        Text(
                            text = stringResource(R.string.register_lastname_label),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        OutlinedTextField(
                            value = lastName,
                            onValueChange = onLastNameChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.register_lastname_placeholder),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            singleLine = true,
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Extensión DNI (opcional)
                    Column {
                        Text(
                            text = stringResource(R.string.register_dni_extension_label),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        OutlinedTextField(
                            value = dniExtension,
                            onValueChange = onDniExtensionChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.register_dni_extension_placeholder),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            singleLine = true,
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Email
                    Column {
                        Text(
                            text = stringResource(R.string.register_email_label),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = onEmailChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.register_email_placeholder),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Contraseña
                    Column {
                        Text(
                            text = stringResource(R.string.register_password_label),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        PasswordTextField(
                            value = password,
                            onValueChange = onPasswordChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.register_password_placeholder),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Confirmar Contraseña
                    Column {
                        Text(
                            text = stringResource(R.string.register_confirm_password_label),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        PasswordTextField(
                            value = confirmPassword,
                            onValueChange = onConfirmPasswordChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.register_confirm_password_placeholder),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            enabled = !isLoading,
                            isError = confirmPassword.isNotEmpty() && !passwordsMatch,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (confirmPassword.isNotEmpty() && !passwordsMatch) {
                            Text(
                                text = "Las contraseñas no coinciden",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }

                    // Teléfono
                    Column {
                        Text(
                            text = stringResource(R.string.register_phone_label),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        OutlinedTextField(
                            value = phone,
                            onValueChange = onPhoneChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.register_phone_placeholder),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Dirección
                    Column {
                        Text(
                            text = stringResource(R.string.register_address_label),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        OutlinedTextField(
                            value = address,
                            onValueChange = onAddressChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.register_address_placeholder),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            singleLine = true,
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // NUEVO: Sección de ubicación
            item {
                LocationSelectionCard(
                    locationSelected = locationSelected,
                    latitude = addressLatitude,
                    longitude = addressLongitude,
                    onSelectLocation = { showMapDialog = true },
                    onClearLocation = onClearLocation,
                    isLoading = isLoading
                )
            }

            // Selección de fotos
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = stringResource(R.string.register_photos_section),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )

                    PhotoSelectionCard(
                        title = stringResource(R.string.register_dni_photo_label),
                        photoUri = dniPhotoUri,
                        onSelectPhoto = onPickDniPhoto,
                        isLoading = isLoading
                    )

                    PhotoSelectionCard(
                        title = stringResource(R.string.register_profile_photo_label),
                        photoUri = profilePhotoUri,
                        onSelectPhoto = onPickProfilePhoto,
                        isLoading = isLoading
                    )
                }
            }

            // Botón de registro
            item {
                Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    Button(
                        onClick = onRegisterButtonClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        contentPadding = PaddingValues(vertical = 15.dp),
                        shape = RoundedCornerShape(10.dp),
                        enabled = !isLoading && isFormValid(dni, firstName, lastName, email, password, confirmPassword, phone, address) && passwordsMatch && locationSelected,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            Text(text = stringResource(R.string.register_button_register))
                        }
                    }
                }
            }
        }

        // Dialog del mapa
        if (showMapDialog) {
            MapDialog(
                onLocationSelected = { lat, lng ->
                    onLocationSelected(lat, lng)
                    showMapDialog = false
                },
                onDismiss = { showMapDialog = false },
                initialLatitude = addressLatitude,
                initialLongitude = addressLongitude
            )
        }
    }
}

// NUEVO: Componente para mostrar el estado de ubicación
@Composable
private fun LocationSelectionCard(
    locationSelected: Boolean,
    latitude: Double?,
    longitude: Double?,
    onSelectLocation: () -> Unit,
    onClearLocation: () -> Unit,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (locationSelected)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Ubicación",
                    tint = if (locationSelected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Ubicación en el mapa",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (locationSelected && latitude != null && longitude != null) {
                Text(
                    text = "Lat: ${String.format("%.6f", latitude)}\nLng: ${String.format("%.6f", longitude)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onSelectLocation,
                        enabled = !isLoading,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cambiar ubicación")
                    }

                    OutlinedButton(
                        onClick = onClearLocation,
                        enabled = !isLoading,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Limpiar")
                    }
                }
            } else {
                Text(
                    text = "Toca el botón para seleccionar tu ubicación en el mapa",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onSelectLocation,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Seleccionar en mapa")
                }
            }
        }
    }
}

@Composable
private fun PhotoSelectionCard(
    title: String,
    photoUri: Uri?,
    onSelectPhoto: () -> Unit,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (photoUri != null) {
                AsyncImage(
                    model = photoUri,
                    contentDescription = title,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedButton(
                onClick = onSelectPhoto,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (photoUri != null)
                        stringResource(R.string.register_change_photo)
                    else
                        stringResource(R.string.register_select_photo)
                )
            }
        }
    }
}

private fun isFormValid(
    dni: String,
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    confirmPassword: String,
    phone: String,
    address: String
): Boolean {
    return dni.isNotBlank() &&
            firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank() &&
            phone.isNotBlank() &&
            address.isNotBlank()
}

@Composable
private fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        singleLine = true,
        enabled = enabled,
        isError = isError,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                )
            }
        },
        modifier = modifier
    )
}