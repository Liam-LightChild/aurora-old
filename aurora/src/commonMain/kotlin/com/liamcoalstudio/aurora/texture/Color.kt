package com.liamcoalstudio.aurora.texture

import kotlinx.serialization.Serializable

@Serializable
data class Color(
    val r: Float,
    val g: Float,
    val b: Float
) {
    companion object Presets {
        inline val white get() = Color(1f, 1f, 1f)
        inline val black get() = Color(0f, 0f, 0f)
    }
}