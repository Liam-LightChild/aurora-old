package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.window.WindowHandle
import com.liamcoalstudio.aurora.window.WindowOpenBuilder
import com.liamcoalstudio.aurora.window.openWindow
import kotlin.reflect.KProperty

class CreatingWindow internal constructor(private val f: WindowOpenBuilder.() -> Unit) {
    operator fun getValue(t: Any?, p: KProperty<*>): WindowHandle = build()

    internal inline fun build(): WindowHandle {
        return openWindow(f)
    }
}