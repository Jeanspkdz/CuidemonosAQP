package com.jean.cuidemonosaqp.modules.auth.ui.login

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jean.cuidemonosaqp.R
import com.jean.cuidemonosaqp.shared.components.PasswordTextField
import com.jean.cuidemonosaqp.shared.theme.CuidemonosAQPTheme

@Composable
fun LoginScreenHost(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {}
) {
    val emailOrDni by viewModel.emailOrDni.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    // Manejar los estados del login
    LaunchedEffect(loginState) {
        when {
            loginState.success -> {
                Toast.makeText(context, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
            }
            loginState.error != null -> {
                Toast.makeText(context, loginState.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    LoginScreen(
        emailOrDni = emailOrDni,
        onEmailOrDniChanged = viewModel::onEmailChanged,
        password = password,
        onPasswordChanged = viewModel::onPasswordChanged,
        onLoginButtonClick = viewModel::onLoginClicked,
        onRegisterClick = onNavigateToRegister,
        isLoading = loginState.isLoading,
        modifier = modifier
    )
}

@Composable
fun LoginScreen(
    emailOrDni: String,
    onEmailOrDniChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    onLoginButtonClick: () -> Unit,
    onRegisterClick: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxHeight()
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo App",
                modifier = Modifier.size(120.dp)
            )
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primaryContainer
                )
            )
            Text(
                text = stringResource(R.string.auth_subtitle),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.inverseSurface.copy(
                        alpha = 0.7f
                    )
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column {
                Text(
                    text = stringResource(R.string.auth_email_or_dni_label),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(3.dp))
                OutlinedTextField(
                    placeholder = {
                        Text(
                            text = stringResource(R.string.auth_email_or_dni_placeholder),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                alpha = 0.6f
                            )
                        )
                    },
                    singleLine = true,
                    value = emailOrDni,
                    onValueChange = { onEmailOrDniChanged(it) },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = stringResource(R.string.auth_password_label),
                    fontWeight = FontWeight.Medium,
                )
                PasswordTextField(
                    placeholder = {
                        Text(
                            text = stringResource(R.string.auth_password_placeholder),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                alpha = 0.6f
                            )
                        )
                    },
                    value = password,
                    onValueChange = { onPasswordChanged(it) },
                    //enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(15.dp))
                //Buttons
                Button(
                    onClick = { onLoginButtonClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    contentPadding = PaddingValues(vertical = 15.dp),
                    shape = RoundedCornerShape(10.dp),
                    enabled = !isLoading && emailOrDni.isNotBlank() && password.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    } else {
                        Text(text = stringResource(R.string.auth_button_login))
                    }
                }

                Spacer(Modifier.height(15.dp))

                OutlinedButton(
                    onClick = { onRegisterClick() },
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
                    contentPadding = PaddingValues(vertical = 15.dp),
                    shape = RoundedCornerShape(10.dp),
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.auth_button_register))
                }
            }
        }

        // Overlay de loading (opcional, si quieres un loading de pantalla completa)
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Puedes descomentar esto si quieres un overlay de loading completo
                // CircularProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    CuidemonosAQPTheme {
        LoginScreen(
            emailOrDni = "",
            onEmailOrDniChanged = {},
            password = "",
            onPasswordChanged = {},
            onLoginButtonClick = {},
            onRegisterClick = {},
            isLoading = false
        )
    }
}

@Preview
@Composable
fun LoginScreenLoadingPreview() {
    CuidemonosAQPTheme {
        LoginScreen(
            emailOrDni = "test@example.com",
            onEmailOrDniChanged = {},
            password = "password123",
            onPasswordChanged = {},
            onLoginButtonClick = {},
            onRegisterClick = {},
            isLoading = true
        )
    }
}