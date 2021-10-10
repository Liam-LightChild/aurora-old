package com.liamcoalstudio.aurora.texture

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val width: Int,
    val height: Int,
    val wrapping: WrappingType,
    val linearInterp: Boolean,
    val colors: List<Color>
) {
    companion object Presets {
        val checkerboard get() = Image(
            width = 2, height = 2,
            wrapping = WrappingType.Repeat,
            linearInterp = false,
            colors = listOf(
                Color.white,
                Color.black,
                Color.black,
                Color.white
            )
        )
    }

    fun asTexture() = Texture.open(this)
}