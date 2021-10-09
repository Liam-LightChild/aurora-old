package com.liamcoalstudio.aurora.texture

import com.liamcoalstudio.aurora.IndexBoundResource
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

@Serializable
enum class WrappingType {
    Repeat,
    RepeatMirrored,
    ClampEdge,
    @Deprecated("Unimplemented") // TODO
    ClampBorder
}

expect class Texture : IndexBoundResource {
    companion object {
        fun open(image: Image): Texture
    }

    override fun resourceUse(i: Int)
    override fun resourceDelete()
}
