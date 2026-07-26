package com.example.vvinamp.ui.widget
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.painterResource
import com.example.vvinamp.util.preview.previewSafeString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.vvinamp.ui.theme.AppColours
import com.example.vvinamp.ui.theme.TSBodyMediumWhite
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.util.preview.PreviewTheme

@Composable
fun ButtonSolid(
    text: String = "",
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    disabledContainerColor: Color = MaterialTheme.colorScheme.outline,
    icon: Painter? = null,
    imageVector: ImageVector? = null,
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
            val iconModifier = Modifier
                .padding(start = 4.dp)
                .align(Alignment.CenterStart)
                .size(24.dp)

            if (imageVector != null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription,
                    tint = iconTint,
                    modifier = iconModifier
                )
            } else if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = contentDescription,
                    tint = iconTint,
                    modifier = iconModifier
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
fun ButtonSolidCloseIconAndText(
    text: String = "",
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    disabledContainerColor: Color = MaterialTheme.colorScheme.outline,
    icon: Painter? = null,
    imageVector: ImageVector? = null,
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconModifier = Modifier
                .padding(end = 4.dp)
                .size(24.dp)

            if (imageVector != null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription,
                    tint = iconTint,
                    modifier = iconModifier
                )
            } else if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = contentDescription,
                    tint = iconTint,
                    modifier = iconModifier
                )
            }

            Text(
                text = text,
                style = TSBodyMediumWhite().copy(color = textColor)
            )
        }
    }
}


@Composable
fun ButtonBlackSmallIcon(
    text: String = "",
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    disabledContainerColor: Color = MaterialTheme.colorScheme.outline,
    icon: Painter? = null,
    imageVector: ImageVector? = null,
    contentDescription: String? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            disabledContainerColor = disabledContainerColor
        ),
        shape = RoundedCornerShape(25.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        val iconModifier = Modifier.size(24.dp)

        if (imageVector != null) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.inversePrimary,
                modifier = iconModifier
            )
        } else if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.inversePrimary,
                modifier = iconModifier
            )
        }

        Spacer(Modifier.width(8.dp))

        Text(
            text = text,
            style = TSBodyMediumWhite().copy(
                color = MaterialTheme.colorScheme.inversePrimary
            )
        )
    }
}


@Composable
fun ButtonVariableSizeSolid(
    text: String = "",
    modifier: Modifier = Modifier
        .height(50.dp),
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
        modifier = modifier,
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
    imageVector: ImageVector? = null,
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
            val iconModifier = Modifier
                .padding(start = 4.dp)
                .align(Alignment.CenterStart)
                .size(24.dp)

            if (imageVector != null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription,
                    tint = iconTint,
                    modifier = iconModifier
                )
            } else if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = contentDescription,
                    tint = iconTint,
                    modifier = iconModifier
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
    shape: Shape = RoundedCornerShape(25.dp),
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    textStyle: TextStyle = TSBodyMediumWhite(),
    containerColor: Color = MaterialTheme.colorScheme.onPrimary,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    buttonHeight: Dp = 45.dp
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(buttonHeight),
        enabled = enabled,
        shape = shape,
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
    selectedButton: String? = null,
    buttonHorizontalPadding: Dp = 16.dp,
    buttonHeight: Dp = 45.dp,
    textStyle: TextStyle = TSBodyMediumWhite(),
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    outlineColorClicked: Color = outlineColor.copy(alpha = 0.4f),
    textColor: Color = MaterialTheme.colorScheme.inversePrimary,
    textColorClicked: Color = textColor.copy(alpha = 0.4f),
    containerColor: Color = MaterialTheme.colorScheme.onPrimary,
    containerColorClicked: Color = containerColor.copy(alpha = 0.4f)
) {
    var clickedItem by remember { mutableStateOf<String?>(null) }
    val currentSelected = selectedButton ?: clickedItem

    FlowRow(
        modifier = modifier,
    ) {
        listButton.forEach { data ->
            val isClicked = currentSelected == data
            ButtonVariableOutlined(
                modifier = Modifier
                    .padding(
                        end = 8.dp,
                        bottom = 12.dp
                    ),
                text = data,
                contentPadding = PaddingValues(horizontal = buttonHorizontalPadding),
                buttonHeight = buttonHeight,
                textStyle = textStyle.copy(color = if (isClicked) textColorClicked else textColor),
                containerColor = if (isClicked) containerColorClicked else containerColor,
                outlineColor = if (isClicked) outlineColorClicked else outlineColor,
                onClick = {
                    if (selectedButton == null) {
                        clickedItem = data
                    }
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
    val imageVector = if (selected) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle
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

@Preview
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
                imageVector = Icons.Default.Email,
                iconTint = AppColours.OnPrimaryBlack
            )
            Spacer(
                Modifier.height(12.dp)
            )
            Row() {
                ButtonBlackSmallIcon(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .height(30.dp),
                    text = "Add",
                    imageVector = Icons.Default.Add,
                )

                Spacer(Modifier.width(8.dp))

                ButtonBlackSmallIcon(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .height(30.dp),
                    text = "Add",
                    imageVector = Icons.Default.Add,
                )

                Spacer(Modifier.width(8.dp))

                ButtonBlackSmallIcon(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .height(30.dp),
                    text = "Add",
                    imageVector = Icons.Default.Add,
                )
            }
            Spacer(
                Modifier.height(12.dp)
            )
            ButtonVariableOutlined(
                text = "Follow",
                buttonHeight = 45.dp,
                contentPadding = PaddingValues(horizontal = 15.dp),
                shape = RoundedCornerShape(4.dp),
                textStyle = TSBodyMediumWhite().copy(
                    color = MaterialTheme.colorScheme.inversePrimary,
                    fontWeight = FontWeight.Bold
                ),
            )
            ButtonSolidCloseIconAndText(
                modifier = Modifier.padding(top = 12.dp),
                text = "Logout",
                textColor = AppColours.error,
                containerColor = AppColours.errorContainer,
                imageVector = Icons.AutoMirrored.Filled.Logout,
                iconTint = AppColours.error
            )
            ButtonOutlined(
                text = "Continue with phone number",
                modifier = Modifier.padding(top = 12.dp),
                outlineColor = AppColours.Outline,
                textColor = AppColours.OffWhite,
                containerColor = AppColours.OnPrimaryBlack,
                imageVector = Icons.Default.Smartphone,
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
                textStyle = TSDisplayMediumWhite().copy(
                    color = MaterialTheme.colorScheme.inversePrimary,
                ),
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
                text = previewSafeString(Res.string.label_i_would_prefer_not, "I would prefer not to receive marketing messages from Vvinamp."),
                isChecked = false,
                onCheckedChange = {}
            )
        }
    }
}
