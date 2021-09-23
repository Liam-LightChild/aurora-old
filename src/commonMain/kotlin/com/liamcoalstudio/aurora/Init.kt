package com.liamcoalstudio.aurora

internal expect fun init()
internal expect fun terminate()

fun <T> create(f: () -> T) {
    init()
    f()
    terminate()
}
