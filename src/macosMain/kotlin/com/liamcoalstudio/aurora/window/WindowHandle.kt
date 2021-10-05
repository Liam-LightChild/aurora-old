package com.liamcoalstudio.aurora.window

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.reinterpret

actual class WindowHandle(val handle: COpaquePointer) {
    actual fun use() {
        glfwMakeContextCurrent(handle.reinterpret())
    }

    actual fun close() {
        glfwDestroyWindow(handle.reinterpret())
    }
}