package com.andrefpc.tvmazeclient.core.extensions

object StringExtensions {
    /**
     * Remove the html tags
     * @return String normalized
     */
    fun String.removeHtmlTags(): String {
        return this.replace(Regex("<[^>]*>"), "")
    }
}
