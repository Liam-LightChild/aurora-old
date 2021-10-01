package com.liamcoalstudio.aurora

enum class BufferUsage {
    Draw,
    DrawStream,
    DrawStatic,

    Data,
    DataStream,
    DataStatic,

    Copy,
    CopyStream,
    CopyStatic
}

expect class BufferHandle {
    companion object {
        fun new(type: BufferType, usage: BufferUsage): BufferHandle
    }

    fun delete()
    fun bind(type: BufferType = this.type)
    internal fun push(data: ByteArray)

    val type: BufferType
    val usage: BufferUsage
}
