package com.liamcoalstudio.aurora.texture

import kotlinx.serialization.Serializable

@Serializable
enum class WrappingType {
    Repeat,
    RepeatMirrored,
    ClampEdge,
    @Deprecated("Unimplemented") // TODO
    ClampBorder
}