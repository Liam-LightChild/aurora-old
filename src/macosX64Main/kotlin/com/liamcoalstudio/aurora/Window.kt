package com.liamcoalstudio.aurora

import kotlinx.cinterop.*

actual class Window private constructor(val ptr: COpaquePointer) {
    actual fun close() {
        glfwDestroyWindow(ptr.reinterpret())
    }

    actual fun activate() {
        glfwMakeContextCurrent(ptr.reinterpret())
    }

    actual inline fun <T> with(crossinline f: Window.() -> T) {
        activate()
        this.f()
    }

    actual companion object {
        actual fun windowed(
            title: String,
            width: Int,
            height: Int,
            share: Window?
        ): Window {
            val x = nativeHeap.alloc<FloatVar>()
            val y = nativeHeap.alloc<FloatVar>()
            glfwGetMonitorContentScale(glfwGetPrimaryMonitor(), x.ptr, y.ptr)

            glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_TRUE)
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
            return Window(glfwCreateWindow(width, height, title, null, share?.ptr?.reinterpret()) ?: throw IllegalStateException("Failed to open window $title"))
        }

        actual fun fullscreen(
            title: String,
            share: Window?
        ): Window {
            val v = glfwGetVideoMode(glfwGetPrimaryMonitor())!!.pointed
            glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_TRUE)
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
            return Window(glfwCreateWindow(v.width, v.height, title, glfwGetPrimaryMonitor(), share?.ptr?.reinterpret()) ?: throw IllegalStateException("Failed to open window $title"))
        }
    }

}