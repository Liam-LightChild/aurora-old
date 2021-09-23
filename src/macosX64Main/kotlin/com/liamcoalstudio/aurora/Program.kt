package com.liamcoalstudio.aurora

import kotlinx.cinterop.*
import platform.OpenGL3.*
import platform.OpenGLCommon.GLsizeiVar
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fputs

actual class Program actual constructor(builder: ProgramBuilder.() -> Unit) {
    private val info = ProgramBuilder().also(builder)

    val shaders by info::shaders
    val id: UInt

    init {
        id = glCreateProgram()
        for(s in shaders) glAttachShader(id, s.id)
        glLinkProgram(id)

        val log = nativeHeap.allocArray<ByteVar>(8192)
        val length = nativeHeap.alloc<GLsizeiVar>()
        glGetProgramInfoLog(id, 8192, length.ptr, log)

        val logStr = if(length.value > 0) log.toKString() else null

        val status = nativeHeap.alloc<IntVar>()
        glGetProgramiv(id, GL_LINK_STATUS, status.ptr)

        if(status.value == 0) {
            if(logStr != null) {
                val f = fopen(".program_link.log", "w")
                fputs(logStr, f)
                fclose(f)
            }

            throw IllegalStateException("Compilation failed; see .program_link.log, if it exists")
        }
    }

    actual fun use() {

    }

    actual fun delete() {
    }

    actual inline fun with(crossinline f: () -> Unit) {
    }

}