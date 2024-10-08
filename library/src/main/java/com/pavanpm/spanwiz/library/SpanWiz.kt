package com.pavanpm.spanwiz.library

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import com.pavanpm.spanwiz.library.models.TextWithSpans
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi


class SpanWiz(private val moshi: Moshi) {

    companion object {
        const val URL_TAG = "URL"
    }

    fun createFromJson(jsonString: String): AnnotatedString? {
        val json = parseJson(jsonString) ?: return null
        return createFromTextWithSpans(json)
    }

    fun parseJson(jsonString: String): TextWithSpans? {
        val adapter: JsonAdapter<TextWithSpans> = moshi.adapter(TextWithSpans::class.java)
        return try {
            adapter.fromJson(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun createFromTextWithSpans(textWithSpans: TextWithSpans): AnnotatedString {
        return buildAnnotatedString {
            append(textWithSpans.text)
            textWithSpans.spans.forEach { span ->
                val spanStyle = when (span.style) {
                    TextSpanType.Bold -> SpanStyle(fontWeight = FontWeight.Bold)
                    TextSpanType.Italic -> SpanStyle(fontStyle = FontStyle.Italic)
                    TextSpanType.Underline -> SpanStyle(textDecoration = TextDecoration.Underline)
                    TextSpanType.Strikethrough -> SpanStyle(textDecoration = TextDecoration.LineThrough)
                    TextSpanType.Color -> span.color?.let { SpanStyle(color = getColor(it)) }

                    TextSpanType.BackgroundColor -> span.backgroundColor?.let {
                        SpanStyle(background = getColor(it))
                    }

                    TextSpanType.FontSize -> span.fontSize?.let {
                        SpanStyle(
                            fontSize = TextUnit(
                                it.toFloat(),
                                TextUnitType.Sp
                            )
                        )
                    }

                    TextSpanType.Clickable -> {
                        span.link?.let {
                            addStringAnnotation(
                                tag = span.spanTag ?: URL_TAG,
                                annotation = span.link,
                                start = span.start,
                                end = span.end
                            )
                        }
                        SpanStyle(
                            color = Color(-0x9b4a0a),
                            textDecoration = TextDecoration.Underline
                        )
                    }

                    TextSpanType.Custom -> SpanStyle()
                    TextSpanType.Superscript -> span.color?.let {
                        SpanStyle(
                            baselineShift = BaselineShift.Superscript,
                            fontSize = 16.sp,
                            color = getColor(it)
                        )
                    }

                    TextSpanType.Subscript ->
                        span.color?.let {
                            SpanStyle(
                                baselineShift = BaselineShift.Subscript,
                                fontSize = 16.sp,
                                color = getColor(it)
                            )
                        }

                    TextSpanType.LetterSpacing -> span.letterSpacing?.let {
                        SpanStyle(
                            letterSpacing = TextUnit(
                                it,
                                TextUnitType.Sp
                            )
                        )
                    }

                    TextSpanType.Shadow -> span.shadow?.let {
                        SpanStyle(
                            shadow = Shadow(
                                color = getColor(it),
                                offset = Offset(5.0f, 10.0f),
                                blurRadius = span.radius ?: 0.0f
                            )
                        )
                    }
                }
                spanStyle?.let {
                    addStyle(it, span.start, span.end)
                }
            }
        }
    }

    private fun getColor(colorString: String): Color {
        return Color(android.graphics.Color.parseColor(buildString {
            append(if (!colorString.startsWith("#")) "#" else "")
            append(colorString)
        }))
    }

}
