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
import android.util.Log // Added for logging

// Define ParseResult sealed class
sealed class ParseResult {
    data class Success(val data: com.pavanpm.spanwiz.library.models.TextWithSpans) : ParseResult()
    data class Error(val exception: Exception, val message: String? = null) : ParseResult()
}

class SpanWiz(private val moshi: Moshi) {

    private val colorCache = mutableMapOf<String, androidx.compose.ui.graphics.Color>()

    companion object {
        const val URL_TAG = "URL" // Existing constant
        private val BOLD_STYLE = SpanStyle(fontWeight = FontWeight.Bold)
        private val ITALIC_STYLE = SpanStyle(fontStyle = FontStyle.Italic)
        private val SUPERSCRIPT_SUBSCRIPT_FONT_SIZE = 16.sp
        private val DEFAULT_SHADOW_OFFSET = Offset(5.0f, 10.0f)
    }

    fun createFromJson(jsonString: String): AnnotatedString? {
        return when (val result = parseJson(jsonString)) {
            is ParseResult.Success -> createFromTextWithSpans(result.data)
            is ParseResult.Error -> {
                Log.w("SpanWiz", "Failed to create AnnotatedString from JSON: ${result.message}", result.exception)
                null
            }
        }
    }

    fun parseJson(jsonString: String): ParseResult {
        val adapter: JsonAdapter<TextWithSpans> = moshi.adapter(TextWithSpans::class.java)
        return try {
            adapter.fromJson(jsonString)?.let {
                ParseResult.Success(it)
            } ?: ParseResult.Error(NullPointerException("Parsed JSON resulted in null object"), "Parsed JSON resulted in null object")
        } catch (e: Exception) {
            Log.e("SpanWiz", "JSON parsing error", e)
            ParseResult.Error(e, "Failed to parse JSON string")
        }
    }

    fun createFromTextWithSpans(textWithSpans: TextWithSpans): AnnotatedString {
        return buildAnnotatedString {
            append(textWithSpans.text)
            textWithSpans.spans.forEach { span ->
                val spanStyle = when (span.style) {
                    TextSpanType.Bold -> BOLD_STYLE
                    TextSpanType.Italic -> ITALIC_STYLE
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
                            fontSize = SUPERSCRIPT_SUBSCRIPT_FONT_SIZE, // Use constant
                            color = getColor(it)
                        )
                    }

                    TextSpanType.Subscript ->
                        span.color?.let {
                            SpanStyle(
                                baselineShift = BaselineShift.Subscript,
                                fontSize = SUPERSCRIPT_SUBSCRIPT_FONT_SIZE, // Use constant
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
                                offset = DEFAULT_SHADOW_OFFSET, // Use constant
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

    private fun getColor(colorString: String): androidx.compose.ui.graphics.Color {
        return colorCache.getOrPut(colorString) {
            androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(buildString {
                append(if (!colorString.startsWith("#")) "#" else "")
                append(colorString)
            }))
        }
    }

}
