package com.liamcoalstudio.aurora

import com.liamcoalstudio.aurora.enums.ShaderStage
import kotlinx.cinterop.*
import platform.OpenGL3.*
import platform.OpenGLCommon.GLint
import platform.OpenGLCommon.GLintVar
import platform.OpenGLCommon.GLsizeiVar
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fputs

actual class Shader internal actual constructor(
    actual val id: UInt,
    actual val stage: ShaderStage
) : Resource() {
    actual constructor(stage: ShaderStage, source: String) : this(glCreateShader(stage.native), stage) {
        pushSource(source)
    }

    actual override fun delete() {
        glDeleteShader(id)
    }

    actual fun pushSource(src: String): String? {
        val sp = nativeHeap.allocArrayOf(src.encodeToByteArray() + 0)
        val array = nativeHeap.allocArrayOf(sp)
        glShaderSource(id, 1, array, null)
        glCompileShader(id)

        val log = nativeHeap.allocArray<ByteVar>(8192)
        val length = nativeHeap.alloc<GLsizeiVar>()
        glGetShaderInfoLog(id, 8192, length.ptr, log)

        val logStr = if(length.value > 0) log.toKString() else null

        val status = nativeHeap.alloc<GLintVar>()
        glGetShaderiv(id, GL_COMPILE_STATUS, status.ptr)

        if(status.value == 0) {
            if(logStr != null) {
                val f = fopen(".shader_c_$stage.log", "w")
                fputs(logStr, f)
                fclose(f)
            }

            throw IllegalStateException("Compilation failed; see .shader_c_$stage.log, if it exists")
        }

        return logStr
    }

    actual override val isValid: Boolean
        get() = glIsShader(id) != 0.toUByte()

    @Deprecated("Not bindable.", level = DeprecationLevel.HIDDEN)
    actual override fun bind() {
        throw IllegalStateException("cannot bind singular shader; only Programs can be bound")
    }
}