package com.liamcoalstudio.aurora

import com.liamcoalstudio.aurora.enums.ShaderStage

expect class Shader internal constructor(id: UInt, stage: ShaderStage) {
    constructor(stage: ShaderStage, source: String)

    fun delete(): Boolean
    fun pushSource(src: String): String?

    val id: UInt
    val stage: ShaderStage
    val isValid: Boolean
}
