package com.pavanpm.spanwiz.library.models

import com.pavanpm.spanwiz.library.TextSpanType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
//@JsonSerializable
data class SpanStyle(
    val start: Int,
    val end: Int,
    val style: TextSpanType,
    val color: String? = null,
    val backgroundColor: String? = null,
    val fontFamily: String? = null,
    val fontSize: Int? = null,
    val link: String? = null,
    val custom: String? = null,
    val fontWeight: Int? = null,
    val letterSpacing: Float? = null,
    val fontStyle: Int? = null,
    val shadow: String? = null, // This is for shadow color
    val radius: Float? = null,
    val shadowOffsetX: Float? = null, // New property
    val shadowOffsetY: Float? = null, // New property
    val spanTag: String? = null
)

