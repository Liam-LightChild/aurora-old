package com.liamcoalstudio.aurora.game

import kotlin.reflect.KProperty

sealed interface Reference<T> {
    fun get(): T
    fun fulfill(t: T)

    operator fun getValue(t: Any?, p: KProperty<*>) = get()
}

inline fun <T> Reference<T>.with(f: (T) -> Unit) = f(get())
