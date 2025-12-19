# TextLinker - Auto Link Detection Library

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blue?logo=kotlin&logoColor=white)](https://kotlinlang.org)\
[![License: MIT](https://img.shields.io/badge/License-MIT-green)](LICENSE)\
[![API](https://img.shields.io/badge/API-21%2B-orange)](#)
[![](https://jitpack.io/v/Excelsior-Technologies-Community/TextLinkerKit.svg)](https://jitpack.io/#Excelsior-Technologies-Community/TextLinkerKit)

**TextLinker** is a powerful Android library that automatically detects and linkifies URLs, email addresses, and phone numbers in text. With extensive XML configuration support, you can customize colors, styles, and behaviors without writing a single line of Kotlin code.

---

## 📸 Preview

<img src="app/src/main/assets/Video.gif"
       alt="Text Linker Kit Library Demo" 
      height="320"/>

---

## ✨ Features

- **Automatic Detection**: Instantly detects URLs, emails, and phone numbers in text
- **Smart Link Handling**: URLs open in browser, emails in mail app, phones in dialer
- **Fully XML-Configurable**: 25+ attributes for complete customization without code
- **Individual Styling**: Separate colors, underlines, and click behavior per link type
- **Edge Case Handling**: Strips trailing punctuation, handles overlapping links
- **Custom Regex Support**: Override default patterns with your own regex
- **Text Formatting**: Make links bold, italic, or both
- **Press Highlighting**: Visual feedback when links are tapped
- **Long Press Support**: Optional long-press detection with custom duration
- **Runtime Customization**: Change any setting programmatically at any time
- **Production Ready**: Error handling, accessibility support, orientation-safe
- **Material Design**: Extends `AppCompatTextView` for full compatibility
- **Zero Dependencies**: Lightweight, uses only Android framework APIs

---

## 📦 Installation

**Step 1:** Add JitPack repository to your root `build.gradle`:

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

**Step 2:** Add dependency to your app module's `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.Excelsior-Technologies-Community:TextLinkerKit:1.0.0'
}
```

---

## 🚀 Usage

### Basic Usage - XML

```xml
<!-- Minimal Configuration - Detects Everything -->
<com.ext.text_linker.TextLinkerView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Visit https://google.com or email support@example.com"
    app:tl_detectUrls="true"
    app:tl_detectEmails="true"
    app:tl_detectPhones="true" />
```

### Custom Colors - XML

```xml
<!-- Different Colors for Each Link Type -->
<com.ext.text_linker.TextLinkerView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Check out openai.com, contact hello@openai.com, or call 1-800-555-0199"
    android:textSize="16sp"
    app:tl_detectUrls="true"
    app:tl_detectEmails="true"
    app:tl_detectPhones="true"
    app:tl_urlColor="#FF0000"
    app:tl_emailColor="#00AA00"
    app:tl_phoneColor="#FF8800" />
```

### Styled Links - XML

```xml
<!-- Bold, Italic, No Underlines -->
<com.ext.text_linker.TextLinkerView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Bold and italic: www.github.com, admin@site.com, 123-456-7890"
    app:tl_detectUrls="true"
    app:tl_detectEmails="true"
    app:tl_detectPhones="true"
    app:tl_linksBold="true"
    app:tl_linksItalic="true"
    app:tl_urlUnderline="false"
    app:tl_emailUnderline="false"
    app:tl_phoneUnderline="false" />
```

### Selective Detection - XML

```xml
<!-- Only URLs Clickable -->
<com.ext.text_linker.TextLinkerView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Only www.reddit.com is clickable, not test@email.com or 9876543210"
    app:tl_detectUrls="true"
    app:tl_detectEmails="false"
    app:tl_detectPhones="false"
    app:tl_urlColor="#0066CC" />

<!-- Only Emails Clickable -->
<com.ext.text_linker.TextLinkerView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Only contact@company.com works, not google.com or 555-1234"
    app:tl_detectUrls="false"
    app:tl_detectEmails="true"
    app:tl_detectPhones="false"
    app:tl_emailColor="#DD4814" />
```

### Advanced Configuration - XML

```xml
<!-- Press Highlight, Long Press, Custom Regex -->
<com.ext.text_linker.TextLinkerView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Advanced features example"
    app:tl_detectUrls="true"
    app:tl_urlColor="#6A1B9A"
    app:tl_enablePressHighlight="true"
    app:tl_pressHighlightColor="#E1BEE7"
    app:tl_enableLongPress="true"
    app:tl_longPressDuration="800"
    app:tl_stripTrailingPunctuation="true"
    app:tl_addAccessibilityHints="true" />
```

---

## 💻 Kotlin Programmatic Usage

### Basic Usage

```kotlin
val textLinker = findViewById<TextLinkerView>(R.id.textLinker)

// Set text - links are automatically detected
textLinker.text = "Visit www.example.com or email contact@example.com"

// Manually trigger detection (when autoDetect is off)
textLinker.detectAndApplyLinks()
```

### Runtime Customization

```kotlin
val textLinker = findViewById<TextLinkerView>(R.id.textLinker)

// Update configuration at runtime
textLinker.updateConfiguration {
    // Disable phone detection
    detectPhones = false
    
    // Change URL color to red
    urlColor = Color.RED
    
    // Make links bold
    linksBold = true
    
    // Disable underlines
    urlUnderline = false
    emailUnderline = false
    
    // Enable long press
    enableLongPress = true
    longPressDuration = 1000
    
    // Custom regex for URLs (example)
    customUrlRegex = "https?://[\\w.-]+\\.[a-z]{2,}"
}

// Get current configuration (read-only)
val config = textLinker.getConfiguration()
println("URLs enabled: ${config.detectUrls}")
println("URL color: ${config.urlColor}")
```

### Complete Programmatic Setup

```kotlin
val textLinker = findViewById<TextLinkerView>(R.id.textLinker)

// Create custom configuration
val customConfig = LinkConfiguration(
    detectUrls = true,
    detectEmails = true,
    detectPhones = false,
    urlColor = Color.parseColor("#FF5722"),
    emailColor = Color.parseColor("#4CAF50"),
    urlUnderline = true,
    emailUnderline = false,
    linksBold = true,
    linksItalic = false,
    enablePressHighlight = true,
    pressHighlightColor = Color.LTGRAY,
    stripTrailingPunctuation = true
)

// Apply configuration
textLinker.setConfiguration(customConfig)

// Set text
textLinker.text = "Contact us at info@company.com or visit our website www.company.com"
```

### Disable Clicks But Keep Styling

```kotlin
textLinker.updateConfiguration {
    // Links will be styled but not clickable
    enableUrlClick = false
    enableEmailClick = false
    enablePhoneClick = false
    
    // Make them gray to indicate they're disabled
    urlColor = Color.GRAY
    emailColor = Color.GRAY
    phoneColor = Color.GRAY
}
```

---

## 🔧 XML Attributes Reference

### Detection Control

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| tl_detectUrls | boolean | true | Enable URL detection |
| tl_detectEmails | boolean | true | Enable email detection |
| tl_detectPhones | boolean | true | Enable phone number detection |
| tl_autoDetect | boolean | true | Auto-detect links when text changes |

### Colors

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| tl_urlColor | color | #0000FF | Color for URL links |
| tl_emailColor | color | #0000FF | Color for email links |
| tl_phoneColor | color | #0000FF | Color for phone links |
| tl_pressHighlightColor | color | #CCCCCC | Highlight color when pressed |

### Underline Styling

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| tl_urlUnderline | boolean | true | Underline URLs |
| tl_emailUnderline | boolean | true | Underline emails |
| tl_phoneUnderline | boolean | true | Underline phone numbers |

### Interaction Behavior

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| tl_enablePressHighlight | boolean | true | Show highlight when link pressed |
| tl_enableLongPress | boolean | false | Enable long press detection |
| tl_longPressDuration | integer | 500 | Long press duration (milliseconds) |

### Click Action Control

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| tl_enableUrlClick | boolean | true | Enable URL click action |
| tl_enableEmailClick | boolean | true | Enable email click action |
| tl_enablePhoneClick | boolean | true | Enable phone click action |

### Custom Regex Patterns

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| tl_customUrlRegex | string | null | Custom regex for URL detection |
| tl_customEmailRegex | string | null | Custom regex for email detection |
| tl_customPhoneRegex | string | null | Custom regex for phone detection |

### Text Formatting

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| tl_linksBold | boolean | false | Make links bold |
| tl_linksItalic | boolean | false | Make links italic |

### Edge Case Handling

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| tl_stripTrailingPunctuation | boolean | true | Remove trailing punctuation from links |
| tl_detectInSpannableText | boolean | false | Detect links in already styled text |

### Accessibility

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| tl_addAccessibilityHints | boolean | true | Add content descriptions for screen readers |

---

## 📝 Public Methods

### TextLinkerView Methods

```kotlin
// Manually trigger link detection on current text
fun detectAndApplyLinks()

// Update configuration with lambda
fun updateConfiguration(block: LinkConfiguration.() -> Unit)

// Get current configuration (read-only copy)
fun getConfiguration(): LinkConfiguration

// Set complete new configuration
fun setConfiguration(newConfig: LinkConfiguration)
```

### LinkConfiguration Properties

```kotlin
// Detection flags
var detectUrls: Boolean
var detectEmails: Boolean
var detectPhones: Boolean
var autoDetect: Boolean

// Colors
var urlColor: Int
var emailColor: Int
var phoneColor: Int
var pressHighlightColor: Int

// Underline styling
var urlUnderline: Boolean
var emailUnderline: Boolean
var phoneUnderline: Boolean

// Interaction
var enablePressHighlight: Boolean
var enableLongPress: Boolean
var longPressDuration: Int

// Click actions
var enableUrlClick: Boolean
var enableEmailClick: Boolean
var enablePhoneClick: Boolean

// Custom regex
var customUrlRegex: String?
var customEmailRegex: String?
var customPhoneRegex: String?

// Text formatting
var linksBold: Boolean
var linksItalic: Boolean

// Edge cases
var stripTrailingPunctuation: Boolean
var detectInSpannableText: Boolean

// Accessibility
var addAccessibilityHints: Boolean
```

---

## 🎯 Detection Examples

### URL Detection

**Supported formats:**
- `https://google.com`
- `http://example.com`
- `www.github.com`
- `stackoverflow.com`
- `docs.android.com/guide`

**Example text:**
```
"Visit https://google.com or www.github.com for code samples"
```

**Result:** Both URLs become clickable and open in browser

---

### Email Detection

**Supported formats:**
- `user@example.com`
- `support@company.co.uk`
- `hello.world@subdomain.domain.com`

**Example text:**
```
"Contact us at support@example.com for help"
```

**Result:** Email becomes clickable and opens mail app with pre-filled address

---

### Phone Detection

**Supported formats:**
- `+91 9876543210`
- `(123) 456-7890`
- `123-456-7890`
- `555-1234`
- `1-800-555-0199`

**Example text:**
```
"Call +91 9876543210 for immediate assistance"
```

**Result:** Phone number becomes clickable and opens dialer

---

### Mixed Content Detection

**Example text:**
```
"Email test@gmail.com or visit openai.com or call 9876543210 for support"
```

**Result:**
- `test@gmail.com` → Opens email app
- `openai.com` → Opens browser
- `9876543210` → Opens dialer

All detected independently with proper click handling!

---

## 🛡️ Edge Cases Handled

### Trailing Punctuation
```
"Visit google.com." → Detects "google.com" (strips period)
"Email: test@test.com," → Detects "test@test.com" (strips comma)
"Check www.site.com!" → Detects "www.site.com" (strips exclamation)
```

### Multiple Links in Same Line
```
"Visit google.com and github.com today"
→ Both URLs detected separately
```

### Overlapping Matches
```
"user@domain.com" → Detected as EMAIL (not URL + EMAIL)
Library intelligently resolves overlaps by priority
```

### Empty or Null Text
```kotlin
textLinker.text = null  // Handled safely, no crash
textLinker.text = ""    // No processing, no crash
```

### Already Clickable Text
```
If text already has spans, library handles gracefully
No duplicate spans created
```

---

## 🏗️ Architecture

TextLinker follows clean architecture principles:

```
TextLinkerView (Custom TextView)
       ↓
AttributeReader → LinkConfiguration
       ↓
LinkDetector (Regex matching)
       ↓
LinkSpanHandler (Creates clickable spans)
       ↓
Click Events → Intent (Browser/Email/Dialer)
```

**Components:**
- **TextLinkerView**: Main entry point, extends AppCompatTextView
- **LinkConfiguration**: Holds all settings from XML
- **AttributeReader**: Parses XML attributes
- **LinkDetector**: Core detection engine with regex
- **LinkSpanHandler**: Creates and manages clickable spans
- **LinkType**: Enum for URL/EMAIL/PHONE types

---

## 🎨 Customization Examples

### Example 1: Corporate Theme
```xml
<com.ext.text_linker.TextLinkerView
    android:text="Contact: info@company.com | Web: www.company.com"
    app:tl_urlColor="#1976D2"
    app:tl_emailColor="#388E3C"
    app:tl_linksBold="true"
    app:tl_urlUnderline="false"
    app:tl_emailUnderline="false" />
```

### Example 2: Minimalist Style
```xml
<com.ext.text_linker.TextLinkerView
    android:text="Simple links: example.com"
    app:tl_urlColor="#333333"
    app:tl_urlUnderline="false"
    app:tl_enablePressHighlight="false" />
```

### Example 3: Vibrant Design
```xml
<com.ext.text_linker.TextLinkerView
    android:text="Colorful links everywhere!"
    app:tl_urlColor="#E91E63"
    app:tl_emailColor="#00BCD4"
    app:tl_phoneColor="#FF9800"
    app:tl_linksBold="true"
    app:tl_linksItalic="true"
    app:tl_pressHighlightColor="#FFE082" />
```

### Example 4: Read-Only Display
```xml
<com.ext.text_linker.TextLinkerView
    android:text="Links shown but not clickable"
    app:tl_enableUrlClick="false"
    app:tl_enableEmailClick="false"
    app:tl_enablePhoneClick="false"
    app:tl_urlColor="#999999"
    app:tl_urlUnderline="false" />
```

---

## ⚡ Performance

- **Efficient Regex**: Optimized patterns for fast detection
- **Smart Caching**: Avoids duplicate processing
- **Lazy Initialization**: Components loaded only when needed
- **Memory Safe**: Proper lifecycle handling, no memory leaks
- **Orientation Safe**: Configuration preserved across rotations

---

## 📄 License

```
MIT License

Copyright (c) 2025 Excelsior Technologies

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---
