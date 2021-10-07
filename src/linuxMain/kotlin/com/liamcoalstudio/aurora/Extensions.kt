package com.liamcoalstudio.aurora

import kotlinx.cinterop.*

actual fun Float.toByteArray(): ByteArray {
    val bytes = nativeHeap.alloc<FloatVar>()
    bytes.value = this
    val b = bytes.ptr.readBytes(sizeOf<FloatVar>().convert())
    nativeHeap.free(bytes)
    return b
}
