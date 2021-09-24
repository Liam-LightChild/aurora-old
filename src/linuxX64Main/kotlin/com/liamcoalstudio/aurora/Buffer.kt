package com.liamcoalstudio.aurora

import kotlinx.cinterop.*

actual enum class BufferMutability(val draw: Int, val copy: Int, val read: Int) {
    Static(GL_STATIC_DRAW, GL_STATIC_COPY, GL_STATIC_READ),
    Dynamic(GL_DYNAMIC_DRAW, GL_DYNAMIC_COPY, GL_DYNAMIC_READ),
    Stream(GL_STREAM_DRAW, GL_STREAM_COPY, GL_STREAM_READ)
}

actual enum class BufferType(val native: GLenum) {
    VertexArrays(GL_ARRAY_BUFFER.convert()),
    ElementArrays(GL_ELEMENT_ARRAY_BUFFER.convert())
}

actual enum class BufferUsage {
    Draw,
    Copy,
    Read
}

actual class Buffer actual constructor(
    actual val type: BufferType,
    actual val mutability: BufferMutability,
    actual val usage: BufferUsage,
) : Resource() {
    actual val bytes: MutableList<Byte> = mutableListOf()

    val native: UInt
    val nativeUsage = when(mutability) {
        BufferMutability.Static -> when(usage) {
            BufferUsage.Draw -> BufferMutability.Static.draw
            BufferUsage.Copy -> BufferMutability.Static.copy
            BufferUsage.Read -> BufferMutability.Static.read
        }
        BufferMutability.Dynamic -> when(usage) {
            BufferUsage.Draw -> BufferMutability.Dynamic.draw
            BufferUsage.Copy -> BufferMutability.Dynamic.copy
            BufferUsage.Read -> BufferMutability.Dynamic.read
        }
        BufferMutability.Stream -> when(usage) {
            BufferUsage.Draw -> BufferMutability.Stream.draw
            BufferUsage.Copy -> BufferMutability.Stream.copy
            BufferUsage.Read -> BufferMutability.Stream.read
        }
    }.convert<GLenum>()

    private var buffer: COpaquePointer? = null

    init {
        val n = nativeHeap.alloc<UIntVar>()
        glGenBuffers!!(1, n.ptr)
        native = n.value
        nativeHeap.free(n.rawPtr)
    }

    actual fun push() {
        if(buffer != null) nativeHeap.free(buffer!!)

        buffer = nativeHeap.allocArrayOf(bytes.toByteArray())
        if(glNamedBufferData != null) {
            glNamedBufferData!!(native, bytes.size.convert(), buffer, nativeUsage)
        } else {
            bind()
            glBufferData!!(type.native, bytes.size.convert(), buffer, nativeUsage)
        }
    }

    actual fun put(value: ByteArray) { bytes.addAll(value.toTypedArray()) }
    actual fun put(value: Collection<Byte>) { bytes.addAll(value) }

    actual fun put(value: Int) {
        put(run {
            val v = nativeHeap.alloc<IntVar> { this.value = value }
            val b = v.ptr.readBytes(sizeOf<IntVar>().toInt())
            nativeHeap.free(v.rawPtr)
            b
        })
    }

    actual fun put(value: Float) {
        put(run {
            val v = nativeHeap.alloc<FloatVar> { this.value = value }
            val b = v.ptr.readBytes(sizeOf<FloatVar>().toInt())
            nativeHeap.free(v.rawPtr)
            b
        })
    }

    actual fun put(value: Double) {
        put(run {
            val v = nativeHeap.alloc<DoubleVar> { this.value = value }
            val b = v.ptr.readBytes(sizeOf<DoubleVar>().toInt())
            nativeHeap.free(v.rawPtr)
            b
        })
    }

    actual fun put(value: Byte) = put(byteArrayOf(value))

    override fun delete() {
        val p = nativeHeap.alloc<UIntVar>()
        p.value = native
        glDeleteBuffers!!(1, p.ptr)
    }

    override fun bind() {
        glBindBuffer!!(type.native, native)
    }

    actual var value: ByteArray
        get() {
            val s = nativeHeap.alloc<GLintVar>()
            return if(glGetNamedBufferSubData != null) {
                glGetNamedBufferParameteriv!!(native, GL_BUFFER_SIZE.convert(), s.ptr)
                val ptr = nativeHeap.allocArray<ByteVar>(s.value)
                glGetNamedBufferSubData!!(native, 0, s.value.convert(), ptr)
                ptr.readBytes(s.value)
            } else {
                bind()
                glGetBufferParameteriv!!(native, GL_BUFFER_SIZE.convert(), s.ptr)
                val ptr = nativeHeap.allocArray<ByteVar>(s.value)
                glGetNamedBufferSubData!!(native, 0, s.value.convert(), ptr)
                ptr.readBytes(s.value)
            }
        }
        set(value) {
            val p = nativeHeap.allocArrayOf(value)
            if(glNamedBufferData != null)
                glNamedBufferData!!(native, value.size.convert(), p, nativeUsage)
            else {
                bind()
                glBufferData!!(type.native, value.size.convert(), p, nativeUsage)
            }
        }

    actual override val isValid: Boolean
        get() = glIsBuffer!!(native) > 0u
}
