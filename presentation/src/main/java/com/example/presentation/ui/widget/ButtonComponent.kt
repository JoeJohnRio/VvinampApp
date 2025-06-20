package com.example.presentation.ui.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R
import com.example.presentation.ui.theme.AppColours
import com.example.presentation.ui.theme.TSBodyMediumWhite
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun ButtonSolid(
    text: String = "",
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    disabledContainerColor: Color = MaterialTheme.colorScheme.outline,
    icon: Painter? = null,
    iconTint: Color = MaterialTheme.colorScheme.onPrimary,
    contentDescription: String? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = disabledContainerColor
        ),
        shape = RoundedCornerShape(25.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            icon?.let {
                Icon(
                    painter = it,
                    contentDescription = contentDescription,
                    tint = iconTint,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .align(Alignment.CenterStart)
                        .size(24.dp)
                )
            }

            Text(
                text = text,
                style = TSBodyMediumWhite().copy(color = textColor),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ButtonVariableSizeSolid(
    text: String = "",
    modifier: Modifier = Modifier,
    modifierText: Modifier = Modifier,
    onClick: () -> Unit = {},
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    disabledContainerColor: Color = MaterialTheme.colorScheme.outline,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = disabledContainerColor
        ),
        shape = RoundedCornerShape(25.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Box(
            modifier = modifierText,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = TSBodyMediumWhite().copy(color = textColor),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ButtonOutlined(
    text: String = "",
    modifier: Modifier = Modifier.fillMaxWidth(),
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    textColor: Color = MaterialTheme.colorScheme.inversePrimary,
    textStyle: TextStyle = TSBodyMediumWhite().copy(color = textColor),
    containerColor: Color = MaterialTheme.colorScheme.onPrimary,
    iconTint: Color = MaterialTheme.colorScheme.inversePrimary,
    icon: Painter? = null,
    contentDescription: String? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(50.dp),
        enabled = enabled,
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(1.dp, outlineColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor
        ),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            icon?.let {
                Icon(
                    painter = it,
                    contentDescription = contentDescription,
                    tint = iconTint,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .align(Alignment.CenterStart)
                        .size(24.dp)
                )
            }

            Text(
                text = text,
                style = textStyle,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Composable
fun ButtonVariableOutlined(
    text: String = "",
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    textColor: Color = MaterialTheme.colorScheme.inversePrimary,
    textStyle: TextStyle = TSBodyMediumWhite().copy(color = textColor),
    containerColor: Color = MaterialTheme.colorScheme.onPrimary,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    buttonHeight: Dp = 50.dp
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(buttonHeight),
        enabled = enabled,
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(1.dp, outlineColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor
        ),
        contentPadding = contentPadding
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = textStyle,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ButtonListWithState(
    modifier: Modifier = Modifier,
    listButton: List<String>,
    onClick: (String) -> Unit,
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    textColor: Color = MaterialTheme.colorScheme.inversePrimary,
    containerColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    var clickedItem by remember { mutableStateOf<String?>(null) }

    FlowRow(
        modifier = modifier,
    ) {
        listButton.forEach { data ->
            val isClicked = clickedItem == data
            ButtonVariableOutlined(
                modifier = Modifier
                    .padding(
                        end = 8.dp,
                        bottom = 12.dp
                    ),
                text = data,
                textColor = if (isClicked) textColor else textColor.copy(alpha = 0.4f),
                containerColor = if (isClicked) containerColor else containerColor.copy(alpha = 0.4f),
                outlineColor = outlineColor,
                onClick = {
                    clickedItem = data
                    onClick(data)
                }
            )
        }
    }
}

@Composable
fun RowWithRoundCheckbox(
    modifier: Modifier = Modifier
        .fillMaxWidth(),
    text: String,
    isChecked: Boolean = false,
    style: TextStyle = TSDisplayMediumWhite().copy(
        color = MaterialTheme.colorScheme.inversePrimary,
        lineHeight = 14.sp,
        fontSize = 9.5.sp,
    ),
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, // Aligns content vertically in the center,
        horizontalArrangement = Arrangement.SpaceBetween, // Aligns content horizontally in the center,
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.weight(0.9f),
            text = text,
            style = style,
        )

        CircleCheckbox(
            modifier = Modifier.weight(0.1f),
            selected = isChecked,
            onChecked = { onCheckedChange(!isChecked) }
        )
    }
}

@Composable
fun CircleCheckbox(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    onChecked: () -> Unit,
    selectedColorIcon: Color = MaterialTheme.colorScheme.onPrimary,
    selectedColorContainer: Color = MaterialTheme.colorScheme.primary,
    outlineColor: Color = MaterialTheme.colorScheme.outline,
) {
    val imageVector = if (selected) Icons.Filled.CheckCircle else Icons.Outlined.Circle
    val tint = if (selected) selectedColorContainer else outlineColor
    val background = if (selected) selectedColorIcon else Color.Transparent

    IconButton(
        modifier = modifier,
        onClick = { onChecked() },
        enabled = enabled
    ) {
        Icon(
            imageVector = imageVector, tint = tint,
            modifier = Modifier.background(background, shape = CircleShape),
            contentDescription = "checkbox"
        )
    }
}

@Preview(apiLevel = 33)
@Composable
private fun ButtonsPreview() {
    PreviewTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppColours.OnPrimaryBlack)
                .padding(16.dp)
        ) {
            ButtonSolid(
                text = "Sign up free",
                textColor = AppColours.OnPrimaryBlack,
                containerColor = AppColours.PrimaryGreen,
                icon = painterResource(R.drawable.ic_email),
                iconTint = AppColours.OnPrimaryBlack
            )

            ButtonOutlined(
                text = "Continue with phone number",
                modifier = Modifier.padding(top = 12.dp),
                outlineColor = AppColours.Outline,
                textColor = AppColours.OffWhite,
                containerColor = AppColours.OnPrimaryBlack,
                icon = painterResource(R.drawable.ic_smartphone),
                iconTint = AppColours.OffWhite,
                contentDescription = "Log in"
            )

            ButtonVariableSizeSolid(
                modifier = Modifier.padding(top = 12.dp),
                text = "Sign up free",
                textColor = AppColours.OnPrimaryBlack,
                containerColor = AppColours.PrimaryGreen,
            )

            ButtonVariableOutlined(
                text = "Continue with phone number",
                modifier = Modifier.padding(top = 12.dp),
                outlineColor = AppColours.Outline,
                textColor = AppColours.OffWhite,
                containerColor = AppColours.OnPrimaryBlack,
            )

            ButtonListWithState(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                listButton = listOf(
                    "Female",
                    "Male",
                    "Non-Binaryyyyyyy",
                    "Other",
                    "Prefer not to say"
                ),
                outlineColor = AppColours.Outline,
                textColor = AppColours.OffWhite,
                containerColor = AppColours.OnPrimaryBlack,
                onClick = {}
            )

            RowWithRoundCheckbox(
                text = stringResource(R.string.label_i_would_prefer_not),
                isChecked = false,
                onCheckedChange = {}
            )
        }
    }
}

