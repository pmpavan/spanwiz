package com.pavanpm.spanwiz.library

// ParseResult is now a public top-level class in ParseResult.kt in the same package.
// com.pavanpm.spanwiz.library.models.TextWithSpans will be resolved by ParseResult.Success.

/**
 * Interface for parsing a JSON string into [com.pavanpm.spanwiz.library.models.TextWithSpans].
 */
interface JsonParser {
    /**
     * Parses the given JSON string.
     * @param jsonString The JSON string to parse.
     * @return A [ParseResult] which is either [ParseResult.Success] holding [com.pavanpm.spanwiz.library.models.TextWithSpans]
     *         or [ParseResult.Error] holding an exception.
     */
    fun parse(jsonString: String): ParseResult
}
