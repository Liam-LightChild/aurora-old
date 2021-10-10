package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.texture.Image
import com.liamcoalstudio.aurora.texture.Texture

class TextureRef(val image: Image) : Reference<Texture> {
    private var texture: Texture? = null

    override fun get() = texture ?: throw IllegalStateException("texture is $texture")
    override fun fulfill(t: Texture) { if(texture == null) texture = t }
}
