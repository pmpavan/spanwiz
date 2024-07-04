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
import com.pavanpm.spanwiz.ui.theme.SpanWizTheme
import com.squareup.moshi.Moshi

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
    val spanWiz = SpanWiz(Moshi.Builder().build())
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
    LazyColumn(
        modifier = Modifier
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            val uriHandler: UriHandler = LocalUriHandler.current

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
            AnnotatedTextView(
                spanWiz = spanWiz,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                annotatedTags = listOf("Click Me"),
                textWithSpans = spanWiz.parseJson(clickableJson),
                onClick = { offset, tag, annotatedTag ->
                    Toast.makeText(
                        context,
                        annotatedTag?.item,
                        Toast.LENGTH_SHORT
                    ).show()
                    if (annotatedTag?.tag != null && annotatedTag.tag != URL_TAG) {
                        uriHandler.openUri(annotatedTag.item)
                    }
//                    clickableString
//                        .getStringAnnotations(tag, offset, offset)
//                        .firstOrNull()?.let { stringAnnotation ->
//                            println(stringAnnotation.item)
//                            Toast.makeText(
//                                context,
//                                stringAnnotation.item,
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            uriHandler.openUri(stringAnnotation.item)
//                        }
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
                        annotatedTags = listOf("Click Me"),
                        textWithSpans = spanWiz.parseJson(json),
                        onClick = { offset, tag, annotatedTag ->
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