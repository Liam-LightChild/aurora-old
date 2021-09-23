package com.liamcoalstudio.aurora

import kotlinx.cinterop.*

actual class InternalErrorException private constructor(message: String) : Exception(message) {
    companion object {
        internal inline fun glfw(): InternalErrorException {
            val desc = nativeHeap.alloc<CPointerVar<ByteVar>>()
            glfwGetError(desc.ptr)
            return InternalErrorException("GLFW error: ${desc.value?.toKString()}")
        }
    }
}