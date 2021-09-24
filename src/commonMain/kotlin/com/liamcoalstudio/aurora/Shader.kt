package com.liamcoalstudio.aurora

import com.liamcoalstudio.aurora.enums.ShaderStage

expect class Shader internal constructor(id: UInt, stage: ShaderStage) : Resource {
    constructor(stage: ShaderStage, source: String)

    @Deprecated("Not bindable.", level = DeprecationLevel.HIDDEN)
    override fun bind()
    override fun delete()

    fun pushSource(src: String): String?

    val id: UInt
    val stage: ShaderStage

    override val isValid: Boolean
}
