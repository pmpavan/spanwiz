package com.pavanpm.spanwiz

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pavanpm.spanwiz.library.AnnotatedTextView
import com.pavanpm.spanwiz.library.SpanWiz
import com.pavanpm.spanwiz.library.SpanWiz.Companion.URL_TAG
import com.pavanpm.spanwiz.library.MoshiJsonParser // Added import
import com.pavanpm.spanwiz.ui.theme.SpanWizTheme
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory // Added import

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpanWizTheme {
                HomePage()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage() {
    Scaffold(
        topBar = {
            val activity = (LocalContext.current as? Activity)
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(id = R.string.app_name))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        activity?.finish()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        List(innerPadding = innerPadding)
    }
}

@Composable
private fun List(
    innerPadding: PaddingValues
) {
    // Updated SpanWiz Instantiation
    val moshiInstance = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val jsonParser = MoshiJsonParser(moshiInstance)
    val spanWiz = SpanWiz(jsonParser)

    val comprehensiveJsonString = """
   {
     "text": "Explore SpanWiz: Bold, Italic, Underline, Strikethrough. Clickable Link. Colored Text & BGColor. Custom FontSize. Super^script & Sub_script with custom sizes. Wide Letters. Shadow default & custom. Serif Font, Monospace Font, Cursive Font. Light Weight, ExtraBold Weight. Bold+Serif+Light.",
     "spans": [
       { "start": 19, "end": 23, "style": "Bold" },
       { "start": 25, "end": 31, "style": "Italic" },
       { "start": 33, "end": 42, "style": "Underline" },
       { "start": 44, "end": 57, "style": "Strikethrough" },
       { "start": 59, "end": 73, "style": "Clickable", "link": "https://developer.android.com" },
       { "start": 75, "end": 87, "style": "Color", "color": "#FF00FF" },
       { "start": 90, "end": 97, "style": "BackgroundColor", "backgroundColor": "#FFFF00" },
       { "start": 99, "end": 113, "style": "FontSize", "fontSize": 24 },
       { "start": 115, "end": 120, "style": "Superscript", "color": "#0000FF", "fontSize": 10 },
       { "start": 129, "end": 133, "style": "Subscript", "color": "#00AA00", "fontSize": 12 },
       { "start": 154, "end": 166, "style": "LetterSpacing", "letterSpacing": 0.2 },
       { "start": 168, "end": 182, "style": "Shadow", "shadow": "#888888", "radius": 2.0 },
       { "start": 185, "end": 192, "style": "Shadow", "shadow": "#555555", "radius": 3.0, "shadowOffsetX": 2.0, "shadowOffsetY": 4.0 },
       { "start": 194, "end": 204, "style": "Custom", "fontFamily": "serif" },
       { "start": 206, "end": 220, "style": "Custom", "fontFamily": "monospace" },
       { "start": 222, "end": 234, "style": "Custom", "fontFamily": "cursive" },
       { "start": 236, "end": 248, "style": "Custom", "fontWeight": 300 },
       { "start": 250, "end": 265, "style": "Custom", "fontWeight": 800 },
       { "start": 267, "end": 282, "style": "Bold", "fontFamily": "serif", "fontWeight": 300 }
     ]
   }
   """

    val clickableJson =
        "{\"text\":\"Hello Android! I'm URL. (Click Me)\",\"spans\":[{\"style\":\"Clickable\",\"start\":19,\"end\":22,\"link\":\"https://www.google.com\"}, {\"style\":\"Clickable\",\"start\":25,\"end\":33,\"spanTag\":\"Click Me\",\"link\":\"https://www.outlook.com\"}]}"

    val listOfJsons = listOf(
        "{\"text\":\"Once upon a time, there was a bold tortoise and an italic hare who decided to race each other. The hare, known for his speed, was confident he would win easily. However, the underlined tortoise had a different plan. While the hare took a nap, the tortoise kept going at a steady pace. Everyone was amazed when the tortoise crossed the finish line with a bright background color. The moral of the story: slow and steady wins the race with a larger font size and noticeable letter spacing. There was even a shadow.\",\"spans\":[{\"start\":30,\"end\":44,\"style\":\"Bold\"},{\"start\":49,\"end\":60,\"style\":\"Italic\"},{\"start\":78,\"end\":82,\"style\":\"Clickable\",\"link\":\"https://www.google.com\"},{\"start\":140,\"end\":159,\"style\":\"Underline\"},{\"start\":64,\"end\":74,\"style\":\"Strikethrough\"},{\"start\":171,\"end\":172,\"style\":\"Superscript\",\"color\":\"#FF5733\"},{\"start\":200,\"end\":209,\"style\":\"Subscript\",\"color\":\"#33FF57\"},{\"start\":242,\"end\":282,\"style\":\"BackgroundColor\",\"backgroundColor\":\"#FFDD00\"},{\"start\":309,\"end\":322,\"style\":\"FontSize\",\"fontSize\":20},{\"start\":471,\"end\":485,\"style\":\"LetterSpacing\",\"letterSpacing\":5},{\"start\":504,\"end\":511,\"style\":\"Shadow\",\"shadow\":\"#000000\",\"radius\":1.5}]}",
        "{\"text\":\"Hello Android! I'm Bold and I'm Italic\",\"spans\":[{\"style\":\"Bold\",\"start\":19,\"end\":23}, {\"style\":\"Italic\",\"start\":31,\"end\":38}]}",
        "{\"text\":\"Hello Android! I'm UNDERLINE and I'm STRIKETHROUGH\",\"spans\":[{\"style\":\"Underline\",\"start\":19,\"end\":28}, {\"style\":\"Strikethrough\",\"start\":37,\"end\":50}]}",
        "{\"text\":\"Hello Android! I'm logSUPERSCRIPT and I'm eSUBSCRIPT\",\"spans\":[{\"style\":\"Superscript\",\"start\":22,\"end\":33,\"color\":\"#581845\"}, {\"style\":\"Subscript\",\"start\":43,\"end\":52,\"color\":\"#FFC300\"}]}",
        "{\"text\":\"Hello Android! I'm COLORED and I'm BACKGROUND_COLORED\",\"spans\":[{\"style\":\"Color\",\"start\":19,\"end\":26,\"color\":\"#FFC300\"}, {\"style\":\"BackgroundColor\",\"start\":35,\"end\":53,\"backgroundColor\":\"#FFC300\"}]}",
        "{\"text\":\"I'm FONT_SIZE_40.\",\"spans\":[{\"style\":\"FontSize\",\"start\":3,\"end\":16,\"fontSize\":40}]}",
        "{\"text\":\"I'm Letter Spacing and have SHADOW\",\"spans\":[{\"style\":\"Shadow\",\"start\":27,\"end\":34,\"shadow\":\"#FF5733\",\"radius\": 10.0}, {\"style\":\"LetterSpacing\",\"start\":4,\"end\":17,\"letterSpacing\":5}]}",
    )

    val clickableString = spanWiz.createFromJson(clickableJson)!!
    val context = LocalContext.current
    val uriHandler: UriHandler = LocalUriHandler.current // Hoisted for reuse

    LazyColumn(
        modifier = Modifier
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text(
                text = "Comprehensive Showcase:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            AnnotatedTextView(
                spanWiz = spanWiz,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                jsonString = comprehensiveJsonString,
                onClick = { _, _, annotatedTag ->
                    Toast.makeText(
                        context,
                        annotatedTag?.item ?: "Clicked on text",
                        Toast.LENGTH_SHORT
                    ).show()
                    annotatedTag?.let {
                        if (it.tag == URL_TAG) {
                            uriHandler.openUri(it.item)
                        }
                    }
                }
            )
            HorizontalDivider()
        }

        item {
            // val uriHandler: UriHandler = LocalUriHandler.current // No longer needed here

            ClickableText(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = clickableString,

                onClick = { offset ->
                    clickableString
                        .getStringAnnotations(URL_TAG, offset, offset)
                        .firstOrNull()?.let { stringAnnotation ->
                            println(stringAnnotation.item)
                            Toast.makeText(
                                context,
                                "$URL_TAG:${stringAnnotation.item}",
                                Toast.LENGTH_SHORT
                            ).show()
                            uriHandler.openUri(stringAnnotation.item)
                        }
                    clickableString
                        .getStringAnnotations("Click Me", offset, offset)
                        .firstOrNull()?.let { stringAnnotation ->
                            println(stringAnnotation.item)
                            Toast.makeText(
                                context,
                                "Click Me:${stringAnnotation.item}",
                                Toast.LENGTH_SHORT
                            ).show()
                            uriHandler.openUri(stringAnnotation.item)
                        }
                }
            )
            HorizontalDivider()
            AnnotatedTextView( // This is the existing AnnotatedTextView for clickableJson
                spanWiz = spanWiz,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                annotatedTags = listOf("Click Me"), // Keep this for specific tags if needed
                jsonString = clickableJson, // Use jsonString instead of textWithSpans
                onClick = { _, tag, annotatedTag -> // Simplified onClick, can be expanded
                    val itemClicked = annotatedTag?.item ?: "Clicked on text"
                    val tagInfo = tag ?: "no tag"
                    Toast.makeText(
                        context,
                        "Item: $itemClicked, Tag: $tagInfo",
                        Toast.LENGTH_SHORT
                    ).show()
                    annotatedTag?.let {
                         if (it.tag == URL_TAG || tag == "Click Me") { // Example handling for specific tags
                            uriHandler.openUri(it.item)
                        }
                    }
                }
            )
            HorizontalDivider()
            Column {
                listOfJsons.map { json ->
                    AnnotatedTextView(
                        spanWiz = spanWiz,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        jsonString = json, // Use jsonString
                        onClick = { _, _, annotatedTag ->
                            Toast.makeText(
                                context,
                                annotatedTag?.item,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    SpanWizTheme {
        HomePage()
    }
}