package com.liamcoalstudio.aurora.draw

import com.liamcoalstudio.aurora.Resource
import com.liamcoalstudio.aurora.buffer.BufferHandle
import com.liamcoalstudio.aurora.shader.ShaderHandle

expect class DrawCollection<T>(
    shader: ShaderHandle<T>,
    vertexBuffer: BufferHandle,
    vertexCount: Int
) : Resource {
    val shader: ShaderHandle<T>
    val vertexCount: Int

    override fun resourceUse()
    override fun resourceDelete()

    fun draw()
}
