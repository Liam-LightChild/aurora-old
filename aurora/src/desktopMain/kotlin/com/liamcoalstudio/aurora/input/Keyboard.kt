package com.liamcoalstudio.aurora.input

import com.liamcoalstudio.aurora.GLFW_PRESS
import com.liamcoalstudio.aurora.GLFW_RELEASE
import com.liamcoalstudio.aurora.glfwGetKey
import com.liamcoalstudio.aurora.glfwSetKeyCallback
import com.liamcoalstudio.aurora.window.WindowHandle
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.staticCFunction

actual fun WindowHandle.isKeyDown(key: Key): Boolean
    = glfwGetKey(handle.reinterpret(), key.native) == GLFW_PRESS

actual fun WindowHandle.isKeyUp(key: Key): Boolean
    = glfwGetKey(handle.reinterpret(), key.native) == GLFW_RELEASE

private val windowHandlers = mutableMapOf<COpaquePointer, KeyHandlers>()

private inline var WindowHandle.keyHandlers
    get() = windowHandlers[handle]!!
    set(value) { windowHandlers[handle] = value }

private val keyCache = mutableMapOf<Int, Key>()

actual fun WindowHandle.setupKeyHandlers(h: KeyHandlers.() -> Unit) {
    keyHandlers = KeyHandlers().also(h)
    glfwSetKeyCallback(handle.reinterpret(), staticCFunction { window: COpaquePointer, key: Int, _: Int, _: Int, _: Int ->
        try {
            windowHandlers[window]!!.push(
                if(keyCache.containsKey(key)) keyCache[key]!!
                else Key.values().single { it.native == key }.also { keyCache[key] = it }
            )
        } catch (e: Throwable) {
            println("""
                --------------------------------------------
                EXCEPTION ENCOUNTERED DURING C FUNCTION CALL
                KeyHandlers::push(Key (fromNative) $key)
                --------------------------------------------
                Aurora has encountered an error and will
                continue from it. You are seeing this because
                Kotlin does not handle exceptions in
                staticCFunction blocks very well.
                --------------------------------------------
                STACKTRACE BELOW
                --------------------------------------------
            """.trimIndent())
            e.printStackTrace()
        }
    }.reinterpret())
}
