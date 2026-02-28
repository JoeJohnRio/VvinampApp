package com.example.presentation.ui.widget.text

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    maxLines: Int = 3
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isOverflowing by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(text) {
        isExpanded = false
        isOverflowing = false
    }

    Box(modifier = modifier
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            if (isOverflowing) {
                isExpanded = !isExpanded
            }
        }
    ) {
        val seeMoreText = "... See more"
        val seeLessText = " Show less"

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            style = textStyle,
            maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
            onTextLayout = { textLayoutResult ->
                if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                    isOverflowing = true
                    // FIX: Safely retrieve the index of the last visible line
                    val lastVisibleLineIndex = textLayoutResult.lineCount - 1
                    lastCharIndex = textLayoutResult.getLineEnd(lastVisibleLineIndex)
                }
            },
            text = buildAnnotatedString {
                if (isOverflowing && !isExpanded) {
                    // Truncate safely based on the actual rendered line limit
                    val adjustText = text.substring(startIndex = 0, endIndex = lastCharIndex)
                        .dropLast(seeMoreText.length + 5)
                        .dropLastWhile { it.isWhitespace() || it == '.' || it == ',' }

                    append(adjustText)
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(seeMoreText)
                    }
                } else {
                    append(text)
                    if (isExpanded && isOverflowing) {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(seeLessText)
                        }
                    }
                }
            }
        )
    }
}
