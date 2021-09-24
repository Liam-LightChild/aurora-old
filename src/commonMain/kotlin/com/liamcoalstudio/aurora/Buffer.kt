package com.liamcoalstudio.aurora

expect enum class BufferType {
    VertexArrays,
    ElementArrays
}

expect enum class BufferMutability {
    Static,
    Dynamic,
    Stream
}

expect enum class BufferUsage {
    Draw,
    Copy,
    @Deprecated("Cannot Read") Read
}

expect class Buffer(type: BufferType, mutability: BufferMutability, usage: BufferUsage) : Resource {
    val type: BufferType
    val mutability: BufferMutability
    val usage: BufferUsage

    val bytes: MutableList<Byte>

    fun push()
    fun put(value: ByteArray)
    fun put(value: Collection<Byte>)
    fun put(value: Int)
    fun put(value: Float)
    fun put(value: Double)
    fun put(value: Byte)

    var value: ByteArray
    @Deprecated("Using isValid on Buffer objects does *not* always return accurate results")
    override val isValid: Boolean
}
