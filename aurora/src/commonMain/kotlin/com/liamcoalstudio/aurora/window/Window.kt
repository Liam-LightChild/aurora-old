package com.liamcoalstudio.aurora.window

import com.liamcoalstudio.aurora.Internal
import com.liamcoalstudio.aurora.Vector2i

fun openWindow(f: WindowOpenBuilder.() -> Unit): WindowHandle {
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

expect fun getFullscreenSize(): Vector2i

@Deprecated("To be removed", level = DeprecationLevel.ERROR)
object Window {
    @Deprecated("Use `openWindow`", ReplaceWith("openWindow(f)", "openWindow"))
    fun open(f: WindowOpenBuilder.() -> Unit): WindowHandle = openWindow(f)
}

open class WindowOpenBuilder {
    lateinit var span: WindowSpan private set
    lateinit var title: String private set
    var shareWindow: WindowHandle? = null
        private set
    val config = mutableMapOf<WindowConfig, Any>()

    fun title(string: String) {
        title = string
    }

    fun span(windowSpan: WindowSpan) {
        this.span = windowSpan
    }

    fun share(windowHandle: WindowHandle) {
        shareWindow = windowHandle
    }

    inline fun windowed(width: Int, height: Int) = span(WindowSpan.Windowed(width, height))

    inline fun fullscreen() = span(WindowSpan.Fullscreen)

    /**
     * Does not affect configuration option [WindowConfig.Visible] directly.
     * Use [visible] for that.
     */
    inline fun invisible() = span(WindowSpan.Invisible)

    inline fun visible(value: Boolean) {
        config[WindowConfig.Visible] = value
    }

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Use individual configuration options.")
    infix fun WindowConfig.setTo(value: Any) {
        config[this] = value
    }
}
