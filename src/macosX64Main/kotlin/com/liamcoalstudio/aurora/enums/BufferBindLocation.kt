package com.liamcoalstudio.aurora.enums

import platform.OpenGL3.*
import platform.OpenGLCommon.GLenum

actual enum class BufferBindLocation(val native: Int) {
    ArrayBuffer(GL_ARRAY_BUFFER),
    ElementArrayBuffer(GL_ELEMENT_ARRAY_BUFFER)
}
