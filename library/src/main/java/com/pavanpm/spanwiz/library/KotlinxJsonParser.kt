package com.pavanpm.spanwiz.library

import android.util.Log // For logging potential errors
import com.pavanpm.spanwiz.library.models.TextWithSpans
import kotlinx.serialization.SerializationException // Specific exception for Kotlinx Serialization
import kotlinx.serialization.json.Json

/**
 * A [JsonParser] implementation that uses the Kotlinx Serialization library for JSON processing.
 * @param json The Kotlinx Json instance to use for parsing. Defaults to a Json instance
 *             that ignores unknown keys.
 */
class KotlinxJsonParser(
    private val json: Json = Json { ignoreUnknownKeys = true }
) : JsonParser {

    override fun parse(jsonString: String): ParseResult {
        return try {
            // Kotlinx Serialization's decodeFromString handles nullability based on the target type.
            // Since TextWithSpans is a non-null type, a JSON string "null" or one that results
            // in a null object after parsing (if that were possible for a non-null type directly)
            // would typically throw a SerializationException.
            val textWithSpans = json.decodeFromString<TextWithSpans>(jsonString)
            ParseResult.Success(textWithSpans)
        } catch (e: SerializationException) {
            // Specific Kotlinx Serialization parsing error
            Log.e("KotlinxJsonParser", "Failed to parse JSON string with Kotlinx Serialization: $jsonString", e)
            ParseResult.Error(e, "Kotlinx Serialization failed: ${e.message}")
        } catch (e: Exception) {
            // Other potential errors (less common with decodeFromString for basic cases)
            Log.e("KotlinxJsonParser", "An unexpected error occurred during Kotlinx Serialization parsing: $jsonString", e)
            ParseResult.Error(e, "Kotlinx Serialization failed with an unexpected error: ${e.message}")
        }
    }
}
