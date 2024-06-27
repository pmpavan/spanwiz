package com.pavanpm.spanwiz.library

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.AnnotatedString.Builder
import androidx.compose.ui.text.AnnotatedString.Range
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
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
    val annotatedString =
        Builder()
            .apply {
                text?.let { append(it) }
                textWithSpans?.let {
                    append(
                        spanWiz.createFromTextWithSpans(it)
                    )
                }
                jsonString?.let {
                    append(
                        spanWiz.createFromJson(jsonString) ?: Builder().toAnnotatedString()
                    )
                }
            }.toAnnotatedString()

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
                onClick.invoke(offset, URL_TAG, urlTag)
            } else if (annotatedTags.isNotEmpty()) {
                annotatedTags.forEach { tag ->
                    annotatedString.getStringAnnotations(tag = tag, start = offset, end = offset)
                        .firstOrNull()?.let { annotation ->
                            onClick.invoke(offset, tag, annotation)
                        } ?: onClick.invoke(offset, null, null)
                }
            } else {
                onClick.invoke(offset, null, null)
            }
        }
    )
}
