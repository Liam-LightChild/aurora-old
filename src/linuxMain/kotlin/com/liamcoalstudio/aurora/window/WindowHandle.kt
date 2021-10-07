package com.liamcoalstudio.aurora.window

import com.liamcoalstudio.aurora.*
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.reinterpret

actual class WindowHandle(val handle: COpaquePointer) {
    actual fun use() {
        glfwMakeContextCurrent(handle.reinterpret())
    }

    actual fun close() {
        glfwDestroyWindow(handle.reinterpret())
    }

    actual var shouldClose: Boolean
        get() = glfwWindowShouldClose(handle.reinterpret()) != 0
        set(value) { glfwSetWindowShouldClose(handle.reinterpret(), if(value) 1 else 0) }
}