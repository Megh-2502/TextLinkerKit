package com.ext.textlinker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ext.text_linker.TextLinkerView

/**
 * MainActivity - Demonstrates TextLinker library usage
 *
 * Shows how minimal code is needed when everything is configured in XML.
 * The library automatically detects and linkifies text based on XML attributes.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ========== MINIMAL USAGE ==========
        // TextLinkerView handles everything automatically via XML configuration
        // No additional code needed for basic usage!

        // ========== OPTIONAL: PROGRAMMATIC CUSTOMIZATION ==========
        // If you need to modify behavior at runtime (rare):

        try {
            val customTextView = findViewById<TextLinkerView>(R.id.textLinkerCustom)

            // Example: Disable phone detection programmatically
            customTextView?.updateConfiguration {
                detectPhones = false
                urlColor = android.graphics.Color.RED
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // ========== OPTIONAL: MANUAL DETECTION TRIGGER ==========
        // Useful when autoDetect is disabled:

        try {
            val manualTextView = findViewById<TextLinkerView>(R.id.textLinkerManual)

            // Set text without auto-detection
            manualTextView?.text = "Visit www.example.com for more info"

            // Manually trigger detection later
            manualTextView?.detectAndApplyLinks()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        // ========== THAT'S IT! ==========
        // The library handles:
        // ✅ Link detection (URLs, emails, phones)
        // ✅ Click handling (browser, email app, dialer)
        // ✅ Styling (colors, underlines, bold, italic)
        // ✅ Edge cases (trailing punctuation, overlaps)
        // ✅ Accessibility hints

        // Everything is configured via XML attributes (see activity_main.xml)
    }
}