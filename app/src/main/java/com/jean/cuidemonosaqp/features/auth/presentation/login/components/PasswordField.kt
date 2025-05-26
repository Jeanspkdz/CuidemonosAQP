package com.jean.cuidemonosaqp.features.auth.presentation.login.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.jean.cuidemonosaqp.R

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: (@Composable () -> Unit)? = null,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val icon =
                if (isPasswordVisible) painterResource(R.drawable.visibility_24) else painterResource(
                    R.drawable.visibility_off
                )
            IconButton(onClick = {
                isPasswordVisible = !isPasswordVisible
            }) {
                Icon(
                    painter = icon,
                    contentDescription = stringResource(R.string.auth_password_icon_button)
                )
            }
        },
        placeholder = placeholder,
        singleLine = true,
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        modifier = modifier
    )
}