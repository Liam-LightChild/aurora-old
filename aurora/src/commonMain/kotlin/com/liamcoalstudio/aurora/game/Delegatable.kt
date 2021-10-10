package com.liamcoalstudio.aurora.game

import kotlin.reflect.KProperty

interface Delegatable<T> {
    operator fun getValue(t: Any?, p: KProperty<*>): T
}