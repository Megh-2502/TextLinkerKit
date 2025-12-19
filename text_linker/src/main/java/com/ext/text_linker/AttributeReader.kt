package com.ext.text_linker

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet

/**
 * Reads and parses XML attributes into LinkConfiguration
 * Handles all attribute extraction and default value assignment
 */
object AttributeReader {

    /**
     * Parse XML attributes from AttributeSet and populate LinkConfiguration
     *
     * @param context Android context
     * @param attrs AttributeSet from XML
     * @return Fully configured LinkConfiguration instance
     */
    fun parseAttributes(context: Context, attrs: AttributeSet?): LinkConfiguration {
        val config = LinkConfiguration()

        if (attrs == null) {
            return config // Return defaults if no attributes
        }

        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.TextLinkerView
        )

        try {
            // ========== DETECTION FLAGS ==========
            config.detectUrls = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_detectUrls,
                config.detectUrls
            )

            config.detectEmails = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_detectEmails,
                config.detectEmails
            )

            config.detectPhones = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_detectPhones,
                config.detectPhones
            )

            config.autoDetect = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_autoDetect,
                config.autoDetect
            )

            // ========== COLORS ==========
            config.urlColor = typedArray.getColor(
                R.styleable.TextLinkerView_tl_urlColor,
                config.urlColor
            )

            config.emailColor = typedArray.getColor(
                R.styleable.TextLinkerView_tl_emailColor,
                config.emailColor
            )

            config.phoneColor = typedArray.getColor(
                R.styleable.TextLinkerView_tl_phoneColor,
                config.phoneColor
            )

            config.pressHighlightColor = typedArray.getColor(
                R.styleable.TextLinkerView_tl_pressHighlightColor,
                config.pressHighlightColor
            )

            // ========== UNDERLINE STYLING ==========
            config.urlUnderline = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_urlUnderline,
                config.urlUnderline
            )

            config.emailUnderline = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_emailUnderline,
                config.emailUnderline
            )

            config.phoneUnderline = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_phoneUnderline,
                config.phoneUnderline
            )

            // ========== INTERACTION ==========
            config.enablePressHighlight = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_enablePressHighlight,
                config.enablePressHighlight
            )

            config.enableLongPress = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_enableLongPress,
                config.enableLongPress
            )

            config.longPressDuration = typedArray.getInt(
                R.styleable.TextLinkerView_tl_longPressDuration,
                config.longPressDuration
            )

            // ========== CLICK ACTIONS ==========
            config.enableUrlClick = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_enableUrlClick,
                config.enableUrlClick
            )

            config.enableEmailClick = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_enableEmailClick,
                config.enableEmailClick
            )

            config.enablePhoneClick = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_enablePhoneClick,
                config.enablePhoneClick
            )

            // ========== CUSTOM REGEX ==========
            config.customUrlRegex = typedArray.getString(
                R.styleable.TextLinkerView_tl_customUrlRegex
            )

            config.customEmailRegex = typedArray.getString(
                R.styleable.TextLinkerView_tl_customEmailRegex
            )

            config.customPhoneRegex = typedArray.getString(
                R.styleable.TextLinkerView_tl_customPhoneRegex
            )

            // ========== TEXT FORMATTING ==========
            config.linksBold = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_linksBold,
                config.linksBold
            )

            config.linksItalic = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_linksItalic,
                config.linksItalic
            )

            // ========== EDGE CASES ==========
            config.stripTrailingPunctuation = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_stripTrailingPunctuation,
                config.stripTrailingPunctuation
            )

            config.detectInSpannableText = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_detectInSpannableText,
                config.detectInSpannableText
            )

            // ========== ACCESSIBILITY ==========
            config.addAccessibilityHints = typedArray.getBoolean(
                R.styleable.TextLinkerView_tl_addAccessibilityHints,
                config.addAccessibilityHints
            )

        } finally {
            typedArray.recycle() // Always recycle to prevent memory leaks
        }

        return config
    }
}