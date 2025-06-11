package com.pavanpm.spanwiz.library

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily // New import
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import com.pavanpm.spanwiz.library.models.TextWithSpans
// Moshi specific imports removed
import android.util.Log // Added for logging

// ParseResult is now defined in ParseResult.kt

/**
 * Main class for SpanWiz library. Responsible for creating [AnnotatedString] instances
 * from text and span definitions.
 *
 * @param jsonParser An implementation of [JsonParser] (e.g.,
 *                   [com.pavanpm.spanwiz.library.parser.moshi.MoshiJsonParser],
 *                   [com.pavanpm.spanwiz.library.parser.gson.GsonJsonParser], or
 *                   [com.pavanpm.spanwiz.library.parser.kotlinx.KotlinxJsonParser])
 *                   to handle the deserialization of JSON strings into
 *                   [com.pavanpm.spanwiz.library.models.TextWithSpans] objects.
 */
class SpanWiz(private val jsonParser: JsonParser) { // Changed constructor parameter

    private val colorCache = mutableMapOf<String, androidx.compose.ui.graphics.Color>()

    companion object {
        const val URL_TAG = "URL" // Existing constant
        // BOLD_STYLE and ITALIC_STYLE are removed as their logic is now inline
        private val SUPERSCRIPT_SUBSCRIPT_FONT_SIZE = 16.sp
        private val DEFAULT_SHADOW_OFFSET = Offset(5.0f, 10.0f)
    }

    fun createFromJson(jsonString: String): AnnotatedString? {
        return when (val result = jsonParser.parse(jsonString)) { // Calls injected jsonParser.parse
            is ParseResult.Success -> createFromTextWithSpans(result.data)
            is ParseResult.Error -> {
                Log.w("SpanWiz", "Failed to create AnnotatedString from JSON. Parser error: ${result.message}", result.exception)
                null
            }
        }
    }

    // parseJson method removed

    fun createFromTextWithSpans(textWithSpans: TextWithSpans): AnnotatedString {
        return buildAnnotatedString {
            append(textWithSpans.text)
            textWithSpans.spans.forEach { span ->
                var styleColor: Color? = null
                var styleFontSize: TextUnit? = null
                var styleFontWeight: FontWeight? = null
                var styleFontStyle: FontStyle? = null
                var styleFontFamily: FontFamily? = null
                var styleTextDecoration: TextDecoration? = null
                var styleBaselineShift: BaselineShift? = null
                var styleLetterSpacing: TextUnit? = null
                var styleBackground: Color? = null
                var styleShadow: Shadow? = null

                when (span.style) {
                    TextSpanType.Bold -> styleFontWeight = FontWeight.Bold
                    TextSpanType.Italic -> styleFontStyle = FontStyle.Italic
                    TextSpanType.Underline -> styleTextDecoration = TextDecoration.Underline
                    TextSpanType.Strikethrough -> styleTextDecoration = TextDecoration.LineThrough
                    TextSpanType.Color -> span.color?.let { styleColor = getColor(it) }
                    TextSpanType.BackgroundColor -> span.backgroundColor?.let { styleBackground = getColor(it) }
                    TextSpanType.FontSize -> span.fontSize?.let { styleFontSize = TextUnit(it.toFloat(), TextUnitType.Sp) }
                    TextSpanType.Clickable -> {
                        span.link?.let {
                            addStringAnnotation(
                                tag = span.spanTag ?: URL_TAG,
                                annotation = span.link,
                                start = span.start,
                                end = span.end
                            )
                        }
                        styleColor = Color(-0x9b4a0a)
                        styleTextDecoration = TextDecoration.Underline
                    }
                    TextSpanType.Superscript -> {
                        styleBaselineShift = BaselineShift.Superscript
                        styleFontSize = span.fontSize?.let { it.toFloat().sp } ?: SUPERSCRIPT_SUBSCRIPT_FONT_SIZE
                        span.color?.let { styleColor = getColor(it) }
                    }
                    TextSpanType.Subscript -> {
                        styleBaselineShift = BaselineShift.Subscript
                        styleFontSize = span.fontSize?.let { it.toFloat().sp } ?: SUPERSCRIPT_SUBSCRIPT_FONT_SIZE
                        span.color?.let { styleColor = getColor(it) }
                    }
                    TextSpanType.LetterSpacing -> span.letterSpacing?.let { styleLetterSpacing = TextUnit(it, TextUnitType.Sp) }
                    TextSpanType.Shadow -> span.shadow?.let { shadowColorString ->
                        val customOffset = if (span.shadowOffsetX != null && span.shadowOffsetY != null) {
                            Offset(span.shadowOffsetX, span.shadowOffsetY)
                        } else {
                            DEFAULT_SHADOW_OFFSET
                        }
                        styleShadow = Shadow(
                            color = getColor(shadowColorString),
                            offset = customOffset,
                            blurRadius = span.radius ?: 0.0f
                        )
                    }
                    TextSpanType.Custom -> { /* No specific style from type itself */ }
                }

                // Apply fontFamily from span model
                span.fontFamily?.let { familyName ->
                    styleFontFamily = when (familyName.lowercase()) {
                        "sans-serif" -> FontFamily.SansSerif
                        "serif" -> FontFamily.Serif
                        "monospace" -> FontFamily.Monospace
                        "cursive" -> FontFamily.Cursive
                        else -> {
                            Log.w("SpanWiz", "Unsupported fontFamily: '$familyName'. No specific font family will be applied. Supported values are 'sans-serif', 'serif', 'monospace', 'cursive'.")
                            null // Explicitly null for clarity
                        }
                    }
                }

                // Apply fontWeight from span model (this will override fontWeight from TextSpanType.Bold if both are present)
                span.fontWeight?.let { weight ->
                    styleFontWeight = when (weight) {
                        100 -> FontWeight.W100
                        200 -> FontWeight.W200
                        300 -> FontWeight.W300
                        400 -> FontWeight.W400 // Normal
                        500 -> FontWeight.W500 // Medium
                        600 -> FontWeight.W600 // SemiBold
                        700 -> FontWeight.W700 // Bold
                        800 -> FontWeight.W800 // ExtraBold
                        900 -> FontWeight.W900 // Black
                        else -> {
                            Log.w("SpanWiz", "Unsupported fontWeight: '$weight'. The default or type-inferred font weight will be used. Supported values are 100-900 in increments of 100.")
                            styleFontWeight // Keep existing from TextSpanType.Bold or null, don't change it due to invalid input
                        }
                    }
                }

                val finalSpanStyle = SpanStyle(
                    color = styleColor ?: Color.Unspecified,
                    fontSize = styleFontSize ?: TextUnit.Unspecified,
                    fontWeight = styleFontWeight,
                    fontStyle = styleFontStyle,
                    fontFamily = styleFontFamily,
                    textDecoration = styleTextDecoration,
                    baselineShift = styleBaselineShift,
                    letterSpacing = styleLetterSpacing ?: TextUnit.Unspecified,
                    background = styleBackground ?: Color.Unspecified,
                    shadow = styleShadow
                )

                if (finalSpanStyle != SpanStyle()) { // Only add if not default/empty
                    addStyle(finalSpanStyle, span.start, span.end)
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
