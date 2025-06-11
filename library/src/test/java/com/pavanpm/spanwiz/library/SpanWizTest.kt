package com.pavanpm.spanwiz.library

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily // New import
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.pavanpm.spanwiz.library.models.TextWithSpans
import com.pavanpm.spanwiz.library.parser.moshi.MoshiJsonParser // Updated import
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test // Assuming JUnit 4

class SpanWizTest {

    private lateinit var moshi: Moshi
    private lateinit var spanWiz: SpanWiz

    @Before
    fun setUp() {
        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val moshiJsonParser = MoshiJsonParser(moshi) // Create the parser instance
        spanWiz = SpanWiz(moshiJsonParser)          // Pass parser to SpanWiz
    }

    // --- Removed SpanWiz.parseJson tests as the method is no longer public on SpanWiz ---

    // --- createFromJson Tests (New) ---
    @Test
    fun `createFromJson with valid JSON returns non-null AnnotatedString`() {
        val jsonString = """
        {
            "text": "Hello",
            "spans": []
        }
        """
        val annotatedString = spanWiz.createFromJson(jsonString)
        assertNotNull("AnnotatedString should not be null for valid JSON", annotatedString)
        assertEquals("Hello", annotatedString?.text)
    }

    @Test
    fun `createFromJson with invalid JSON returns null`() {
        val jsonString = "{ \"text\": \"Hello\", \"spans\": [ " // Invalid JSON
        val annotatedString = spanWiz.createFromJson(jsonString)
        assertNull("AnnotatedString should be null for invalid JSON", annotatedString)
    }

    // --- createFromTextWithSpans Tests (Existing tests remain largely unchanged) ---

    @Test
    fun `createFromTextWithSpans applies Bold style`() {
        val textWithSpans = TextWithSpans(
            text = "Hello World",
            spans = listOf(com.pavanpm.spanwiz.library.models.SpanStyle(0, 5, TextSpanType.Bold))
        )
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        assertEquals("Hello World", annotatedString.text)
        val styles = annotatedString.spanStyles
        assertTrue(styles.any { it.item.fontWeight == FontWeight.Bold && it.start == 0 && it.end == 5 })
    }

    @Test
    fun `createFromTextWithSpans applies Italic style`() {
        val textWithSpans = TextWithSpans(
            text = "Hello Italic",
            spans = listOf(com.pavanpm.spanwiz.library.models.SpanStyle(0, 5, TextSpanType.Italic))
        )
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        assertTrue(styles.any { it.item.fontStyle == FontStyle.Italic && it.start == 0 && it.end == 5 })
    }

    @Test
    fun `createFromTextWithSpans applies Underline style`() {
        val textWithSpans = TextWithSpans(
            text = "Underlined Text",
            spans = listOf(com.pavanpm.spanwiz.library.models.SpanStyle(0, 10, TextSpanType.Underline))
        )
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        assertTrue(styles.any { it.item.textDecoration == TextDecoration.Underline && it.start == 0 && it.end == 10 })
    }

    @Test
    fun `createFromTextWithSpans applies Strikethrough style`() {
        val textWithSpans = TextWithSpans(
            text = "Strikethrough Text",
            spans = listOf(com.pavanpm.spanwiz.library.models.SpanStyle(0, 13, TextSpanType.Strikethrough))
        )
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        assertTrue(styles.any { it.item.textDecoration == TextDecoration.LineThrough && it.start == 0 && it.end == 13 })
    }


    @Test
    fun `createFromTextWithSpans applies Color style and tests caching`() {
        val colorHex = "#FF0000"
        val textWithSpans = TextWithSpans(
            text = "Red Text",
            spans = listOf(
                com.pavanpm.spanwiz.library.models.SpanStyle(0, 3, TextSpanType.Color, color = colorHex),
                com.pavanpm.spanwiz.library.models.SpanStyle(4, 7, TextSpanType.Color, color = colorHex) // Same color
            )
        )
        val expectedColor = Color(android.graphics.Color.parseColor(colorHex))

        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles

        assertTrue(styles.any { it.item.color == expectedColor && it.start == 0 && it.end == 3 })
        assertTrue(styles.any { it.item.color == expectedColor && it.start == 4 && it.end == 7 })

        // To verify caching, we'd ideally check if parseColor was called only once.
        // This is hard without mocking android.graphics.Color.parseColor or inspecting SpanWiz internals.
        // For now, we trust the getOrPut logic. A more direct test would require refactoring getColor or using a mock.
        // We can at least verify the cache has the entry after the first call.
        val cacheField = spanWiz.javaClass.getDeclaredField("colorCache")
        cacheField.isAccessible = true
        val cacheMap = cacheField.get(spanWiz) as Map<*, *>
        assertTrue(cacheMap.containsKey(colorHex))
    }

    @Test
    fun `createFromTextWithSpans applies BackgroundColor style`() {
        val bgColorHex = "#FFFF00"
        val textWithSpans = TextWithSpans(
            text = "Yellow Background",
            spans = listOf(com.pavanpm.spanwiz.library.models.SpanStyle(0, 6, TextSpanType.BackgroundColor, backgroundColor = bgColorHex))
        )
        val expectedBgColor = Color(android.graphics.Color.parseColor(bgColorHex))
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        assertTrue(styles.any { it.item.background == expectedBgColor && it.start == 0 && it.end == 6 })
    }

    @Test
    fun `createFromTextWithSpans applies FontSize style`() {
        val textWithSpans = TextWithSpans(
            text = "Big Text",
            spans = listOf(com.pavanpm.spanwiz.library.models.SpanStyle(0, 3, TextSpanType.FontSize, fontSize = 24))
        )
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        assertTrue(styles.any { it.item.fontSize == 24.sp && it.start == 0 && it.end == 3 })
    }

    @Test
    fun `createFromTextWithSpans applies Clickable style`() {
        val linkUrl = "https://example.com"
        val textWithSpans = TextWithSpans(
            text = "Click Here",
            spans = listOf(com.pavanpm.spanwiz.library.models.SpanStyle(0, 5, TextSpanType.Clickable, link = linkUrl))
        )
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val stringAnnotations = annotatedString.getStringAnnotations(SpanWiz.URL_TAG, 0, 5)
        assertEquals(1, stringAnnotations.size)
        assertEquals(linkUrl, stringAnnotations[0].item)
        // Also check for default styling of links (underline, specific color)
        val styles = annotatedString.spanStyles
        assertTrue(styles.any {
            it.start == 0 && it.end == 5 &&
            it.item.textDecoration == TextDecoration.Underline &&
            it.item.color == Color(-0x9b4a0a) // Default link color from SpanWiz
        })
    }

    @Test
    fun `createFromTextWithSpans applies Superscript style with constant font size`() {
        val colorHex = "#00FF00"
        val textWithSpans = TextWithSpans(
            text = "Super^2",
            spans = listOf(com.pavanpm.spanwiz.library.models.SpanStyle(5, 7, TextSpanType.Superscript, color = colorHex))
        )
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        assertTrue(styles.any {
            it.item.baselineShift == BaselineShift.Superscript &&
            it.item.fontSize == 16.sp && // Check for constant
            it.item.color == Color(android.graphics.Color.parseColor(colorHex)) &&
            it.start == 5 && it.end == 7
        })
    }

    @Test
    fun `createFromTextWithSpans applies Subscript style with constant font size`() {
        val colorHex = "#0000FF"
        val textWithSpans = TextWithSpans(
            text = "Sub_2",
            spans = listOf(com.pavanpm.spanwiz.library.models.SpanStyle(3, 5, TextSpanType.Subscript, color = colorHex))
        )
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        assertTrue(styles.any {
            it.item.baselineShift == BaselineShift.Subscript &&
            it.item.fontSize == 16.sp && // Check for constant
            it.item.color == Color(android.graphics.Color.parseColor(colorHex)) &&
            it.start == 3 && it.end == 5
        })
    }

    @Test
    fun `createFromTextWithSpans applies Superscript style with custom font size`() {
        val colorHex = "#00FF00"
        val customFontSize = 12
        val textWithSpans = TextWithSpans(
            text = "Super^custom",
            spans = listOf(com.pavanpm.spanwiz.library.models.SpanStyle(
                start = 5,
                end = 12,
                style = TextSpanType.Superscript,
                color = colorHex,
                fontSize = customFontSize // Provide custom font size
            ))
        )
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        assertTrue(styles.any {
            it.item.baselineShift == BaselineShift.Superscript &&
            it.item.fontSize == customFontSize.sp && // Check for custom font size
            it.item.color == Color(android.graphics.Color.parseColor(colorHex)) &&
            it.start == 5 && it.end == 12
        })
    }

    @Test
    fun `createFromTextWithSpans applies Subscript style with custom font size`() {
        val colorHex = "#0000FF"
        val customFontSize = 10
        val textWithSpans = TextWithSpans(
            text = "Sub_custom",
            spans = listOf(com.pavanpm.spanwiz.library.models.SpanStyle(
                start = 3,
                end = 10,
                style = TextSpanType.Subscript,
                color = colorHex,
                fontSize = customFontSize // Provide custom font size
            ))
        )
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        assertTrue(styles.any {
            it.item.baselineShift == BaselineShift.Subscript &&
            it.item.fontSize == customFontSize.sp && // Check for custom font size
            it.item.color == Color(android.graphics.Color.parseColor(colorHex)) &&
            it.start == 3 && it.end == 10
        })
    }

    @Test
    fun `createFromTextWithSpans applies LetterSpacing style`() {
        val textWithSpans = TextWithSpans(
            text = "Wide Text",
            spans = listOf(com.pavanpm.spanwiz.library.models.SpanStyle(0, 4, TextSpanType.LetterSpacing, letterSpacing = 0.5f))
        )
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        assertTrue(styles.any { it.item.letterSpacing == 0.5f.sp && it.start == 0 && it.end == 4 })
    }

    @Test
    fun `createFromTextWithSpans applies Shadow style with default offset`() { // Renamed
        val shadowColorHex = "#888888"
        // Ensure no shadowOffsetX or shadowOffsetY are provided in this model
        val textWithSpans = TextWithSpans(
            text = "Shadow Text",
            spans = listOf(com.pavanpm.spanwiz.library.models.SpanStyle(
                start = 0, end = 6, style = TextSpanType.Shadow,
                shadow = shadowColorHex, radius = 2.0f
                // shadowOffsetX and shadowOffsetY are null by default
            ))
        )
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        // DEFAULT_SHADOW_OFFSET is already a private val in SpanWiz companion,
        // so we might need to redefine it here for assertion or assert against its known values.
        // For simplicity, let's use its known values (5.0f, 10.0f) or make it accessible for test.
        // Assuming SpanWiz.DEFAULT_SHADOW_OFFSET is accessible or its values are known:
        val expectedDefaultOffset = androidx.compose.ui.geometry.Offset(5.0f, 10.0f)
        assertTrue(styles.any {
            it.item.shadow?.color == Color(android.graphics.Color.parseColor(shadowColorHex)) &&
            it.item.shadow?.offset == expectedDefaultOffset &&
            it.item.shadow?.blurRadius == 2.0f &&
            it.start == 0 && it.end == 6
        })
    }

    @Test
    fun `createFromTextWithSpans applies Shadow style with custom offset`() {
        val shadowColorHex = "#ABCDEF"
        val customOffsetX = 7.0f
        val customOffsetY = 12.0f
        val textWithSpans = TextWithSpans(
            text = "Custom Shadow",
            spans = listOf(com.pavanpm.spanwiz.library.models.SpanStyle(
                start = 0,
                end = 13,
                style = TextSpanType.Shadow,
                shadow = shadowColorHex,
                radius = 3.0f,
                shadowOffsetX = customOffsetX, // Provide custom offset X
                shadowOffsetY = customOffsetY  // Provide custom offset Y
            ))
        )
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        val expectedCustomOffset = androidx.compose.ui.geometry.Offset(customOffsetX, customOffsetY)
        assertTrue(styles.any {
            it.item.shadow?.color == Color(android.graphics.Color.parseColor(shadowColorHex)) &&
            it.item.shadow?.offset == expectedCustomOffset && // Check for custom offset
            it.item.shadow?.blurRadius == 3.0f &&
            it.start == 0 && it.end == 13
        })
    }

    @Test
    fun `createFromTextWithSpans applies fontFamily Serif`() {
        val textWithSpans = TextWithSpans("Serif Text", listOf(
            com.pavanpm.spanwiz.library.models.SpanStyle(0, 5, TextSpanType.Custom, fontFamily = "serif")
        ))
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        assertTrue(styles.any { it.item.fontFamily == FontFamily.Serif && it.start == 0 && it.end == 5 })
    }

    @Test
    fun `createFromTextWithSpans applies fontFamily SansSerif`() {
        val textWithSpans = TextWithSpans("SansSerif Text", listOf(
            com.pavanpm.spanwiz.library.models.SpanStyle(0, 9, TextSpanType.Custom, fontFamily = "sans-serif")
        ))
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        assertTrue(styles.any { it.item.fontFamily == FontFamily.SansSerif && it.start == 0 && it.end == 9 })
    }

    @Test
    fun `createFromTextWithSpans applies fontWeight W300 (Light)`() {
        val textWithSpans = TextWithSpans("Light Text", listOf(
            com.pavanpm.spanwiz.library.models.SpanStyle(0, 5, TextSpanType.Custom, fontWeight = 300)
        ))
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        assertTrue(styles.any { it.item.fontWeight == FontWeight.W300 && it.start == 0 && it.end == 5 })
    }

    @Test
    fun `createFromTextWithSpans applies fontWeight W700 (Bold) explicitly`() {
        val textWithSpans = TextWithSpans("Explicit Bold", listOf(
            com.pavanpm.spanwiz.library.models.SpanStyle(0, 13, TextSpanType.Custom, fontWeight = 700)
        ))
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        assertTrue(styles.any { it.item.fontWeight == FontWeight.Bold && it.start == 0 && it.end == 13 })
    }

    @Test
    fun `fontWeight from span model overrides TextSpanType Bold`() {
        val textWithSpans = TextWithSpans("Not Really Bold", listOf(
            com.pavanpm.spanwiz.library.models.SpanStyle(0, 15, TextSpanType.Bold, fontWeight = 300) // Type is Bold, but fontWeight is Light
        ))
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        val styles = annotatedString.spanStyles
        // Check that W300 is applied, not the default Bold from TextSpanType.Bold
        assertTrue(styles.any { it.item.fontWeight == FontWeight.W300 && it.start == 0 && it.end == 15 })
        assertFalse(styles.any { it.item.fontWeight == FontWeight.Bold && styles.none { inner -> inner.item.fontWeight == FontWeight.W300 } && it.start == 0 && it.end == 15 })
    }

    @Test
    fun `createFromTextWithSpans with empty spans list`() {
        val textWithSpans = TextWithSpans("Hello", emptyList())
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        assertEquals("Hello", annotatedString.text)
        assertTrue(annotatedString.spanStyles.isEmpty())
    }

    @Test
    fun `createFromTextWithSpans with empty text`() {
        val textWithSpans = TextWithSpans("", listOf(com.pavanpm.spanwiz.library.models.SpanStyle(0, 0, TextSpanType.Bold)))
        val annotatedString = spanWiz.createFromTextWithSpans(textWithSpans)
        assertEquals("", annotatedString.text)
        // Styles on empty text might be tricky or ignored by AnnotatedString.
        // Depending on AnnotatedString behavior, spanStyles might be empty or contain the style for range 0,0
        // For now, just ensure no crash and text is empty.
    }
}
