package com.liamcoalstudio.aurora.assets

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import platform.posix.*

@OptIn(ExperimentalSerializationApi::class)
actual fun asset(path: String): Asset {
    val f = fopen("assets/$path.ast", "r")
    fseek(f, SEEK_END.toLong(), 0)
    val size = ftell(f).toInt()
    fseek(f, SEEK_SET.toLong(), 0)
    val bytes = ByteArray(size)
    bytes.usePinned { fread(it.addressOf(0), size.toULong(), 1uL, f) }
    fclose(f)
    return Cbor.decodeFromByteArray(bytes)
}

fun test() {
    shader<Nothing>(asset("shader"))
}
