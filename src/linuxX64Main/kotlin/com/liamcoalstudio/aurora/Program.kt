package com.liamcoalstudio.aurora

import kotlinx.cinterop.*
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fwrite

actual class Program actual constructor(builder: ProgramBuilder.() -> Unit) {
    val id: UInt = glCreateProgram!!()

    private val info: ProgramBuilder

    init {
        val p = ProgramBuilder().apply(builder)
        info = p
        for(s in p.shaders) glAttachShader!!(id, s.id)
        for((o, c) in p.outputs) glBindFragDataLocation!!(id, c.convert(), nativeHeap.allocArrayOf(o.encodeToByteArray()))

        glLinkProgram!!(id)

        val len = nativeHeap.alloc<GLsizeiVar>()
        val str = nativeHeap.allocArray<GLcharVar>(8192)
        glGetProgramInfoLog!!(id, 8192, len.ptr, str)

        val success = nativeHeap.alloc<GLintVar>()
        glGetProgramiv!!(id, GL_LINK_STATUS.convert(), success.ptr)

        if(len.value > 0) {
            val f = fopen(".program_link.log", "w")
            fwrite(str, len.value.convert(), 1, f)
            fclose(f)
        }

        if(success.value == 0) {
            throw IllegalStateException("Program failed to link; log saved to ./.program_link.log")
        }
    }

    actual fun use() = glUseProgram!!(id)

    actual fun delete() {
        glDeleteProgram!!(id)
        info.shaders.forEach(Shader::delete)
    }

    actual inline fun with(crossinline f: () -> Unit) {
        use()
        f()
    }
}