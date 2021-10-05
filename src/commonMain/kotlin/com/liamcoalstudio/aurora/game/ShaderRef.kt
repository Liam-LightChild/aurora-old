package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.shader.ShaderHandle
import kotlin.properties.Delegates

class ShaderRef(val vertexSource: String, val fragmentSource: String) : Reference<ShaderHandle> {
    private var value: ShaderHandle? = null

    override fun get() = value ?: throw IllegalStateException("value is $value")

    override fun fulfill(t: ShaderHandle) { if(value == null) value = t }
}