package com.liamcoalstudio.aurora.window

expect class WindowHandle {
    fun use()
    fun close()

    var shouldClose: Boolean
}