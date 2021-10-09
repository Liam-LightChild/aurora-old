package com.liamcoalstudio.aurora.draw

import com.liamcoalstudio.aurora.*
import com.liamcoalstudio.aurora.buffer.BufferHandle
import com.liamcoalstudio.aurora.buffer.BufferType
import com.liamcoalstudio.aurora.shader.ShaderHandle
import kotlinx.cinterop.*
import platform.OpenGL3.*

actual class DrawCollection<T> actual constructor(
    actual val shader: ShaderHandle<T>,
    vertexBuffer: BufferHandle,
    actual val vertexCount: Int
) : Resource() {
    val vao: UInt

    init {
        val n = nativeHeap.alloc<UIntVar>()
        glGenVertexArrays(1, n.ptr)
        vao = n.value
        use()
        vertexBuffer.bind(BufferType.VertexBuffer)
    }

    actual override fun resourceUse() {
        glBindVertexArray(vao)
    }

    actual override fun resourceDelete() {
        val p = nativeHeap.alloc<UIntVar>()
        p.value = vao
        glDeleteVertexArrays(1, p.ptr)
        nativeHeap.free(p)
    }

    actual fun draw() {
        use()
        glDrawArrays(GL_TRIANGLES, 0, vertexCount)
    }
}
