package com.liamcoalstudio.aurora.buffer

import kotlinx.cinterop.*
import platform.OpenGL3.*
import platform.OpenGLCommon.GLenum

internal val BufferType.native: GLenum get() = when(this) {
    BufferType.VertexBuffer -> GL_ARRAY_BUFFER
    BufferType.VertexIndexerBuffer -> GL_ELEMENT_ARRAY_BUFFER
}.convert()

internal val BufferUsage.native: GLenum get() = when(this) {
    BufferUsage.Draw -> GL_DYNAMIC_DRAW
    BufferUsage.DrawStream -> GL_STREAM_DRAW
    BufferUsage.DrawStatic -> GL_STATIC_DRAW
    BufferUsage.Data -> GL_DYNAMIC_READ
    BufferUsage.DataStream -> GL_STREAM_READ
    BufferUsage.DataStatic -> GL_STATIC_READ
    BufferUsage.Copy -> GL_DYNAMIC_COPY
    BufferUsage.CopyStream -> GL_STREAM_COPY
    BufferUsage.CopyStatic -> GL_STATIC_COPY
}.convert()

actual class BufferHandle private constructor(
    private val handle: UInt,
    actual val type: BufferType,
    actual val usage: BufferUsage
) {
    actual companion object {
        actual fun new(
            type: BufferType,
            usage: BufferUsage
        ): BufferHandle {
            val id = nativeHeap.alloc<UIntVar>()

            try {
                glGenBuffers(1, id.ptr)
                return com.liamcoalstudio.aurora.buffer.BufferHandle(id.value, type, usage)
            } finally {
                nativeHeap.free(id)
            }
        }
    }

    actual fun delete() {
        val id = nativeHeap.alloc<UIntVar>()

        try {
            id.value = handle
            glDeleteBuffers(1, id.ptr)
        } finally {
            nativeHeap.free(id)
        }
    }

    actual fun bind(type: BufferType) {
        glBindBuffer(type.native, handle)
    }

    internal actual fun push(data: ByteArray) {
        bind(type)

        data.usePinned { pinned ->
            glBufferData(type.native, data.size.convert(), pinned.addressOf(0), usage.native)
        }
    }
}