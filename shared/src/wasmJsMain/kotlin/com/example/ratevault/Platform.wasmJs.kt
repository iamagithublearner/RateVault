package com.example.ratevault

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

actual fun getCurrentDate(): String = jsDateToString()

@OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
private fun jsDateToString(): String = js("""
    new Date().toLocaleDateString('en-US', { month: 'long', day: '2-digit', year: 'numeric' })
""")
