package com.liamcoalstudio.aurora

import com.liamcoalstudio.aurora.enums.ShaderStage

actual class Shader actual constructor(id: UInt, stage: ShaderStage) {
    actual fun use() {
        TODO("not implemented")
    }

    actual fun delete(): Boolean {
        TODO("not implemented")
    }

    actual fun pushSource(src: String): String? {
        TODO("not implemented")
    }

    actual val id: UInt
        get() = TODO("not implemented")
    actual val stage: ShaderStage
        get() = TODO("not implemented")

}
