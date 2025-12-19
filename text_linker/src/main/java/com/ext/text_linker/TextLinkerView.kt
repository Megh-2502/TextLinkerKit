package com.ext.text_linker

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * TextLinkerView - Custom TextView with automatic link detection
 *
 * This is the main entry point for the TextLinker library.
 * It extends AppCompatTextView and automatically detects and linkifies
 * URLs, emails, and phone numbers based on XML configuration.
 *
 * Features:
 * - Auto-detection of URLs, emails, phone numbers
 * - Fully XML-configurable (colors, behavior, styling)
 * - Click and long-press support
 * - Edge case handling (trailing punctuation, overlaps)
 * - Production-ready with accessibility support
 *
 * Usage:
 * Add to XML layout and configure via tl_* attributes
 */
class TextLinkerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    // Configuration loaded from XML attributes
    // IMPORTANT: Initialize immediately to avoid null pointer in setText
    private val config: LinkConfiguration = AttributeReader.parseAttributes(context, attrs)

    // Core components - lazy initialization to avoid premature access
    private val linkDetector: LinkDetector by lazy { LinkDetector(config) }
    private val spanHandler: LinkSpanHandler by lazy { LinkSpanHandler(context, config) }

    // Flag to track if initialization is complete
    private var isInitialized = false

    init {
        // Enable link clicking
        movementMethod = LinkMovementMethod.getInstance()

        // Make links focusable for accessibility
        if (config.addAccessibilityHints) {
            isFocusable = true
            isFocusableInTouchMode = false
        }

        // Mark initialization complete
        isInitialized = true

        // If auto-detect is enabled and text already exists, process it
        // This happens after the parent constructor has set initial text
        if (config.autoDetect && !text.isNullOrEmpty()) {
            post {
                // Post to ensure we're fully initialized
                detectAndApplyLinks()
            }
        }
    }

    /**
     * Override setText to automatically detect links when text changes
     * CRITICAL: Must check initialization to avoid null pointer during construction
     */
    override fun setText(text: CharSequence?, type: BufferType?) {
        // During construction, parent TextView calls setText before our init block
        // So we need to check if we're initialized first
        if (!isInitialized || !config.autoDetect || text.isNullOrEmpty()) {
            super.setText(text, type)
            return
        }

        try {
            // Detect links and apply spans
            val processedText = processText(text)
            super.setText(processedText, BufferType.SPANNABLE)
        } catch (e: Exception) {
            // Fallback to regular text if processing fails
            super.setText(text, type)
        }
    }

    /**
     * Manually trigger link detection on current text
     * Useful when autoDetect is disabled or for programmatic refresh
     */
    fun detectAndApplyLinks() {
        val currentText = text
        if (!currentText.isNullOrEmpty()) {
            try {
                val processedText = processText(currentText)
                super.setText(processedText, BufferType.SPANNABLE)
            } catch (e: Exception) {
                // Keep current text if processing fails
            }
        }
    }

    /**
     * Process text: detect links and apply clickable spans
     */
    private fun processText(text: CharSequence): CharSequence {
        // Detect all links
        val matches = linkDetector.detectLinks(text)

        if (matches.isEmpty()) {
            return text // No links found
        }

        // Apply spans to create clickable links
        return spanHandler.applySpans(text, matches)
    }

    /**
     * Update configuration at runtime (advanced usage)
     * Allows programmatic customization beyond XML
     *
     * @param block Lambda to modify configuration
     */
    fun updateConfiguration(block: LinkConfiguration.() -> Unit) {
        config.block()
        // Re-process current text with new config
        if (!text.isNullOrEmpty()) {
            detectAndApplyLinks()
        }
    }

    /**
     * Get current configuration (read-only access)
     */
    fun getConfiguration(): LinkConfiguration = config.copy()

    /**
     * Manually set configuration (for complete override)
     */
    fun setConfiguration(newConfig: LinkConfiguration) {
        // Copy values from new config
        config.detectUrls = newConfig.detectUrls
        config.detectEmails = newConfig.detectEmails
        config.detectPhones = newConfig.detectPhones
        config.autoDetect = newConfig.autoDetect
        config.urlColor = newConfig.urlColor
        config.emailColor = newConfig.emailColor
        config.phoneColor = newConfig.phoneColor
        config.pressHighlightColor = newConfig.pressHighlightColor
        config.urlUnderline = newConfig.urlUnderline
        config.emailUnderline = newConfig.emailUnderline
        config.phoneUnderline = newConfig.phoneUnderline
        config.enablePressHighlight = newConfig.enablePressHighlight
        config.enableLongPress = newConfig.enableLongPress
        config.longPressDuration = newConfig.longPressDuration
        config.enableUrlClick = newConfig.enableUrlClick
        config.enableEmailClick = newConfig.enableEmailClick
        config.enablePhoneClick = newConfig.enablePhoneClick
        config.customUrlRegex = newConfig.customUrlRegex
        config.customEmailRegex = newConfig.customEmailRegex
        config.customPhoneRegex = newConfig.customPhoneRegex
        config.linksBold = newConfig.linksBold
        config.linksItalic = newConfig.linksItalic
        config.stripTrailingPunctuation = newConfig.stripTrailingPunctuation
        config.detectInSpannableText = newConfig.detectInSpannableText
        config.addAccessibilityHints = newConfig.addAccessibilityHints

        // Re-process text
        if (!text.isNullOrEmpty()) {
            detectAndApplyLinks()
        }
    }
}