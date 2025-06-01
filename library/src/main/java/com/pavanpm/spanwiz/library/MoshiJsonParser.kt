package com.pavanpm.spanwiz.library

import android.util.Log // For logging potential errors
import com.pavanpm.spanwiz.library.models.TextWithSpans
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

/**
 * A [JsonParser] implementation that uses the Moshi library for JSON processing.
 * @param moshi The Moshi instance to use for parsing. It's recommended that this instance
 *              is configured with `KotlinJsonAdapterFactory` for optimal Kotlin class support.
 */
class MoshiJsonParser(private val moshi: Moshi) : JsonParser {

    override fun parse(jsonString: String): ParseResult {
        // This logic is moved from the original SpanWiz.parseJson method
        val adapter: JsonAdapter<TextWithSpans> = moshi.adapter(TextWithSpans::class.java)
        return try {
            adapter.fromJson(jsonString)?.let {
                ParseResult.Success(it)
            } ?: ParseResult.Error(
                NullPointerException("Parsed JSON resulted in null object"), // More specific exception
                "Moshi adapter returned null for a non-null type TextWithSpans from JSON string: '$jsonString'"
            )
        } catch (e: Exception) {
            // Log the error for debugging purposes. The error is also propagated via ParseResult.Error.
            Log.e("MoshiJsonParser", "Failed to parse JSON string with Moshi: $jsonString", e)
            ParseResult.Error(e, "Moshi parsing failed: ${e.message}")
        }
    }
}
