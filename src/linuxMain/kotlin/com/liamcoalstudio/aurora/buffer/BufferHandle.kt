package com.liamcoalstudio.aurora.buffer

import com.liamcoalstudio.aurora.*
import com.liamcoalstudio.aurora.buffer.BufferType.*
import com.liamcoalstudio.aurora.buffer.BufferUsage.*
import kotlinx.cinterop.*

internal val BufferType.native: GLenum
    get() = when(this) {
    VertexBuffer -> GL_ARRAY_BUFFER
    VertexIndexerBuffer -> GL_ELEMENT_ARRAY_BUFFER
}.convert()

internal val BufferUsage.native: GLenum
    get() = when(this) {
    Draw -> GL_DYNAMIC_DRAW
    DrawStream -> GL_STREAM_DRAW
    DrawStatic -> GL_STATIC_DRAW
    Data -> GL_DYNAMIC_READ
    DataStream -> GL_STREAM_READ
    DataStatic -> GL_STATIC_READ
    Copy -> GL_DYNAMIC_COPY
    CopyStream -> GL_STREAM_COPY
    CopyStatic -> GL_STATIC_COPY
}.convert()

actual class BufferHandle private constructor(private val handle: UInt, actual val type: BufferType, actual val usage: BufferUsage) : Resource() {
    actual companion object {
        actual fun new(type: BufferType, usage: BufferUsage): BufferHandle {
            val id = nativeHeap.alloc<UIntVar>()
            glGenBuffers!!(1, id.ptr)
            val i = id.value
            nativeHeap.free(id)

            return BufferHandle(i, type, usage).also { it.bind(type) }
        }
    }

    actual override fun resourceUse() {
        glBindBuffer!!(type.native, handle)
    }

    actual override fun resourceDelete() {
        val id = nativeHeap.alloc<UIntVar>()
        id.value = handle
        glDeleteBuffers!!(1, id.ptr)
        nativeHeap.free(id)
    }

    actual fun bind(type: BufferType) {
        glBindBuffer!!(type.native, handle)
    }

    internal actual fun push(data: ByteArray) {
        data.usePinned { pinned ->
            glNamedBufferData?.invoke(handle, data.size.convert(), pinned.addressOf(0), usage.native)
                ?: run {
                    bind(type)
                    glBufferData!!(type.native, data.size.convert(), pinned.addressOf(0), usage.native)
                }
        }
    }
}