package com.liamcoalstudio.aurora

import com.liamcoalstudio.aurora.enums.Key

object Keyboard {
    internal var keysJustDown = mutableListOf<Key>()
        private set
    internal var keysJustUp = mutableListOf<Key>()
        private set
    internal var keysDown = mutableListOf<Key>()
        private set

    fun press(key: Key) {
        keysJustDown = keysJustDown.plus(key).distinct().toMutableList()
        keysDown = keysDown.plus(key).distinct().toMutableList()
    }

    fun release(key: Key) {
        keysJustUp = keysJustUp.plus(key).distinct().toMutableList()
        keysDown = keysDown.minus(key).toMutableList()
    }

    fun reset() {
        keysJustUp.clear()
        keysJustDown.clear()
    }
}