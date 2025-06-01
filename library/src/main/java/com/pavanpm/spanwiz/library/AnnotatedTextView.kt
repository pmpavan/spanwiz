package com.pavanpm.spanwiz.library

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.AnnotatedString.Range // Keep this if onClick type uses it
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import com.pavanpm.spanwiz.library.SpanWiz.Companion.URL_TAG
import com.pavanpm.spanwiz.library.models.TextWithSpans


@Composable
fun AnnotatedTextView(
    modifier: Modifier = Modifier,
    spanWiz: SpanWiz,
    jsonString: String? = null,
    textWithSpans: TextWithSpans? = null,
    text: AnnotatedString? = null,
    style: TextStyle = TextStyle.Default,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    annotatedTags: List<String> = listOf(),
    onClick: (Int, String?, Range<String>?) -> Unit = { _, _, _ -> }
) {
    val currentOnClick by rememberUpdatedState(onClick)

    val annotatedString = remember(text, textWithSpans, jsonString, spanWiz) {
        buildAnnotatedString {
            text?.let { append(it) }
            textWithSpans?.let { tws ->
                append(
                    spanWiz.createFromTextWithSpans(tws)
                )
            }
            jsonString?.let { js ->
                spanWiz.createFromJson(js)?.let {
                    append(it)
                } ?: append(AnnotatedString("")) // Append empty AnnotatedString on error/null
            }
        }
    }

    val uriHandler: UriHandler = LocalUriHandler.current

    ClickableText(
        text = annotatedString,
        modifier = modifier,
        style = style,
        softWrap = softWrap,
        overflow = overflow,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
        onClick = { offset ->
            val urlTag =
                annotatedString.getStringAnnotations(tag = URL_TAG, start = offset, end = offset)
                    .firstOrNull()
            if (urlTag != null) {
                uriHandler.openUri(urlTag.item)
                currentOnClick.invoke(offset, URL_TAG, urlTag) // Use currentOnClick
            } else if (annotatedTags.isNotEmpty()) {
                annotatedTags.forEach { tag ->
                    annotatedString.getStringAnnotations(tag = tag, start = offset, end = offset)
                        .firstOrNull()?.let { annotation ->
                            currentOnClick.invoke(offset, tag, annotation) // Use currentOnClick
                        } ?: currentOnClick.invoke(offset, null, null) // Use currentOnClick
                }
            } else {
                currentOnClick.invoke(offset, null, null) // Use currentOnClick
            }
        }
    )
}
