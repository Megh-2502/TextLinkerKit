package com.ext.text_linker

/**
 * Enum representing different types of detectable links
 * Each type has associated default regex patterns and action intents
 */
enum class LinkType {
    URL,    // Web URLs (http, https, www, domains)
    EMAIL,  // Email addresses
    PHONE;  // Phone numbers (various formats)

    /**
     * Get default regex pattern for this link type
     */
    fun getDefaultPattern(): String {
        return when (this) {
            URL -> {
                // Matches: http://, https://, www., domain.com
                """(?i)\b((?:https?://|www\d{0,3}[.]|[a-z0-9.\-]+[.][a-z]{2,4}/)(?:[^\s()<>]+|\(([^\s()<>]+|(\([^\s()<>]+\)))*\))+(?:\(([^\s()<>]+|(\([^\s()<>]+\)))*\)|[^\s`!()\[\]{};:'".,<>?«»""'']))"""
            }

            EMAIL -> {
                // Matches: user@domain.com
                """[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}"""
            }

            PHONE -> {
                // Matches: +91 9876543210, (123) 456-7890, 123-456-7890, etc.
                """(\+?\d{1,3}[-.\s]?)?\(?\d{3}\)?[-.\s]?\d{3,4}[-.\s]?\d{4}"""
            }
        }
    }

    /**
     * Get intent action for this link type
     */
    fun getIntentAction(): String {
        return when (this) {
            URL -> android.content.Intent.ACTION_VIEW
            EMAIL -> android.content.Intent.ACTION_SENDTO
            PHONE -> android.content.Intent.ACTION_DIAL
        }
    }

    /**
     * Prepare URL for intent (add missing protocols, prefixes)
     */
    fun prepareForIntent(rawText: String): String {
        return when (this) {
            URL -> {
                var url = rawText.trim()
                // Add http:// if no protocol specified
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://$url"
                }
                url
            }

            EMAIL -> {
                // Add mailto: prefix
                "mailto:${rawText.trim()}"
            }

            PHONE -> {
                // Add tel: prefix
                "tel:${rawText.trim()}"
            }
        }
    }
}