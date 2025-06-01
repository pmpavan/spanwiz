package com.pavanpm.spanwiz.library.parser.gson // Updated package

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.pavanpm.spanwiz.library.JsonParser // Added import
import com.pavanpm.spanwiz.library.ParseResult // Added import
import com.pavanpm.spanwiz.library.models.TextWithSpans

/**
 * A [JsonParser] implementation that uses the Gson library for JSON processing.
 * @param gson The Gson instance to use for parsing. Consider configuring it with `GsonBuilder`
 *             if specific settings (e.g., date formats, type adapters) are needed, though
 *             default Gson should work for the current [TextWithSpans] model.
 */
class GsonJsonParser(private val gson: Gson) : JsonParser {

    override fun parse(jsonString: String): ParseResult {
        return try {
            val textWithSpans = gson.fromJson(jsonString, TextWithSpans::class.java)
            if (textWithSpans != null) {
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
                Log.w("GsonJsonParser", "Gson returned null for TextWithSpans from JSON string: '$jsonString'")
                ParseResult.Error(
                    NullPointerException("Gson returned null for a non-null type TextWithSpans."),
                    "Gson parsing returned null, possibly from 'null' JSON input."
                )
            }
        } catch (e: JsonSyntaxException) {
            Log.e("GsonJsonParser", "Failed to parse JSON string with Gson due to syntax error: $jsonString", e)
            ParseResult.Error(e, "Gson parsing failed due to syntax error: ${e.message}")
        } catch (e: Exception) {
            Log.e("GsonJsonParser", "An unexpected error occurred during Gson parsing: $jsonString", e)
            ParseResult.Error(e, "Gson parsing failed with an unexpected error: ${e.message}")
        }
    }
}
