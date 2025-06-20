package com.example.presentation.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.ui.theme.TSDisplayMediumWhite

@Composable
fun TextFieldNormal(
    text: String = "",
    modifier: Modifier = Modifier.fillMaxWidth(),
    shape: Shape = RoundedCornerShape(4.dp),
    onValueChanged: (String) -> Unit = { },
    textFieldInfo: String? = null
) {
    Column {
        TextField(
            value = text,
            textStyle = TSDisplayMediumWhite(),
            onValueChange = {
                onValueChanged(it)
            },
            modifier = modifier
                .padding(0.dp),
            shape = shape,
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            ),
        )

        textFieldInfo?.let {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = it,
                style = TSDisplayMediumWhite().copy(
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}

@Composable
fun TextFieldPassword(
    text: String = "",
    modifier: Modifier = Modifier.fillMaxWidth(),
    shape: Shape = RoundedCornerShape(4.dp),
    onValueChanged: (String) -> Unit = { },
    textFieldInfo: String? = null,
    isError: () -> Boolean = {
        text.length < 10
    }
) {
    var passwordHidden by remember { mutableStateOf(true) }

    Column {
        TextField(
            value = text,
            textStyle = TSDisplayMediumWhite()
                .copy(
                    color = if (isError()) Color.Red else MaterialTheme.colorScheme.inversePrimary
                ),
            onValueChange = {
                onValueChanged(it)
            },
            modifier = modifier
                .padding(0.dp),
            shape = shape,
            isError = isError(),
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                errorContainerColor = MaterialTheme.colorScheme.inversePrimary
            ), visualTransformation =
            if (passwordHidden) PasswordVisualTransformation()
            else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            trailingIcon = {
                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                    val visibilityIcon = if (passwordHidden)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff

                    // Please provide localized description for accessibility services
                    val description = if (passwordHidden)
                        "Show password"
                    else
                        "Hide password"

                    Icon(
                        imageVector = visibilityIcon,
                        contentDescription = description
                    )
                }
            }
        )

        textFieldInfo?.let {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = it,
                style = TSDisplayMediumWhite().copy(
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}