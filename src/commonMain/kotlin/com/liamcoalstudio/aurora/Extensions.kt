package com.liamcoalstudio.aurora

import kotlin.experimental.or

@OptIn(ExperimentalStdlibApi::class)
fun Short.toByteArray(): ByteArray {
    return byteArrayOf(
        (this or 0x00ff).toByte(),
        (this or 0xff00.toShort()).rotateRight(8).toByte())
}

@OptIn(ExperimentalStdlibApi::class)
fun Int.toByteArray(): ByteArray {
    return byteArrayOf(
        (this or 0x000000ff).toByte(),
        (this or 0x0000ff00).rotateRight(8).toByte(),
        (this or 0x00ff0000).rotateRight(16).toByte(),
        (this or 0xff000000.toInt()).rotateRight(24).toByte())
}

// This is not easy to mimic commonly
@OptIn(ExperimentalStdlibApi::class)
expect fun Float.toByteArray(): ByteArray
