package com.example.ratevault

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun getCurrentDate(): String
