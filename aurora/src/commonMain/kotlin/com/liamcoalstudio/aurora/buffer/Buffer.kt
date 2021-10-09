package com.liamcoalstudio.aurora.buffer

import com.liamcoalstudio.aurora.toByteArray

class Buffer private constructor() {
    private val bytes: MutableList<Byte> = mutableListOf()

    val byteArray get() = bytes.toByteArray()

    class BufferBuilder internal constructor(val buffer: Buffer) {
        inline fun byte(b: Byte) = buffer.writeByte(b)
        inline fun short(s: Short) = buffer.writeShort(s)
        inline fun int(i: Int) = buffer.writeInt(i)
        inline fun float(f: Float) = buffer.writeFloat(f)
        inline fun bytes(a: ByteArray) = buffer.write(a)
        inline fun shorts(a: ShortArray) = a.forEach(buffer::writeShort)
        inline fun ints(a: IntArray) = a.forEach(buffer::writeInt)
        inline fun floats(a: FloatArray) = a.forEach(buffer::writeFloat)
    }

    companion object {
        fun build(f: BufferBuilder.() -> Unit) = BufferBuilder(Buffer()).also(f).buffer

        val empty get() = build {  }
    }

    fun write(a: ByteArray) {
        bytes.addAll(a.toTypedArray())
    }

    fun write(vararg bytes: Byte) {
        this.bytes.addAll(bytes.toTypedArray())
    }

    inline fun writeByte(b: Byte) = write(b)
    inline fun writeShort(s: Short) = write(s.toByteArray())
    inline fun writeInt(i: Int) = write(i.toByteArray())
    inline fun writeFloat(f: Float) = write(f.toByteArray())

    fun push(bufferHandle: BufferHandle) = bufferHandle.push(bytes.toByteArray())
}
