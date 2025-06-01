package com.pavanpm.spanwiz.library

// Ensure necessary imports if TextWithSpans or Exception are from different packages,
// though for this structure, they are likely fine.
// For com.pavanpm.spanwiz.library.models.TextWithSpans, it's in a sub-package, so an import is needed.
import com.pavanpm.spanwiz.library.models.TextWithSpans

/**
 * Represents the result of a JSON parsing operation.
 * It can be either a [Success] containing the parsed data, or an [Error]
 * containing details about the parsing failure.
 */
public sealed class ParseResult {
    /**
     * Represents a successful parsing operation.
     * @property data The parsed data of type [TextWithSpans].
     */
    public data class Success(val data: TextWithSpans) : ParseResult()

    /**
     * Represents a failed parsing operation.
     * @property exception The exception that occurred during parsing.
     * @property message An optional descriptive message about the error.
     */
    public data class Error(val exception: Exception, val message: String? = null) : ParseResult()
}
