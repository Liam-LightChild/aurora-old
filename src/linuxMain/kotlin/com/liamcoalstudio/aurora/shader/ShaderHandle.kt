package com.liamcoalstudio.aurora.shader

import com.liamcoalstudio.aurora.*
import com.liamcoalstudio.aurora.buffer.Buffer
import com.liamcoalstudio.aurora.buffer.BufferHandle
import com.liamcoalstudio.aurora.buffer.BufferType
import com.liamcoalstudio.aurora.buffer.BufferUsage
import com.liamcoalstudio.aurora.draw.DrawCollection
import kotlinx.cinterop.*
import platform.posix.fflush
import platform.posix.fputs
import platform.posix.stderr

actual class ShaderHandle<T> internal actual constructor(internal val handle: UInt) : Resource() {
    actual companion object {
        actual fun <T> open() = ShaderHandle<T>(glCreateProgram!!())
    }

    internal val inputs = mutableListOf<ShaderInput<*, T>>()

    actual fun addShader(type: ShaderType, source: String) {
        val s = glCreateShader!!(type.native)

        source.cstr.getBytes().usePinned {
            val p = it.addressOf(0)
            val a = nativeHeap.allocArrayOf(p)
            glShaderSource!!(s, 1, a.reinterpret(), null)
        }
        glCompileShader!!(s)

        val log = ByteArray(8192)
        val length = nativeHeap.alloc<GLsizeiVar>()

        try {
            log.usePinned { logP ->
                glGetShaderInfoLog!!(s, 8192, length.ptr, logP.addressOf(0))
            }

            if(length.value > 0) {
                fputs("While adding $type to shader:\n" + log.toKString(), stderr)
            }
        } finally {
            nativeHeap.free(length)
        }

        val status = nativeHeap.alloc<IntVar>()

        try {
            glGetShaderiv!!(s, GL_COMPILE_STATUS.convert(), status.ptr)
            if(status.value != 1) {
                fflush(stderr)
                throw IllegalStateException("Shader compilation failed.")
            }
        } finally {
            nativeHeap.free(status)
        }

        glAttachShader!!(handle, s)
        glDeleteShader!!(s)
    }

    actual fun finish() {
        glLinkProgram!!(handle)

        val log = ByteArray(8192)
        val length = nativeHeap.alloc<GLsizeiVar>()

        try {
            log.usePinned { logP ->
                glGetProgramInfoLog!!(handle, 8192, length.ptr, logP.addressOf(0))
            }

            if(length.value > 0) {
                fputs("While linking shader:\n" + log.toKString(), stderr)
            }
        } finally {
            nativeHeap.free(length)
        }

        val status = nativeHeap.alloc<IntVar>()

        try {
            glGetProgramiv!!(handle, GL_LINK_STATUS.convert(), status.ptr)
            if(status.value != 1) {
                fflush(stderr)
                throw IllegalStateException("Shader linking failed.")
            }
        } finally {
            nativeHeap.free(status)
        }
    }

    actual override fun resourceUse() {
        glUseProgram!!(handle)
    }

    actual override fun resourceDelete() {
        glDeleteProgram!!(handle)
    }

    actual fun addInput(input: ShaderInput<*, T>) {
        inputs.add(input)
    }

    actual fun constructDrawCollection(vararg values: T): DrawCollection<T> {
        val vertexBuffer = BufferHandle.new(BufferType.VertexBuffer, BufferUsage.DrawStatic)

        val buffer = Buffer.build {
            for(v in values) {
                for(i in inputs) {
                    val o = i.p(v)!!
                    val b = i.inputType.encode(o)
                    bytes(b)
                }
            }
        }

        val c = DrawCollection(
            this,
            vertexBuffer,
            values.size
        ).also {
            it.onDelete { vertexBuffer.delete() }
            it.use()
        }

        buffer.push(vertexBuffer)
        vertexBuffer.bind(BufferType.VertexBuffer)
        var index = 0L
        val size = inputs.sumOf { it.inputType.size }
        for((name, type, _) in inputs) {
            val l = glGetAttribLocation!!(handle, name.cstr.getPointer(Arena()))
            val (s, t) = when (type) {
                ShaderInputType.Byte -> 1 to GL_BYTE
                ShaderInputType.Double -> 1 to GL_DOUBLE
                ShaderInputType.Float -> 1 to GL_FLOAT
                ShaderInputType.Int -> 1 to GL_INT
                ShaderInputType.UInt -> 1 to GL_UNSIGNED_INT
                ShaderInputType.Vector2d ->  2 to GL_DOUBLE
                ShaderInputType.Vector2f ->  2 to GL_FLOAT
                ShaderInputType.Vector2i ->  2 to GL_INT
                ShaderInputType.Vector2ui -> 2 to GL_UNSIGNED_INT
                ShaderInputType.Vector3d ->  3 to GL_DOUBLE
                ShaderInputType.Vector3f ->  3 to GL_FLOAT
                ShaderInputType.Vector3i ->  3 to GL_INT
                ShaderInputType.Vector3ui -> 3 to GL_UNSIGNED_INT
                ShaderInputType.Vector4d ->  4 to GL_DOUBLE
                ShaderInputType.Vector4f ->  4 to GL_FLOAT
                ShaderInputType.Vector4i ->  4 to GL_INT
                ShaderInputType.Vector4ui -> 4 to GL_UNSIGNED_INT
                else -> throw IllegalArgumentException("Invalid shader type $type")
            }
            glVertexAttribPointer!!(l.convert(), s, t.convert(), 0u, size, index.toCPointer())
            glEnableVertexAttribArray!!(l.convert())
            index += type.size
        }

        return c
    }
}

actual enum class ShaderType(val native: GLenum) {
    VERTEX(GL_VERTEX_SHADER.convert()),
    FRAGMENT(GL_FRAGMENT_SHADER.convert())
}

