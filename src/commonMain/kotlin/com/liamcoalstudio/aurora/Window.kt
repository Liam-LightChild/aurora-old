@file:OptIn(ExperimentalContracts::class)
package com.liamcoalstudio.aurora

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@AuroraDSLMarker
inline fun openWindow(crossinline f: WindowOpenBuilder.() -> Unit): WindowHandle {
    contract {
        callsInPlace(f, InvocationKind.EXACTLY_ONCE)
        returnsNotNull()
    }

    val b = WindowOpenBuilder().also(f)

    val isFullscreen: Boolean
    val width: Int?
    val height: Int?
    val config = b.config

    when(b.span) {
        WindowSpan.Fullscreen -> {
            isFullscreen = true
            width = null
            height = null
        }
        is WindowSpan.Windowed -> {
            isFullscreen = false
            width = (b.span as WindowSpan.Windowed).width
            height = (b.span as WindowSpan.Windowed).height
        }
        WindowSpan.Invisible -> {
            isFullscreen = false
            width = 32
            height = 32
            config[WindowConfig.Visible] = false
        }
    }

    return Internal.openWindow(
        name = b.title,
        fullscreen = isFullscreen,
        width = width,
        height = height,
        share = b.shareWindow,
        config = config.toMap()
    )
}

@Deprecated("To be removed", level = DeprecationLevel.ERROR)
object Window {
    @Deprecated("Use `openWindow`", ReplaceWith("openWindow(f)", "com.liamcoalstudio.aurora.openWindow"))
    fun open(f: WindowOpenBuilder.() -> Unit): WindowHandle = openWindow(f)
}

open class WindowOpenBuilder {
    lateinit var span: WindowSpan private set
    lateinit var title: String private set
    var shareWindow: WindowHandle? = null
        private set
    val config = mutableMapOf<WindowConfig, Any>()

    @AuroraDSLMarker
    fun title(string: String) {
        title = string
    }

    @AuroraDSLMarker
    fun span(windowSpan: WindowSpan) {
        this.span = windowSpan
    }

    @AuroraDSLMarker
    fun share(windowHandle: WindowHandle) {
        shareWindow = windowHandle
    }

    @AuroraDSLMarker
    inline fun windowed(width: Int, height: Int) = span(WindowSpan.Windowed(width, height))

    @AuroraDSLMarker
    inline fun fullscreen() = span(WindowSpan.Fullscreen)

    /**
     * Does not affect configuration option [WindowConfig.Visible] directly.
     * Use [visible] for that.
     */
    @AuroraDSLMarker
    inline fun invisible() = span(WindowSpan.Invisible)

    @AuroraDSLMarker
    inline fun visible(value: Boolean) {
        config[WindowConfig.Visible] = value
    }

    @AuroraDSLMarker
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Use individual configuration options.")
    infix fun WindowConfig.setTo(value: Any) {
        config[this] = value
    }
}
