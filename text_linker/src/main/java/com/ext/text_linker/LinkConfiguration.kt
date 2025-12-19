package com.ext.text_linker

import android.graphics.Color

/**
 * Configuration class holding all TextLinker settings
 * Populated from XML attributes via AttributeReader
 */
data class LinkConfiguration(
    // ========== DETECTION FLAGS ==========
    var detectUrls: Boolean = true,
    var detectEmails: Boolean = true,
    var detectPhones: Boolean = true,
    var autoDetect: Boolean = true,

    // ========== COLORS ==========
    var urlColor: Int = Color.BLUE,
    var emailColor: Int = Color.BLUE,
    var phoneColor: Int = Color.BLUE,
    var pressHighlightColor: Int = Color.LTGRAY,

    // ========== UNDERLINE STYLING ==========
    var urlUnderline: Boolean = true,
    var emailUnderline: Boolean = true,
    var phoneUnderline: Boolean = true,

    // ========== INTERACTION ==========
    var enablePressHighlight: Boolean = true,
    var enableLongPress: Boolean = false,
    var longPressDuration: Int = 500, // milliseconds

    // ========== CLICK ACTIONS ==========
    var enableUrlClick: Boolean = true,
    var enableEmailClick: Boolean = true,
    var enablePhoneClick: Boolean = true,

    // ========== CUSTOM REGEX ==========
    var customUrlRegex: String? = null,
    var customEmailRegex: String? = null,
    var customPhoneRegex: String? = null,

    // ========== TEXT FORMATTING ==========
    var linksBold: Boolean = false,
    var linksItalic: Boolean = false,

    // ========== EDGE CASES ==========
    var stripTrailingPunctuation: Boolean = true,
    var detectInSpannableText: Boolean = false,

    // ========== ACCESSIBILITY ==========
    var addAccessibilityHints: Boolean = true
) {
    /**
     * Get color for specific link type
     */
    fun getColorForType(type: LinkType): Int {
        return when (type) {
            LinkType.URL -> urlColor
            LinkType.EMAIL -> emailColor
            LinkType.PHONE -> phoneColor
        }
    }

    /**
     * Check if underline is enabled for specific type
     */
    fun isUnderlineEnabled(type: LinkType): Boolean {
        return when (type) {
            LinkType.URL -> urlUnderline
            LinkType.EMAIL -> emailUnderline
            LinkType.PHONE -> phoneUnderline
        }
    }

    /**
     * Check if detection is enabled for specific type
     */
    fun isDetectionEnabled(type: LinkType): Boolean {
        return when (type) {
            LinkType.URL -> detectUrls
            LinkType.EMAIL -> detectEmails
            LinkType.PHONE -> detectPhones
        }
    }

    /**
     * Check if click is enabled for specific type
     */
    fun isClickEnabled(type: LinkType): Boolean {
        return when (type) {
            LinkType.URL -> enableUrlClick
            LinkType.EMAIL -> enableEmailClick
            LinkType.PHONE -> enablePhoneClick
        }
    }

    /**
     * Get regex pattern for specific type (custom or default)
     */
    fun getPattern(type: LinkType): String {
        return when (type) {
            LinkType.URL -> customUrlRegex ?: type.getDefaultPattern()
            LinkType.EMAIL -> customEmailRegex ?: type.getDefaultPattern()
            LinkType.PHONE -> customPhoneRegex ?: type.getDefaultPattern()
        }
    }
}