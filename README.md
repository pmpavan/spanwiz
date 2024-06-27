# SpanWiz / Text Span library for Compose

[![Release](https://jitpack.io/v/Username/RepoName.svg)](https://jitpack.io/#Username/RepoName)

SpanWiz is an Android Kotlin SDK for creating text spans based on JSON data sent from a backend using Moshi. It supports styles like bold, italic, and clickable links.

## Features

- Parse JSON to apply text styles.
- Support for bold, italic, and clickable links.
- Easily integrate with Jetpack Compose.

### Supported Text Span Types
- Bold
- Italic
- Clickable
- Underline
- Strikethrough
- Superscript
- Subscript
- Background color
- Font size
- Letter Spacing
- Shadow

## Installation
### JitPack

Add the JitPack repository to your root `build.gradle` at the end of repositories:

```groovy
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```

Add the dependency to your module build.gradle:

```groovy
dependencies {
    implementation 'com.github.pmpavan:SpanWiz:Tag'
}
```


## Usage
### Json Format
```json
{
  "text": "Once upon a time, there was a bold tortoise and an italic hare who decided to race each other. The hare, known for his speed, was confident he would win easily. However, the underlined tortoise had a different plan. While the hare took a nap, the tortoise kept going at a steady pace. Everyone was amazed when the tortoise crossed the finish line with a bright background color. The moral of the story: slow and steady wins the race with a larger font size and noticeable letter spacing. There was even a shadow.",
  "spans": [
    {
      "start": 30,
      "end": 44,
      "style": "Bold"
    },
    {
      "start": 49,
      "end": 60,
      "style": "Italic"
    },
    {
      "start": 78,
      "end": 82,
      "style": "Link",
      "link": "https://www.google.com"
    },
    {
      "start": 140,
      "end": 159,
      "style": "Underline"
    },
    {
      "start": 64,
      "end": 74,
      "style": "Strikethrough"
    },
    {
      "start": 171,
      "end": 172,
      "style": "Superscript",
      "color": "#FF5733"
    },
    {
      "start": 200,
      "end": 209,
      "style": "Subscript",
      "color": "#33FF57"
    },
    {
      "start": 243,
      "end": 283,
      "style": "BackgroundColor",
      "backgroundColor": "#FFDD00"
    },
    {
      "start": 309,
      "end": 322,
      "style": "FontSize",
      "fontSize": 20
    },
    {
      "start": 329,
      "end": 348,
      "style": "LetterSpacing",
      "letterSpacing": 0.1
    },
    {
      "start": 504,
      "end": 511,
      "style": "Shadow",
      "shadow": "#000000",
      "radius": 1.5
    }
  ]
}
```
### Building using AnnotatedTextView Composable
```kotlin
    AnnotatedTextView(
                spanWiz = spanWiz,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                annotatedTags = listOf("Click Me"),
                textWithSpans = spanWiz.parseJson(JsonString),
                onClick = { offset, tag, annotatedTag ->
                    Toast.makeText(
                        context,
                        annotatedTag?.item,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
```

### Create a SpanWiz instance
```kotlin
    val spanWiz = SpanWiz(moshiInstance)
    val json = spanWiz.applyToAnnotatedString(jsonString)
```

### Apply the JSON to a TextView
```kotlin
    textView.text = json
```


