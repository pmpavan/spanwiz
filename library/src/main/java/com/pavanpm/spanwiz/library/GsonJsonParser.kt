package com.pavanpm.spanwiz.library

import android.util.Log // For logging potential errors
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException // Specific exception for Gson
import com.pavanpm.spanwiz.library.models.TextWithSpans

/**
 * A [JsonParser] implementation that uses the Gson library for JSON processing.
 * @param gson The Gson instance to use for parsing.
 */
class GsonJsonParser(private val gson: Gson) : JsonParser {

    override fun parse(jsonString: String): ParseResult {
        return try {
            val textWithSpans = gson.fromJson(jsonString, TextWithSpans::class.java)
            if (textWithSpans != null) {
                // Gson can sometimes parse "null" into a null object if the type is nullable,
                // but TextWithSpans itself is non-null. A null result here likely means
                // the top-level JSON was "null" or there was an issue creating the object,
                // even if no direct exception was thrown.
                // The TextWithSpans data class has non-nullable `text` and `spans` properties.
                // A check for their nullity after parsing is redundant if the data class is correctly defined
                // and Gson respects Kotlin's non-nullability (which it should with default constructors or appropriate adapters).
                // However, if Gson *could* produce a TextWithSpans with null for these by bypassing Kotlin's guarantees
                // (e.g. using Java reflection without Kotlin awareness), this check would be a safeguard.
                // Assuming standard Gson behavior with Kotlin data classes, `textWithSpans.text` or `.spans` being null
                // after `textWithSpans` itself is non-null should not happen if TextWithSpans is correctly defined.
                // For robustness against misconfiguration or unusual Gson behavior, the check is kept.
                if (textWithSpans.text == null || textWithSpans.spans == null) {
                     Log.w("GsonJsonParser", "Gson parsed JSON but result has null fields for non-null properties: $jsonString")
                     ParseResult.Error(
                         IllegalStateException("Gson parsed JSON but critical fields (text/spans) are null."),
                         "Gson parsing resulted in an invalid TextWithSpans object (null fields)."
                     )
                } else {
                    ParseResult.Success(textWithSpans)
                }
            } else {
                // This case might occur if jsonString is "null" and Gson is configured to produce null for it.
                Log.w("GsonJsonParser", "Gson returned null for TextWithSpans from JSON string: '$jsonString'")
                ParseResult.Error(
                    NullPointerException("Gson returned null for a non-null type TextWithSpans."),
                    "Gson parsing returned null, possibly from 'null' JSON input."
                )
            }
        } catch (e: JsonSyntaxException) {
            // Specific Gson parsing error
            Log.e("GsonJsonParser", "Failed to parse JSON string with Gson due to syntax error: $jsonString", e)
            ParseResult.Error(e, "Gson parsing failed due to syntax error: ${e.message}")
        } catch (e: Exception) {
            // Other potential errors during parsing or object instantiation
            Log.e("GsonJsonParser", "An unexpected error occurred during Gson parsing: $jsonString", e)
            ParseResult.Error(e, "Gson parsing failed with an unexpected error: ${e.message}")
        }
    }
}
