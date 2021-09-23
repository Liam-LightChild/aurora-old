package com.liamcoalstudio.aurora

expect class Window {
    fun close()
    fun activate()

    inline fun <T> with(crossinline f: Window.() -> T)

    companion object {
        fun windowed(title: String, width: Int, height: Int, share: Window? = null): Window
        fun fullscreen(title: String, share: Window? = null): Window
    }
}