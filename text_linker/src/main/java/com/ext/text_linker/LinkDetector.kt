package com.ext.text_linker

import android.util.Log
import java.util.regex.Pattern

/**
 * Core detection engine that finds URLs, emails, and phone numbers in text
 * Handles edge cases like trailing punctuation, overlapping matches, etc.
 */
class LinkDetector(private val config: LinkConfiguration) {

    companion object {
        private const val TAG = "LinkDetector"

        // Trailing punctuation to strip (.,!?;:)
        private val TRAILING_PUNCTUATION = setOf('.', ',', '!', '?', ';', ':', ')', ']', '}')
    }

    /**
     * Detect all links in the given text
     *
     * @param text Input text to analyze
     * @return List of detected LinkMatch objects
     */
    fun detectLinks(text: CharSequence?): List<LinkSpanHandler.LinkMatch> {
        if (text.isNullOrEmpty()) return emptyList()

        val matches = mutableListOf<LinkSpanHandler.LinkMatch>()

        // Detect each type if enabled
        if (config.detectUrls) {
            matches.addAll(detectType(text, LinkType.URL))
        }

        if (config.detectEmails) {
            matches.addAll(detectType(text, LinkType.EMAIL))
        }

        if (config.detectPhones) {
            matches.addAll(detectType(text, LinkType.PHONE))
        }

        // Remove overlapping matches (prioritize longer matches)
        val filteredMatches = removeOverlaps(matches)

        Log.d(TAG, "Detected ${filteredMatches.size} links in text")

        return filteredMatches
    }

    /**
     * Detect links of a specific type using regex
     */
    private fun detectType(
        text: CharSequence,
        type: LinkType
    ): List<LinkSpanHandler.LinkMatch> {
        val matches = mutableListOf<LinkSpanHandler.LinkMatch>()

        try {
            val pattern = Pattern.compile(config.getPattern(type))
            val matcher = pattern.matcher(text)

            while (matcher.find()) {
                var start = matcher.start()
                var end = matcher.end()
                var matchedText = text.substring(start, end)

                // Strip trailing punctuation if enabled
                if (config.stripTrailingPunctuation) {
                    while (end > start && matchedText.last() in TRAILING_PUNCTUATION) {
                        end--
                        matchedText = matchedText.dropLast(1)
                    }
                }

                // Validate the match
                if (isValidMatch(matchedText, type)) {
                    matches.add(
                        LinkSpanHandler.LinkMatch(
                            text = matchedText,
                            start = start,
                            end = end,
                            type = type
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error detecting ${type.name}", e)
        }

        return matches
    }

    /**
     * Validate detected match based on type-specific rules
     */
    private fun isValidMatch(text: String, type: LinkType): Boolean {
        if (text.isEmpty()) return false

        return when (type) {
            LinkType.URL -> {
                // Must have at least one dot for domain
                text.contains('.') && text.length >= 4
            }

            LinkType.EMAIL -> {
                // Must have @ and dot
                text.contains('@') && text.contains('.') && text.length >= 5
            }

            LinkType.PHONE -> {
                // Must have at least 7 digits
                val digitCount = text.count { it.isDigit() }
                digitCount >= 7
            }
        }
    }

    /**
     * Remove overlapping matches, keeping longer/higher priority ones
     * Priority: URL > EMAIL > PHONE
     */
    private fun removeOverlaps(
        matches: List<LinkSpanHandler.LinkMatch>
    ): List<LinkSpanHandler.LinkMatch> {
        if (matches.size <= 1) return matches

        // Sort by start position, then by length (descending), then by type priority
        val sorted = matches.sortedWith(
            compareBy<LinkSpanHandler.LinkMatch> { it.start }
                .thenByDescending { it.end - it.start } // Length
                .thenBy { getTypePriority(it.type) }
        )

        val result = mutableListOf<LinkSpanHandler.LinkMatch>()

        for (match in sorted) {
            // Check if this match overlaps with any already accepted match
            val overlaps = result.any { existing ->
                doRangesOverlap(match.start, match.end, existing.start, existing.end)
            }

            if (!overlaps) {
                result.add(match)
            }
        }

        return result
    }

    /**
     * Get priority value for link type (lower = higher priority)
     */
    private fun getTypePriority(type: LinkType): Int {
        return when (type) {
            LinkType.URL -> 1
            LinkType.EMAIL -> 2
            LinkType.PHONE -> 3
        }
    }

    /**
     * Check if two ranges overlap
     */
    private fun doRangesOverlap(start1: Int, end1: Int, start2: Int, end2: Int): Boolean {
        return start1 < end2 && start2 < end1
    }
}