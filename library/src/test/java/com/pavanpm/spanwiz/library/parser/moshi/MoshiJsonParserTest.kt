package com.pavanpm.spanwiz.library.parser.moshi // New package

import com.pavanpm.spanwiz.library.ParseResult
import com.pavanpm.spanwiz.library.TextSpanType
import com.pavanpm.spanwiz.library.models.TextWithSpans
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MoshiJsonParserTest {

    private lateinit var moshi: Moshi
    private lateinit var moshiJsonParser: MoshiJsonParser // Uses the class from the new package

    @Before
    fun setUp() {
        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        moshiJsonParser = MoshiJsonParser(moshi)
    }

    @Test
    fun `parse with valid JSON returns Success and correct data`() {
        val jsonString = """
        {
            "text": "Hello Moshi",
            "spans": [ { "start": 0, "end": 5, "style": "Bold" } ]
        }
        """
        val result = moshiJsonParser.parse(jsonString)
        assertTrue("Parsing should be successful. Result: $result", result is ParseResult.Success)
        val data = (result as ParseResult.Success).data
        assertEquals("Hello Moshi", data.text)
        assertEquals(1, data.spans.size)
        assertEquals(TextSpanType.Bold, data.spans[0].style)
    }

    @Test
    fun `parse with invalid JSON (malformed) returns Error with JsonDataException`() {
        val malformedJson = """{ "text": "Moshi Test", "spans": [ { "style": "Italic" "start": 0, "end": 4 } ] }""" // Missing comma
        val result = moshiJsonParser.parse(malformedJson)
        assertTrue("Parsing invalid JSON should result in Error. Result: $result", result is ParseResult.Error)
        val error = result as ParseResult.Error
        assertTrue("Exception should be JsonDataException. Got: ${error.exception::class.java}", error.exception is JsonDataException)
    }

    @Test
    fun `parse with 'null' string input returns Error`() {
        val jsonString = "null"
        val result = moshiJsonParser.parse(jsonString)
        assertTrue("Parsing 'null' string should result in Error. Result: $result", result is ParseResult.Error)
        val exception = (result as ParseResult.Error).exception
        assertTrue("Exception should be NullPointerException. Got: ${exception::class.java}", exception is NullPointerException)
    }

    @Test
    fun `parse with JSON where 'text' field is missing (non-nullable) returns Error`() {
        val jsonString = """{"spans": []}"""
        val result = moshiJsonParser.parse(jsonString)
        assertTrue("Parsing JSON with missing non-nullable 'text' field should result in Error. Result: $result", result is ParseResult.Error)
        val error = result as ParseResult.Error
        assertTrue("Exception for missing 'text' should be JsonDataException. Got: ${error.exception::class.java}", error.exception is JsonDataException)
    }

    @Test
    fun `parse with JSON where 'spans' field is missing (non-nullable) returns Error`() {
        val jsonString = """{"text": "Hello"}"""
        val result = moshiJsonParser.parse(jsonString)
        assertTrue("Parsing JSON with missing non-nullable 'spans' field should result in Error. Result: $result", result is ParseResult.Error)
        val error = result as ParseResult.Error
        assertTrue("Exception for missing 'spans' should be JsonDataException. Got: ${error.exception::class.java}", error.exception is JsonDataException)
    }
}
