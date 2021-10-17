package com.liamcoalstudio.aurora.draw

expect class Framebuffer(width: Int, height: Int) : DrawTarget {
    override fun unUse()
    override fun resourceUse()
    override fun resourceDelete()
}
