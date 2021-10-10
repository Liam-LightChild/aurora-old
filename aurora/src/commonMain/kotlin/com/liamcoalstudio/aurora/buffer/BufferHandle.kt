package com.liamcoalstudio.aurora.buffer

import com.liamcoalstudio.aurora.Resource

expect class BufferHandle : Resource {
    companion object {
        fun new(type: BufferType, usage: BufferUsage): BufferHandle
    }

    override fun resourceUse()
    override fun resourceDelete()
    fun bind(type: BufferType = this.type)
    internal fun push(data: ByteArray)

    val type: BufferType
    val usage: BufferUsage
}
