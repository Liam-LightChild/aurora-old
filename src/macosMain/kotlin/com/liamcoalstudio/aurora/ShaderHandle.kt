package com.liamcoalstudio.aurora

import kotlinx.cinterop.*
import platform.OpenGL3.*
import platform.OpenGLCommon.*
import platform.posix.fflush
import platform.posix.fputs
import platform.posix.stderr

actual enum class ShaderType(val native: GLenum) {
    VERTEX(GL_VERTEX_SHADER.convert()),
    FRAGMENT(GL_FRAGMENT_SHADER.convert())
}

actual value class ShaderHandle actual constructor(actual val handle: UInt) {
    actual companion object {
        actual fun open() = ShaderHandle(glCreateProgram())
    }

    actual fun addShader(type: ShaderType, source: String) {
        val s = glCreateShader(type.native)

        source.cstr.getBytes().usePinned {
            val p = it.addressOf(0)
            val a = nativeHeap.allocArrayOf(p)
            glShaderSource(s, 1, a.reinterpret(), null)
        }
        glCompileShader(s)

        val log = ByteArray(8192)
        val length = nativeHeap.alloc<GLsizeiVar>()

        try {
            log.usePinned { logP ->
                glGetShaderInfoLog(s, 8192, length.ptr, logP.addressOf(0))
            }

            if(length.value > 0) {
                fputs("While adding $type to shader:\n" + log.toKString(), stderr)
            }
        } finally {
            nativeHeap.free(length)
        }

        val status = nativeHeap.alloc<IntVar>()

        try {
            glGetShaderiv(s, GL_COMPILE_STATUS.convert(), status.ptr)
            if(status.value != 1) {
                fflush(stderr)
                throw IllegalStateException("Shader compilation failed.")
            }
        } finally {
            nativeHeap.free(status)
        }

        glAttachShader(handle, s)
        glDeleteShader(s)
    }

    actual fun finish() {
        glLinkProgram(handle)

        val log = ByteArray(8192)
        val length = nativeHeap.alloc<GLsizeiVar>()

        try {
            log.usePinned { logP ->
                glGetProgramInfoLog(handle, 8192, length.ptr, logP.addressOf(0))
            }

            if(length.value > 0) {
                fputs("While linking shader:\n" + log.toKString(), stderr)
            }
        } finally {
            nativeHeap.free(length)
        }

        val status = nativeHeap.alloc<IntVar>()

        try {
            glGetProgramiv(handle, GL_LINK_STATUS.convert(), status.ptr)
            if(status.value != 1) {
                fflush(stderr)
                throw IllegalStateException("Shader linking failed.")
            }
        } finally {
            nativeHeap.free(status)
        }
    }

    actual fun use() {
        glUseProgram(handle)
    }

    actual fun delete() {
        glDeleteProgram(handle)
    }
}
