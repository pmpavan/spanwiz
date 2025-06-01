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

## Choosing a JSON Parser

SpanWiz now features a flexible JSON parsing system. It uses a `JsonParser` interface, allowing you to choose which underlying JSON parsing library you want to use. We provide implementations for three popular libraries: Moshi, Gson, and Kotlinx Serialization.

**Important Note on Dependencies and App Size:** Currently, the main `spanwiz` library artifact (`com.github.pmpavan.spanwiz:library`) includes direct dependencies for Moshi, Gson, and Kotlinx Serialization. This means that when you add SpanWiz to your project, all three JSON processing libraries are included, which can contribute to increased app size.

### Future Modularization (Recommended for App Size Optimization)

To allow for true optional dependencies and ensure you only include the JSON parsing library you actually use, future development of SpanWiz should focus on splitting the library into a multi-module structure:

*   **`spanwiz-core`**: This module would contain the core SpanWiz logic (including `SpanWiz`, `AnnotatedTextView`), the `JsonParser` interface, and data models. It would have *no* direct dependencies on specific JSON libraries like Moshi, Gson, or Kotlinx Serialization.
*   **`spanwiz-parser-moshi`**: This module would depend on `spanwiz-core` and add the Moshi library. It would provide the `MoshiJsonParser` implementation.
*   **`spanwiz-parser-gson`**: This module would depend on `spanwiz-core` and add the Gson library. It would provide the `GsonJsonParser` implementation.
*   **`spanwiz-parser-kotlinx`**: This module would depend on `spanwiz-core` and add the Kotlinx Serialization library. It would provide the `KotlinxJsonParser` implementation.

With such a setup, your `build.gradle` dependencies would look like this, giving you fine-grained control:
```gradle
dependencies {
  implementation 'com.github.pavanpm.spanwiz:spanwiz-core:VERSION'
  // Choose only ONE of the following parser implementations:
  implementation 'com.github.pavanpm.spanwiz:spanwiz-parser-moshi:VERSION'
  // implementation 'com.github.pavanpm.spanwiz:spanwiz-parser-gson:VERSION'
  // implementation 'com.github.pavanpm.spanwiz:spanwiz-parser-kotlinx:VERSION'
}
```
This modular approach is the recommended path forward for optimal app size and clean dependency management. Until this modularization is implemented, users highly concerned about including all three parsers may need to investigate advanced dependency exclusion mechanisms in their build configuration, though this can be complex and error-prone.

### Using Moshi (Default in previous versions)
To use SpanWiz with Moshi, create a `MoshiJsonParser` instance and pass it to `SpanWiz`:

```kotlin
import com.pavanpm.spanwiz.library.SpanWiz
import com.pavanpm.spanwiz.library.parser.moshi.MoshiJsonParser // Updated import
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

// ...
val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory()) // Recommended for Kotlin projects
    .build()
val moshiJsonParser = MoshiJsonParser(moshi)
val spanWizWithMoshi = SpanWiz(moshiJsonParser)

// Example usage:
// val annotatedString = spanWizWithMoshi.createFromJson(yourJsonString)
```

### Using Gson
To use SpanWiz with Gson, create a `GsonJsonParser` instance:

```kotlin
import com.pavanpm.spanwiz.library.SpanWiz
import com.pavanpm.spanwiz.library.parser.gson.GsonJsonParser // Updated import
import com.google.gson.Gson

// ...
val gson = Gson() // Or use new GsonBuilder().create() for custom configuration
val gsonJsonParser = GsonJsonParser(gson)
val spanWizWithGson = SpanWiz(gsonJsonParser)

// Example usage:
// val annotatedString = spanWizWithGson.createFromJson(yourJsonString)
```

### Using Kotlinx Serialization
To use SpanWiz with Kotlinx Serialization, create a `KotlinxJsonParser` instance:

```kotlin
import com.pavanpm.spanwiz.library.SpanWiz
import com.pavanpm.spanwiz.library.parser.kotlinx.KotlinxJsonParser // Updated import
import kotlinx.serialization.json.Json

// ...
// Uses the default Json configuration in KotlinxJsonParser (Json { ignoreUnknownKeys = true })
val kotlinxJsonParser = KotlinxJsonParser()
// Or, provide your own configured Json instance:
// val customKotlinxJson = Json { encodeDefaults = true; explicitNulls = false }
// val kotlinxJsonParserWithCustomConfig = KotlinxJsonParser(customKotlinxJson)
val spanWizWithKotlinx = SpanWiz(kotlinxJsonParser)

// Example usage:
// val annotatedString = spanWizWithKotlinx.createFromJson(yourJsonString)
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
To use SpanWiz, you first need an instance of a `JsonParser` implementation (e.g., `MoshiJsonParser`, `GsonJsonParser`, or `KotlinxJsonParser`). See the "Choosing a JSON Parser" section for details.

Here's an example using `MoshiJsonParser`:
```kotlin
import com.pavanpm.spanwiz.library.SpanWiz
import com.pavanpm.spanwiz.library.parser.moshi.MoshiJsonParser
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

// Configure your chosen JSON library instance
val moshiInstance = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Create the corresponding JsonParser
val jsonParser = MoshiJsonParser(moshiInstance)

// Create SpanWiz with the parser
val spanWiz = SpanWiz(jsonParser)
```

### Building using AnnotatedTextView Composable
The `AnnotatedTextView` composable is the recommended way to display your styled text. It handles parsing and rendering.
```kotlin
    // Assuming 'spanWiz' is initialized as shown above
    // and 'yourJsonString' is the JSON data from your backend.

    AnnotatedTextView(
        spanWiz = spanWiz,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        jsonString = yourJsonString, // Pass the JSON string directly
        // annotatedTags = listOf("YourCustomTag"), // Optional: for handling custom annotations
        onClick = { offset, tag, annotatedTag ->
            // Handle clicks on annotated parts of the text
            // For example, open a URL if a URL_TAG is present
            annotatedTag?.let {
                if (it.tag == SpanWiz.URL_TAG) {
                    // uriHandler.openUri(it.item) // Requires LocalUriHandler.current
                }
            }
            Toast.makeText(
                context, // Requires LocalContext.current
                "Clicked: ${annotatedTag?.item ?: "text"}",
                Toast.LENGTH_SHORT
            ).show()
        }
    )
```

### Building manually
If you need more control, you can parse the JSON and create the `AnnotatedString` manually:
```kotlin
    // Assuming 'spanWiz' is initialized and 'yourJsonString' is available
    val annotatedString: AnnotatedString? = spanWiz.createFromJson(yourJsonString)

    // If parsing was successful, 'annotatedString' can be used in a Text composable
    if (annotatedString != null) {
        Text(
            text = annotatedString,
            // ... other Text parameters
        )
    } else {
        // Handle JSON parsing error, e.g., display placeholder text
        Text("Error loading content.")
    }
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

