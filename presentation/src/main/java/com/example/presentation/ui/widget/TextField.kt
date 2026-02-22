package com.example.presentation.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSLabelMediumWhite
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun TextFieldNormal(
    text: String = "",
    modifier: Modifier = Modifier.fillMaxWidth(),
    shape: Shape = RoundedCornerShape(4.dp),
    onValueChanged: (String) -> Unit = { },
    placeholder: String? = null,
    leadingIcon: Painter? = null,
    textFieldInfo: String? = null,
    isError: () -> Boolean = { false }
) {
    Column {
        TextField(
            value = text,
            textStyle = TSDisplayMediumWhite().copy(
                color = if (isError()) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.inversePrimary
            ),
            onValueChange = {
                onValueChanged(it)
            },
            placeholder = placeholder?.let {
                @Composable {
                    Text(
                        text = placeholder,
                        style = TSDisplayMediumWhite().copy(
                            fontSize = 15.sp,
                        )
                    )
                }
            },
            leadingIcon = leadingIcon?.let {
                @Composable {
                    Icon(
                        painter = it,
                        contentDescription = null
                    )
                }
            },
            modifier = modifier
                .padding(0.dp),
            shape = shape,
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                errorContainerColor = MaterialTheme.colorScheme.onErrorContainer,
                errorTextColor = MaterialTheme.colorScheme.onError
            ),
            isError = isError()
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
fun TextFieldSearchNoClick(
    text: String = "",
    modifier: Modifier = Modifier.fillMaxWidth(),
    shape: Shape = RoundedCornerShape(4.dp),
    onValueChanged: (String) -> Unit = { },
    textFieldInfo: String? = null,
    onClick: () -> Unit = {}
) {
    Box {
        Column {
            TextField(
                modifier = modifier
                    .padding(0.dp)
                    .clickable {
                        onClick()
                    },
                value = text,
                textStyle = TSLabelMediumWhite().copy(
                    color = MaterialTheme.colorScheme.inversePrimary
                ),
                onValueChange = {
                    onValueChanged(it)
                },
                placeholder = @Composable {
                    Text(
                        text = stringResource(R.string.label_what_do_you_want_to_play),
                        style = TSDisplayMediumWhite().copy(
                            color = MaterialTheme.colorScheme.background,
                            fontSize = 15.sp,
                        )
                    )
                },
                leadingIcon = @Composable {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        tint = MaterialTheme.colorScheme.background,
                        contentDescription = null
                    )
                },
                shape = shape,
                colors = TextFieldDefaults.colors().copy(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = MaterialTheme.colorScheme.inversePrimary,
                    focusedContainerColor = MaterialTheme.colorScheme.inversePrimary,
                    errorContainerColor = MaterialTheme.colorScheme.onErrorContainer,
                    errorTextColor = MaterialTheme.colorScheme.onError
                )
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

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable {
                    // Navigate to your destination here
                    onClick()
                }
        )
    }
}

@Composable
fun TextFieldSearch(
    text: String = "",
    modifier: Modifier = Modifier.fillMaxWidth(),
    shape: Shape = RoundedCornerShape(4.dp),
    onValueChanged: (String) -> Unit = { },
    textFieldInfo: String? = null,
    isError: () -> Boolean = { false },
    onBackClicked: () -> Unit = {}
) {
    Column {
        TextField(
            value = text,
            textStyle = TSDisplayMediumWhite().copy(
                color = if (isError()) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.inversePrimary
            ),
            onValueChange = {
                onValueChanged(it)
            },
            placeholder = @Composable {
                    Text(
                        text = "What do you want to listen to?",
                        style = TSDisplayMediumWhite().copy(
                            fontSize = 15.sp,
                        )
                    )
                },
            leadingIcon = @Composable {
                    Icon(
                        modifier = Modifier.clickable {
                            onBackClicked()
                        },
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = null
                    )
                }
            ,
            trailingIcon = if (text.isNotEmpty()) {
                @Composable {
                    Icon(
                        modifier = Modifier.clickable {
                            onValueChanged("")
                        },
                        contentDescription = null,
                        painter = painterResource(R.drawable.ic_close)
                    )
                }
            } else null,
            modifier = modifier
                .padding(0.dp),
            shape = shape,
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                errorContainerColor = MaterialTheme.colorScheme.onErrorContainer,
                errorTextColor = MaterialTheme.colorScheme.onError
            ),
            isError = isError()
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
        false
    }
) {
    var passwordHidden by remember { mutableStateOf(true) }

    Column {
        TextField(
            value = text,
            textStyle = TSDisplayMediumWhite()
                .copy(
                    color = if (isError()) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.inversePrimary
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

@Preview
@Composable
fun TextFieldPreviewNoClick() {
    PreviewTheme {
        Column(
            Modifier.padding(16.dp)
        ) {
            TextFieldNormal(
                text = "Test",
                onValueChanged = {

                }
            )

            Spacer(Modifier.padding(8.dp))

            TextFieldSearchNoClick(
                onValueChanged = {

                }
            )

            Spacer(Modifier.padding(8.dp))

            TextFieldSearch(
                text = "",
                onValueChanged = {

                }
            )

            Spacer(Modifier.padding(8.dp))

            TextFieldPassword(
                text = "Test",
                onValueChanged = {

                }
            )
        }
    }
}