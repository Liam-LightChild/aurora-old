package com.liamcoalstudio.aurora.texture

import com.liamcoalstudio.aurora.IndexBoundResource

expect class Texture : IndexBoundResource {
    companion object {
        fun open(image: Image): Texture
    }

    override fun resourceUse(i: Int)
    override fun resourceDelete()
}
