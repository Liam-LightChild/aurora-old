package com.liamcoalstudio.aurora.input

import com.liamcoalstudio.aurora.window.WindowHandle

class KeyHandlers {
    internal val handlers = mutableMapOf<Key, () -> Unit>()

    fun on(key: Key, f: () -> Unit) {
        handlers[key] = f
    }

    internal fun push(key: Key) {
        handlers[key]?.invoke()
    }
}

expect fun WindowHandle.isKeyDown(key: Key): Boolean
expect fun WindowHandle.isKeyUp(key: Key): Boolean
expect fun WindowHandle.setupKeyHandlers(h: KeyHandlers.() -> Unit)
