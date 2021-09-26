package com.liamcoalstudio.aurora

expect enum class ShaderType {
    VERTEX,
    FRAGMENT
}

expect value class ShaderHandle(val handle: UInt) {
    companion object {
        fun open(): ShaderHandle
    }

    fun addShader(type: ShaderType, source: String)
    fun finish()
    fun use()
    fun delete()
}