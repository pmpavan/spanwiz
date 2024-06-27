package com.pavanpm.spanwiz

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pavanpm.spanwiz.library.AnnotatedTextView
import com.pavanpm.spanwiz.library.SpanWiz
import com.pavanpm.spanwiz.ui.theme.SpanWizTheme
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

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
private fun HomePage() {
    val spanWiz = SpanWiz(Moshi.Builder().add(KotlinJsonAdapterFactory()).build())
    val clickableJson =
        "{\"text\":\"Hello Android! I'm URL. (Click Me)\",\"spans\":[{\"style\":\"LINK\",\"start\":19,\"end\":22,\"link\":\"https://www.google.com\"}, {\"style\":\"LINK\",\"start\":25,\"end\":33,\"spanTag\":\"Click Me\",\"link\":\"https://www.outlook.com\"}]}"

    val listOfJsons = listOf(
        "{\"text\":\"Hello Android! I'm Bold and I'm Italic\",\"spans\":[{\"style\":\"BOLD\",\"start\":19,\"end\":23}, {\"style\":\"ITALIC\",\"start\":31,\"end\":38}]}",
        "{\"text\":\"Hello Android! I'm UNDERLINE and I'm STRIKETHROUGH\",\"spans\":[{\"style\":\"UNDERLINE\",\"start\":19,\"end\":28}, {\"style\":\"STRIKETHROUGH\",\"start\":37,\"end\":50}]}",
        "{\"text\":\"Hello Android! I'm logSUPERSCRIPT and I'm eSUBSCRIPT\",\"spans\":[{\"style\":\"SUPERSCRIPT\",\"start\":22,\"end\":33,\"color\":\"#581845\"}, {\"style\":\"SUBSCRIPT\",\"start\":43,\"end\":52,\"color\":\"#FFC300\"}]}",
        "{\"text\":\"Hello Android! I'm COLORED and I'm BACKGROUND_COLORED\",\"spans\":[{\"style\":\"COLOR\",\"start\":19,\"end\":26,\"color\":\"#FFC300\"}, {\"style\":\"BACKGROUND_COLOR\",\"start\":35,\"end\":53,\"backgroundColor\":\"#FFC300\"}]}",
        "{\"text\":\"I'm FONT_SIZE_24 and I'm FONT_WEIGHT_25\",\"spans\":[{\"style\":\"FONT_SIZE\",\"start\":3,\"end\":16,\"fontSize\":40}]}",
        "{\"text\":\"I'm Letter Spacing and have SHADOW\",\"spans\":[{\"style\":\"SHADOW\",\"start\":3,\"end\":35,\"shadow\":\"#FF5733\",\"radius\": 10.0}, {\"style\":\"FONT_SIZE\",\"start\":26,\"end\":35,\"fontSize\":40}]}",
        "{\"text\":\"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.\",\"spans\":[{\"style\":\"BOLD\",\"start\":6,\"end\":13},{\"style\":\"ITALIC\",\"start\":9,\"end\":13},{\"style\":\"UNDERLINE\",\"start\":14,\"end\":18},{\"style\":\"STRIKETHROUGH\",\"start\":23,\"end\":26},{\"style\":\"SUPERSCRIPT\",\"start\":29,\"end\":32},{\"style\":\"SUBSCRIPT\",\"start\":35,\"end\":36},{\"style\":\"COLOR\",\"start\":39,\"end\":42,\"color\":\"#DAF7A6\"},{\"style\":\"BACKGROUND_COLOR\",\"start\":44,\"end\":48,\"backgroundColor\":\"#FFC300\"},{\"style\":\"FONT_SIZE\",\"start\":52,\"end\":59,\"fontSize\":25},{\"style\":\"LINK\",\"start\":63,\"end\":70,\"link\":\"https://www.google.com\"},{\"style\":\"CUSTOM\",\"start\":72,\"end\":74},{\"style\":\"LETTER_SPACING\",\"start\":87,\"end\":91,\"letterSpacing\":0.4},{\"style\":\"SHADOW\",\"start\":93,\"end\":151,\"shadow\":\"#FF5733\"}]}"
    )

    val clickableString = spanWiz.applyToAnnotatedString(clickableJson)!!
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(id = R.string.app_name))
                },
                navigationIcon = {
                    IconButton(onClick = { println("back pressed") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Bottom app bar",
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { println("Add") }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        List(innerPadding, clickableString, spanWiz, clickableJson, listOfJsons)
    }
}

@Composable
private fun List(
    innerPadding: PaddingValues,
    clickableString: AnnotatedString,
    spanWiz: SpanWiz,
    clickableJson: String,
    listOfJsons: List<String>
) {
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
                        .getStringAnnotations("URL", offset, offset)
                        .firstOrNull()?.let { stringAnnotation ->
                            println(stringAnnotation.item)
                            Toast.makeText(
                                context,
                                "URL:${stringAnnotation.item}",
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
                    println("tag: $tag, offset: $offset")
                    Toast.makeText(
                        context,
                        annotatedTag?.item,
                        Toast.LENGTH_SHORT
                    ).show()
                    if (annotatedTag?.tag != null && annotatedTag.tag != "URL") {
                        uriHandler.openUri(annotatedTag.item)
                    }
//                                    clickableString
//                                        .getStringAnnotations("Click Me", offset, offset)
//                                        .firstOrNull()?.let { stringAnnotation ->
//                                            println(stringAnnotation.item)
//                                            Toast.makeText(
//                                                context,
//                                                stringAnnotation.item,
//                                                Toast.LENGTH_SHORT
//                                            ).show()
////                                            uriHandler.openUri(stringAnnotation.item)
//                                        }
                }
            )
            HorizontalDivider()
            listOfJsons.mapIndexed { index, json ->
                println("json $index: ${json.mapIndexed { index, c -> index to c }}")
                Column {
                    Greeting(
                        spanWiz.applyToAnnotatedString(json)
                            ?: buildAnnotatedString { append("Hello") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: AnnotatedString?, modifier: Modifier = Modifier) {
    Text(
        text = name ?: buildAnnotatedString { append("Hello") },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpanWizTheme {
        HomePage()
    }
}