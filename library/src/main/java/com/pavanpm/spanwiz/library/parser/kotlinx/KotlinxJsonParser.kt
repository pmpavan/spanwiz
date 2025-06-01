package com.pavanpm.spanwiz.library.parser.kotlinx // Updated package

import android.util.Log
import com.pavanpm.spanwiz.library.JsonParser // Added import
import com.pavanpm.spanwiz.library.ParseResult // Added import
import com.pavanpm.spanwiz.library.models.TextWithSpans
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * A [JsonParser] implementation that uses the Kotlinx Serialization library for JSON processing.
 * @param json The Kotlinx Json instance to use for parsing. Defaults to a Json instance
 *             that ignores unknown keys (`Json { ignoreUnknownKeys = true }`).
 *             Data classes ([TextWithSpans], [com.pavanpm.spanwiz.library.models.SpanStyle])
 *             must be annotated with `@kotlinx.serialization.Serializable`.
 */
class KotlinxJsonParser(
    private val json: Json = Json { ignoreUnknownKeys = true }
) : JsonParser {

    override fun parse(jsonString: String): ParseResult {
        return try {
            val textWithSpans = json.decodeFromString<TextWithSpans>(jsonString)
            ParseResult.Success(textWithSpans)
        } catch (e: SerializationException) {
            Log.e("KotlinxJsonParser", "Failed to parse JSON string with Kotlinx Serialization: $jsonString", e)
            ParseResult.Error(e, "Kotlinx Serialization failed: ${e.message}")
        } catch (e: Exception) {
            Log.e("KotlinxJsonParser", "An unexpected error occurred during Kotlinx Serialization parsing: $jsonString", e)
            ParseResult.Error(e, "Kotlinx Serialization failed with an unexpected error: ${e.message}")
        }
    }
}
