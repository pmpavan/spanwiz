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
    /**
     * Optional font family name (e.g., "serif", "sans-serif", "monospace", "cursive").
     * Applied to any span type.
     */
    val fontFamily: String? = null,
    /**
     * Font size in sp.
     * For [TextSpanType.FontSize], this directly sets the font size.
     * For [TextSpanType.Superscript] and [TextSpanType.Subscript], this overrides the default size if provided.
     */
    val fontSize: Int? = null,
    val link: String? = null,
    val custom: String? = null,
    /**
     * Optional font weight. Supported values: 100 (Thin), 200 (ExtraLight), 300 (Light),
     * 400 (Normal), 500 (Medium), 600 (SemiBold), 700 (Bold), 800 (ExtraBold), 900 (Black).
     * Applied to any span type. If specified, this will override the default weight from [TextSpanType.Bold].
     */
    val fontWeight: Int? = null,
    val letterSpacing: Float? = null,
    val fontStyle: Int? = null,
    val shadow: String? = null, // This is for shadow color
    val radius: Float? = null,
    /** Horizontal offset for the shadow, in pixels. Used if [style] is [TextSpanType.Shadow]. Defaults to a predefined value if null. */
    val shadowOffsetX: Float? = null, // New property
    /** Vertical offset for the shadow, in pixels. Used if [style] is [TextSpanType.Shadow]. Defaults to a predefined value if null. */
    val shadowOffsetY: Float? = null, // New property
    val spanTag: String? = null
)

