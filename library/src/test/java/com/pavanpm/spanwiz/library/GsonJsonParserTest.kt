package com.pavanpm.spanwiz.library

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.pavanpm.spanwiz.library.models.TextWithSpans
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GsonJsonParserTest {

    private lateinit var gson: Gson
    private lateinit var gsonJsonParser: GsonJsonParser

    @Before
    fun setUp() {
        gson = GsonBuilder().create()
        gsonJsonParser = GsonJsonParser(gson)
    }

    @Test
    fun `parse with valid JSON returns Success and correct data`() {
        val jsonString = """
        {
            "text": "Hello World",
            "spans": [
                {
                    "start": 0, "end": 5, "style": "Bold",
                    "color": "#FF0000", "backgroundColor": "#00FF00",
                    "fontFamily": "serif", "fontSize": 12, "fontWeight": 700,
                    "link": "http://example.com", "custom": "customData",
                    "letterSpacing": 0.1,
                    "shadow": "#0000FF", "radius": 1.0,
                    "shadowOffsetX": 2.0, "shadowOffsetY": 3.0,
                    "spanTag": "testTag"
                }
            ]
        }
        """
        val result = gsonJsonParser.parse(jsonString)
        assertTrue("Parsing should be successful. Result: $result", result is ParseResult.Success)
        val data = (result as ParseResult.Success).data
        assertEquals("Hello World", data.text)
        assertEquals(1, data.spans.size)
        val span = data.spans[0]
        assertEquals(0, span.start)
        assertEquals(5, span.end)
        assertEquals(TextSpanType.Bold, span.style)
        assertEquals("#FF0000", span.color)
        assertEquals("#00FF00", span.backgroundColor)
        assertEquals("serif", span.fontFamily)
        assertEquals(12, span.fontSize)
        assertEquals(700, span.fontWeight)
        assertEquals("http://example.com", span.link)
        assertEquals("customData", span.custom)
        assertEquals(0.1f, span.letterSpacing!!, 0.001f)
        assertEquals("#0000FF", span.shadow)
        assertEquals(1.0f, span.radius!!, 0.001f)
        assertEquals(2.0f, span.shadowOffsetX!!, 0.001f)
        assertEquals(3.0f, span.shadowOffsetY!!, 0.001f)
        assertEquals("testTag", span.spanTag)
    }

    @Test
    fun `parse with all TextSpanType enums returns Success`() {
        val text = TextSpanType.values().joinToString(separator = " ") { it.name }
        var currentPos = 0
        val spansJson = TextSpanType.values().map { enumVal ->
            val spanEntry = """{ "start": $currentPos, "end": ${currentPos + enumVal.name.length}, "style": "${enumVal.name}" }"""
            currentPos += enumVal.name.length + 1
            spanEntry
        }.joinToString(separator = ",")

        val jsonString = """
        {
            "text": "$text",
            "spans": [$spansJson]
        }
        """
        val result = gsonJsonParser.parse(jsonString)
        assertTrue("Parsing should be successful for all enum types. Result: $result", result is ParseResult.Success)
        val data = (result as ParseResult.Success).data
        assertEquals(TextSpanType.values().size, data.spans.size)
        TextSpanType.values().forEachIndexed { index, textSpanType ->
            assertEquals(textSpanType, data.spans[index].style)
        }
    }

    @Test
    fun `parse with invalid JSON returns Error with JsonSyntaxException`() {
        val malformedJson = """{ "text": "Test", "spans": [ { "style": "Bold" "start": 0, "end": 4 } ] }""" // Missing comma
        val result = gsonJsonParser.parse(malformedJson)
        assertTrue("Parsing invalid JSON should result in Error. Result: $result", result is ParseResult.Error)
        val error = result as ParseResult.Error
        assertTrue("Exception should be JsonSyntaxException. Got: ${error.exception::class.java}", error.exception is JsonSyntaxException)
    }

    @Test
    fun `parse with 'null' string input returns Error`() {
        val jsonString = "null"
        val result = gsonJsonParser.parse(jsonString)
        assertTrue("Parsing 'null' string should result in Error. Result: $result", result is ParseResult.Error)
        val exception = (result as ParseResult.Error).exception
        assertTrue("Exception should be NullPointerException or IllegalStateException from our checks. Got: ${exception::class.java}",
            exception is NullPointerException || exception is IllegalStateException)
    }

    @Test
    fun `parse with JSON where 'text' field is missing returns Error`() {
        val jsonString = """{"spans": []}"""
        val result = gsonJsonParser.parse(jsonString)
        assertTrue("Parsing JSON with missing 'text' field should result in Error. Result: $result", result is ParseResult.Error)
        val exception = (result as ParseResult.Error).exception
        assertTrue("Exception should be IllegalStateException from our checks. Got: ${exception::class.java}", exception is IllegalStateException)
    }

    @Test
    fun `parse with JSON where 'spans' field is missing returns Error`() {
        val jsonString = """{"text": "Hello"}"""
        val result = gsonJsonParser.parse(jsonString)
        assertTrue("Parsing JSON with missing 'spans' field should result in Error. Result: $result", result is ParseResult.Error)
        val exception = (result as ParseResult.Error).exception
        assertTrue("Exception should be IllegalStateException from our checks. Got: ${exception::class.java}", exception is IllegalStateException)
    }

    @Test
    fun `parse with empty spans array returns Success`() {
        val jsonString = """{"text": "Hello", "spans": []}"""
        val result = gsonJsonParser.parse(jsonString)
        assertTrue("Parsing should be successful. Result: $result", result is ParseResult.Success)
        val data = (result as ParseResult.Success).data
        assertEquals("Hello", data.text)
        assertTrue(data.spans.isEmpty())
    }

    @Test
    fun `parse with empty text string returns Success`() {
        val jsonString = """{"text": "", "spans": []}"""
        val result = gsonJsonParser.parse(jsonString)
        assertTrue("Parsing should be successful. Result: $result", result is ParseResult.Success)
        val data = (result as ParseResult.Success).data
        assertEquals("", data.text)
        assertTrue(data.spans.isEmpty())
    }
}
