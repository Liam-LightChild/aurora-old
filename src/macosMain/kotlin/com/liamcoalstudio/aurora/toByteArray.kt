package com.liamcoalstudio.aurora

import kotlinx.cinterop.*

actual fun Float.toByteArray(): ByteArray {
    this.usePinned {
        val p = nativeHeap.alloc<IntVar>()
        return try {
            it.copy(p.ptr)
            p.value.toByteArray()
        } finally {
            nativeHeap.free(p)
        }
    }
}
