package com.liamcoalstudio.aurora.texture

import com.liamcoalstudio.aurora.*
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.invoke
import kotlinx.cinterop.usePinned

actual class Texture(internal val handle: UInt) : IndexBoundResource() {
    actual companion object {
        @Suppress("DEPRECATION")
        @OptIn(ExperimentalUnsignedTypes::class)
        actual fun open(image: Image): Texture {
            val floats = image.colors.flatMap { listOf(it.r, it.g, it.b) }.toFloatArray()
            val id = UIntArray(1)

            id.usePinned { glGenTextures(1, it.addressOf(0)) }
            glBindTexture(GL_TEXTURE_2D, id[0])

            val wrap = when(image.wrapping) {
                WrappingType.Repeat -> GL_REPEAT
                WrappingType.RepeatMirrored -> GL_MIRRORED_REPEAT
                WrappingType.ClampEdge -> GL_CLAMP_TO_EDGE
                WrappingType.ClampBorder -> GL_CLAMP_TO_BORDER
            }

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap)

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, if(image.linearInterp) GL_LINEAR_MIPMAP_LINEAR else GL_NEAREST_MIPMAP_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, if(image.linearInterp) GL_LINEAR else GL_NEAREST)

            floats.usePinned { pin ->
                glTexImage2D(
                    target = GL_TEXTURE_2D,
                    width = image.width,
                    height = image.height,
                    internalformat = GL_RGB,
                    format = GL_RGB,
                    border = 0,
                    level = 0,
                    type = GL_FLOAT,
                    pixels = pin.addressOf(0)
                )
            }

            glGenerateMipmap!!(GL_TEXTURE_2D.convert())

            return Texture(id[0])
        }
    }

    actual override fun resourceUse(i: Int) {
        glActiveTexture!!((GL_TEXTURE0 + i).convert())
        glBindTexture(GL_TEXTURE_2D, handle)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    actual override fun resourceDelete() {
        uintArrayOf(handle).usePinned { glDeleteTextures(1, it.addressOf(0)) }
    }
}