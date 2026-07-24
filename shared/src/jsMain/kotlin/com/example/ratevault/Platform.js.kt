package com.example.ratevault

import web.navigator.navigator
import kotlin.js.Date

class JsPlatform: Platform {
    private val userAgent = navigator.userAgent
    private val browserList = listOf("Chrome", "Firefox", "Safari", "Edge")

    override val name: String = userAgent.findAnyOf(browserList, ignoreCase = true)
            ?.let { (startIndex) -> userAgent.substring(startIndex).substringBefore(" ") }
            ?: "Unknown"
}

actual fun getPlatform(): Platform = JsPlatform()

actual fun getCurrentDate(): String {
    val date = Date()
    val options = js("({ month: 'long', day: '2-digit', year: 'numeric' })")
    return date.asDynamic().toLocaleDateString("en-US", options) as String
}
