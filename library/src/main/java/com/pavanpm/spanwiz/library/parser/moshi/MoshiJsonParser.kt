package com.pavanpm.spanwiz.library.parser.moshi // Updated package

import android.util.Log
import com.pavanpm.spanwiz.library.JsonParser // Added import
import com.pavanpm.spanwiz.library.ParseResult // Added import
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
        val adapter: JsonAdapter<TextWithSpans> = moshi.adapter(TextWithSpans::class.java)
        return try {
            adapter.fromJson(jsonString)?.let {
                ParseResult.Success(it)
            } ?: ParseResult.Error(
                NullPointerException("Parsed JSON resulted in null object"),
                "Moshi adapter returned null for a non-null type TextWithSpans from JSON string: '$jsonString'"
            )
        } catch (e: Exception) {
            Log.e("MoshiJsonParser", "Failed to parse JSON string with Moshi: $jsonString", e)
            ParseResult.Error(e, "Moshi parsing failed: ${e.message}")
        }
    }
}
