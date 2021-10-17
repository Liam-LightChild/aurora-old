package com.liamcoalstudio.aurora.window

import com.liamcoalstudio.aurora.Vector2i

expect class WindowHandle {
    fun use()
    fun close()

    val size: Vector2i
    var shouldClose: Boolean
}