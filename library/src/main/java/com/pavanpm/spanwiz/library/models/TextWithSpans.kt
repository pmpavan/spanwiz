package com.pavanpm.spanwiz.library.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
//@JsonSerializable
data class TextWithSpans(
    val text: String,
    val spans: List<SpanStyle>
)