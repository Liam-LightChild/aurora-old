package com.liamcoalstudio.aurora

import com.liamcoalstudio.aurora.enums.Key

actual class Window {
    actual fun close(): Unit = TODO()
    actual fun activate(): Unit = TODO()

    actual inline fun onKey(f: (Key) -> Unit): Unit = TODO()
    actual inline fun <T> with(crossinline f: Window.() -> T): Unit = TODO()

    actual companion object {
        actual fun windowed(
            title: String,
            width: Int,
            height: Int,
            share: Window?,
        ): Window = TODO("not implemented")

        actual fun fullscreen(
            title: String,
            share: Window?,
        ): Window = TODO("not implemented")
    }
}
