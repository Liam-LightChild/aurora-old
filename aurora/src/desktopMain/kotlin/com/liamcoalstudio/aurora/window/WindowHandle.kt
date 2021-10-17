package com.liamcoalstudio.aurora.window

import com.liamcoalstudio.aurora.*
import kotlinx.cinterop.*

actual class WindowHandle(val handle: COpaquePointer) {
    actual fun use() {
        glfwMakeContextCurrent(handle.reinterpret())
    }

    actual fun close() {
        glfwDestroyWindow(handle.reinterpret())
    }

    actual val size: Vector2i
        get() {
            val a = IntArray(2)
            a.usePinned { glfwGetWindowSize(handle.reinterpret(), it.addressOf(0), it.addressOf(1)) }
            return Vector2i(a[0], a[1])
        }

    actual var shouldClose: Boolean
        get() = glfwWindowShouldClose(handle.reinterpret()) != 0
        set(value) { glfwSetWindowShouldClose(handle.reinterpret(), if(value) 1 else 0) }
}
