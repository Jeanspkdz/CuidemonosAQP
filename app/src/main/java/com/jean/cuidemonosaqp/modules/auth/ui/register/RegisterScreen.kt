@file:OptIn(ExperimentalMaterial3Api::class)

package com.jean.cuidemonosaqp.modules.auth.ui.register

import android.content.ContentResolver
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.jean.cuidemonosaqp.shared.utils.uriToPart
import kotlinx.coroutines.flow.collectLatest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit
) {
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    // Estado de la pantalla (loading, success, error)
    val state by viewModel.registerState.collectAsState()

    // Campos del formulario
    var dni by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dniExtension by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var reputationStatusId by remember { mutableStateOf("1") }

    // Imagenes seleccionadas como Uri
    var profilePhotoUri by remember { mutableStateOf<Uri?>(null) }
    var dniPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // Lanzadores de selección de imagen
    val profilePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> profilePhotoUri = uri }

    val dniPhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> dniPhotoUri = uri }

    // Mostrar Toast en caso de éxito
    if (state.success) {
        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_LONG).show()
        LaunchedEffect(Unit) {
            onRegisterSuccess()
        }
    }

    // Mostrar Toast en caso de error
    state.error?.let {
        Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
    }

    Column(Modifier.padding(16.dp)) {

        // Campos de texto para datos personales
        OutlinedTextField(dni, { dni = it }, label = { Text("DNI") })
        OutlinedTextField(firstName, { firstName = it }, label = { Text("Nombre") })
        OutlinedTextField(lastName, { lastName = it }, label = { Text("Apellido") })
        OutlinedTextField(dniExtension, { dniExtension = it }, label = { Text("Extensión DNI (opcional)") })
        OutlinedTextField(password, { password = it }, label = { Text("Contraseña") })
        OutlinedTextField(phone, { phone = it }, label = { Text("Teléfono") })
        OutlinedTextField(email, { email = it }, label = { Text("Correo electrónico") })
        OutlinedTextField(address, { address = it }, label = { Text("Dirección") })
        OutlinedTextField(reputationStatusId, { reputationStatusId = it }, label = { Text("ID reputación") })

        Spacer(modifier = Modifier.height(12.dp))

        // Botón para seleccionar imagen de DNI
        Button(onClick = { dniPhotoPicker.launch("image/*") }) {
            Text("Seleccionar foto de DNI")
        }

        // Previsualización de imagen de DNI
        dniPhotoUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier.size(80.dp).padding(top = 4.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para seleccionar imagen de perfil
        Button(onClick = { profilePhotoPicker.launch("image/*") }) {
            Text("Seleccionar foto de perfil")
        }

        // Previsualización de imagen de perfil
        profilePhotoUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier.size(80.dp).padding(top = 4.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de registro
        Button(onClick = {
            // Convertir Strings a RequestBody para la API
            val dniBody = dni.toRequestBody("text/plain".toMediaTypeOrNull())
            val firstNameBody = firstName.toRequestBody("text/plain".toMediaTypeOrNull())
            val lastNameBody = lastName.toRequestBody("text/plain".toMediaTypeOrNull())
            val passwordBody = password.toRequestBody("text/plain".toMediaTypeOrNull())
            val phoneBody = phone.toRequestBody("text/plain".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
            val addressBody = address.toRequestBody("text/plain".toMediaTypeOrNull())
            val repStatusBody = reputationStatusId.toRequestBody("text/plain".toMediaTypeOrNull())
            val dniExtBody = dniExtension.takeIf { it.isNotBlank() }
                ?.toRequestBody("text/plain".toMediaTypeOrNull())

            // Convertir imágenes (Uri) a MultipartBody.Part
            val dniPart = dniPhotoUri?.let { uriToPart("dni_photo", it, contentResolver) }
            val profilePart = profilePhotoUri?.let { uriToPart("profile_photo", it, contentResolver) }

            // Enviar registro al ViewModel
            viewModel.registerUser(
                dniBody,
                firstNameBody,
                lastNameBody,
                dniExtBody,
                passwordBody,
                phoneBody,
                emailBody,
                addressBody,
                repStatusBody,
                dniPart,
                profilePart
            )
        }) {
            Text("Registrarse")
        }

        // Círculo de carga si se está procesando el registro
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
        }
    }
}