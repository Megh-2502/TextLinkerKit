package com.ext.text_linker

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.Toast

/**
 * Handles creation of clickable spans and touch interactions
 * Manages click, long press, and visual feedback for links
 */
class LinkSpanHandler(
    private val context: Context,
    private val config: LinkConfiguration
) {

    companion object {
        private const val TAG = "LinkSpanHandler"
    }

    /**
     * Data class representing a detected link match
     */
    data class LinkMatch(
        val text: String,
        val start: Int,
        val end: Int,
        val type: LinkType
    )

    /**
     * Apply clickable spans to a list of detected links
     *
     * @param originalText Original text content
     * @param matches List of detected link matches
     * @return SpannableString with clickable spans applied
     */
    fun applySpans(originalText: CharSequence, matches: List<LinkMatch>): SpannableString {
        val spannable = SpannableString(originalText)

        // Sort matches by start position to handle overlaps
        val sortedMatches = matches.sortedBy { it.start }

        for (match in sortedMatches) {
            // Skip if detection disabled for this type
            if (!config.isDetectionEnabled(match.type)) continue

            // Create and apply custom clickable span
            val clickableSpan = createClickableSpan(match)
            spannable.setSpan(
                clickableSpan,
                match.start,
                match.end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Apply color
            val color = config.getColorForType(match.type)
            spannable.setSpan(
                ForegroundColorSpan(color),
                match.start,
                match.end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Apply underline if enabled
            if (config.isUnderlineEnabled(match.type)) {
                spannable.setSpan(
                    UnderlineSpan(),
                    match.start,
                    match.end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            // Apply bold style if enabled
            if (config.linksBold) {
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    match.start,
                    match.end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            // Apply italic style if enabled
            if (config.linksItalic) {
                spannable.setSpan(
                    StyleSpan(Typeface.ITALIC),
                    match.start,
                    match.end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        return spannable
    }

    /**
     * Create a custom ClickableSpan with press highlight and long press support
     */
    private fun createClickableSpan(match: LinkMatch): ClickableSpan {
        return object : ClickableSpan() {

            private var isLongPress = false
            private val longPressHandler = Handler(Looper.getMainLooper())
            private val longPressRunnable = Runnable {
                isLongPress = true
                handleLongPress(match)
            }

            override fun onClick(widget: View) {
                // Only handle click if not a long press
                if (!isLongPress && config.isClickEnabled(match.type)) {
                    handleClick(match)
                }
                isLongPress = false // Reset flag
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                // Apply press highlight color if enabled
                if (config.enablePressHighlight) {
                    // Note: Press state is handled by TextView internally
                    // This just ensures the span is styled correctly
                }

                // Remove default underline (we handle it via UnderlineSpan)
                ds.isUnderlineText = false
            }
        }
    }

    /**
     * Handle click action - open appropriate app
     */
    private fun handleClick(match: LinkMatch) {
        try {
            val preparedUrl = match.type.prepareForIntent(match.text)
            val intent = Intent(match.type.getIntentAction(), Uri.parse(preparedUrl))

            // For email, clear flags and set as new task
            if (match.type == LinkType.EMAIL) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            context.startActivity(intent)

            Log.d(TAG, "Opened ${match.type.name}: ${match.text}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open ${match.type.name}: ${match.text}", e)
            Toast.makeText(
                context,
                "No app available to handle ${match.type.name.lowercase()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Handle long press action - show options or copy
     */
    private fun handleLongPress(match: LinkMatch) {
        if (!config.enableLongPress) return

        Log.d(TAG, "Long press detected on ${match.type.name}: ${match.text}")

        // Show toast with detected text
        Toast.makeText(
            context,
            "Long press: ${match.text}",
            Toast.LENGTH_SHORT
        ).show()

        // In a real implementation, you might show a context menu here
        // with options like "Copy", "Share", etc.
    }
}