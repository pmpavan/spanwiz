package com.pavanpm.spanwiz.library.models

// import com.squareup.moshi.JsonClass // Keep if still needed for Moshi, or remove if only one annotation type is preferred
import kotlinx.serialization.Serializable // ADD THIS IMPORT
import com.squareup.moshi.JsonClass

@Serializable // ADD THIS ANNOTATION
@JsonClass(generateAdapter = true) // Moshi annotation
data class TextWithSpans(
    val text: String,
    val spans: List<SpanStyle>
)