package com.jean.cuidemonosaqp.features.auth.presentation.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jean.cuidemonosaqp.R
import com.jean.cuidemonosaqp.features.auth.presentation.login.components.PasswordTextField
import com.jean.cuidemonosaqp.shared.presentation.theme.CuidemonosAQPTheme

@Composable
private fun LoginScreen(
    state: LoginState,
    onAction: (action: LoginAction) -> Unit,
    modifier: Modifier = Modifier
) {

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
                value = state.email_or_dni,
                onValueChange = { it -> onAction(LoginAction.OnEmailOrDniChanged(it)) },
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
                value = state.password,
                onValueChange = { onAction(LoginAction.OnPasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(15.dp))
            //Buttons
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                contentPadding = PaddingValues(vertical = 15.dp),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.auth_button_login))
            }

            Spacer(Modifier.height(15.dp))

            OutlinedButton(
                onClick = {},
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
                contentPadding = PaddingValues(vertical = 15.dp),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.auth_button_register))
            }


        }
    }
}


@Composable
fun LoginScreenHost(
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoginScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
@Preview(showBackground = true)
fun SignInScreenPreview() {
    CuidemonosAQPTheme(dynamicColor = false) {
        LoginScreen(state = LoginState(), onAction = {})
    }
}

