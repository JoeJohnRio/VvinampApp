package com.example.presentation.ui.widget.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSLabelMediumWhite
import com.example.presentation.ui.widget.TextFieldNormal
import com.example.presentation.ui.widget.TextFieldPassword
import com.example.presentation.ui.widget.TextFieldSearch
import com.example.presentation.ui.widget.TextFieldSearchNoClick
import com.example.presentation.util.orErrorNoText
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun IconWithTextOnly(
    modifier: Modifier = Modifier,
    painter: Painter,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp, vertical = 8.dp
            )
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painter,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surfaceBright
        )

        Spacer(Modifier.width(8.dp))


        Text(
            modifier = Modifier.weight(1f),
            text = text,
            style = TSDisplayMediumWhite().copy(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.inversePrimary
            )
        )
    }
}


@Preview
@Composable
fun TextFieldPreviewNoClick() {
    PreviewTheme {
        Column(
            Modifier.padding(16.dp)
        ) {
            IconWithTextOnly(
                painter = painterResource(R.drawable.ic_share),
                text = "Share",
                onClick = {

                }
            )

            Spacer(Modifier.padding(8.dp))

            IconWithTextOnly(
                painter = painterResource(R.drawable.ic_share),
                text = "Share",
                onClick = {

                }
            )

            Spacer(Modifier.padding(8.dp))

            IconWithTextOnly(
                painter = painterResource(R.drawable.ic_share),
                text = "Share",
                onClick = {

                }
            )

            Spacer(Modifier.padding(8.dp))
        }
    }
}