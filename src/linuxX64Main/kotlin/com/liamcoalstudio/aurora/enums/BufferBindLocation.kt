package com.liamcoalstudio.aurora.enums

import com.liamcoalstudio.aurora.GL_ARRAY_BUFFER
import com.liamcoalstudio.aurora.GL_ELEMENT_ARRAY_BUFFER
import kotlinx.cinterop.convert

actual enum class BufferBindLocation(val value: UInt) {
    ArrayBuffer(GL_ARRAY_BUFFER.convert()),
    ElementArrayBuffer(GL_ELEMENT_ARRAY_BUFFER.convert())
}
