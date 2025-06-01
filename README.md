<img src="https://github.com/pmpavan/spanwiz/blob/master/raw/main/logo.webp" alt="SpanWiz Icon" width="200" height="200">

# SpanWiz / Text Span library for Compose

![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)
[![Release](https://jitpack.io/v/pmpavan/spanwiz.svg)](https://jitpack.io/#pmpavan/spanwiz)

[APIs](https://pmpavan.github.io/spanwiz/)

SpanWiz is an Android Kotlin SDK for creating text spans based on JSON data sent from a backend using Moshi. It supports styles like bold, italic, and clickable links.

## Features

- Support text styles on the dynamic text.
- Support for bold, italic, and clickable links.
- Easily integrate with Jetpack Compose.

### Supported Text Span Types
- Bold: `{"style": "Bold"}`
- Italic: `{"style": "Italic"}`
- Clickable: `{"style": "Clickable", "link": "https://example.com"}`
- Underline: `{"style": "Underline"}`
- Strikethrough: `{"style": "Strikethrough"}`
- Superscript: `{"style": "Superscript", "color": "#FF5733", "fontSize": 12}` (fontSize is optional, defaults to 16sp)
- Subscript: `{"style": "Subscript", "color": "#33FF57", "fontSize": 12}` (fontSize is optional, defaults to 16sp)
- Background Color: `{"style": "BackgroundColor", "backgroundColor": "#FFDD00"}`
- Font Size: `{"style": "FontSize", "fontSize": 20}`
- Letter Spacing: `{"style": "LetterSpacing", "letterSpacing": 0.1}`
- Shadow: `{"style": "Shadow", "shadow": "#000000", "radius": 1.5, "shadowOffsetX": 3.0, "shadowOffsetY": 6.0}` (shadowOffsetX and shadowOffsetY are optional, default to 5.0f, 10.0f respectively)

### General Span Properties

The following properties can be added to *any* span object within the `spans` array to further customize its appearance:

- `"fontFamily": "<string>"`: Optional. Specifies the font family for the text within this span.
  Supported generic values include: `"sans-serif"`, `"serif"`, `"monospace"`, `"cursive"`.
  Example: `{"start": 0, "end": 5, "style": "Bold", "fontFamily": "serif"}`
- `"fontWeight": <number>`: Optional. Specifies the font weight for the text within this span.
  Supported integer values are 100 (Thin), 200 (ExtraLight), 300 (Light), 400 (Normal), 500 (Medium), 600 (SemiBold), 700 (Bold), 800 (ExtraBold), and 900 (Black).
  If provided for a span that also has `style: "Bold"`, this `fontWeight` will take precedence.
  Example: `{"start": 0, "end": 5, "style": "Custom", "fontWeight": 300}`

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
    implementation 'com.github.pmpavan:spanwiz:0.0.1'
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
      "style": "Bold",
      "fontWeight": 800,
      "fontFamily": "sans-serif"
    },
    {
      "start": 49,
      "end": 60,
      "style": "Italic",
      "fontFamily": "cursive"
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
      "color": "#FF5733",
      "fontSize": 10
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
      "radius": 1.5,
      "shadowOffsetX": 3.0,
      "shadowOffsetY": 6.0
    }
  ]
}
```

### Create a SpanWiz instance
```kotlin
    val spanWiz = SpanWiz(moshiInstance)
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

### Building manually

### Approach 1
```kotlin
    val annotatedString = spanWiz.createFromJson(jsonString)
```

### Approach 2
```kotlin
        val textWithSpans = parseJson(jsonString)
        val annotatedString = textWithSpans?.let { applyFromTextWithSpans(textWithSpans) }
```

### Apply the annotated string to the Text composable
```kotlin
    Text(
        text = annotatedString,
        style = TextStyle(
            color = Color.Black,
            fontSize = 16.sp
        )
    )
```

### Advanced Usage
TBD

## Roadmap
- [ ] Add support for custom text styles
- [ ] Extend support for SpannableString
- [ ] Add support for kotlin serilization
- [ ] Add support for Gson

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Authors
- Pavan P M - Initial work - [@pmpavan](https://github.com/pmpavan)

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

