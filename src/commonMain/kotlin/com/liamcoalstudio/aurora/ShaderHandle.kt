package com.liamcoalstudio.aurora

expect value class ShaderHandle(val handle: UInt) {
    companion object {
        fun open(): ShaderHandle
    }

    fun addShader(type: ShaderType, source: String)
    fun finish()
    fun use()
    fun delete()
}