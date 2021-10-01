package com.liamcoalstudio.aurora

sealed class WindowSpan {
    class Windowed(val width: Int, val height: Int) : WindowSpan() {
        override fun toString(): String {
            return "Windowed(width=$width, height=$height)"
        }
    }
    object Fullscreen : WindowSpan()
    object Invisible : WindowSpan()
}