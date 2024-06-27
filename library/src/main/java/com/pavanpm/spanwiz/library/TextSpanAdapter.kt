package com.pavanpm.spanwiz.library

import com.pavanpm.spanwiz.library.models.TextWithSpans
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import org.json.JSONObject.NULL
import java.io.IOException

//class TextSpanAdapter : JsonAdapter<TextWithSpans>() {
//
//    /** The underlying deserialization logic is thread-safe and does not require synchronization. **/
//    @Throws(IOException::class)
//    override fun fromJson(reader: JsonReader): TextWithSpans? {
//        if (reader.peek() == NULL) {
//            return reader.nextNull()
//        }
//        val string = reader.nextString()
//        return string.parseIsoDate()
//    }
//
//    /*** The underlying serialization logic is thread-safe and does not require synchronization. **/
//    @Throws(IOException::class)
//    override fun toJson(writer: JsonWriter, value: TextWithSpans?) {
//        if (value == null) {
//            writer.nullValue()
//        } else {
//            val string = value.formatIsoDate()
//            writer.value(string)
//        }
//    }
//}