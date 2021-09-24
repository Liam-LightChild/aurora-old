package com.liamcoalstudio.aurora

import com.liamcoalstudio.aurora.enums.ShaderStage
import com.liamcoalstudio.aurora.enums.ShaderStage.FRAGMENT
import com.liamcoalstudio.aurora.enums.ShaderStage.VERTEX
import kotlinx.cinterop.*

val ShaderStage.native
    get() = when(this) {
        VERTEX -> GL_VERTEX_SHADER
        FRAGMENT -> GL_FRAGMENT_SHADER
    }.convert<GLenum>()

fun glfwError(): InternalErrorException {
    val glfw = nativeHeap.alloc<CPointerVar<CPointerVar<ByteVar>>>()
    glfwGetError(glfw.value)
    return InternalErrorException(glfw.pointed!!.value!!.toKString())
}
