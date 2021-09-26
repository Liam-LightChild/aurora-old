package com.liamcoalstudio.aurora

@DslMarker annotation class WindowOpenDSL

expect enum class WindowConfig {
    WINDOW_VISIBLE
}

sealed class WindowSpan {
    class Windowed(val width: Int, val height: Int) : WindowSpan() {
        override fun toString(): String {
            return "Windowed(width=$width, height=$height)"
        }
    }
    object Fullscreen : WindowSpan()
    object Invisible : WindowSpan()
}

object Window {
    fun open(f: WindowOpenBuilder.() -> Unit): WindowHandle {
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
                config[WindowConfig.WINDOW_VISIBLE] = false
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
}

open class WindowOpenBuilder {
    internal lateinit var span: WindowSpan
    internal lateinit var title: String
    internal var shareWindow: WindowHandle? = null
    internal val config = mutableMapOf<WindowConfig, Any>()

    @WindowOpenDSL
    fun title(string: String) {
        title = string
    }

    @WindowOpenDSL
    fun span(windowSpan: WindowSpan) {
        this.span = windowSpan
    }

    @WindowOpenDSL
    fun share(windowHandle: WindowHandle) {
        shareWindow = windowHandle
    }

    @WindowOpenDSL
    inline fun windowed(width: Int, height: Int) = span(WindowSpan.Windowed(width, height))

    @WindowOpenDSL
    inline fun fullscreen() = span(WindowSpan.Fullscreen)

    @WindowOpenDSL
    inline fun invisible() = span(WindowSpan.Invisible)

    @WindowOpenDSL
    infix fun WindowConfig.setTo(value: Any) {
        config[this] = value
    }
}
