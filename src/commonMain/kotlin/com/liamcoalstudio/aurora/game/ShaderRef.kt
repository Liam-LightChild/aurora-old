package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.shader.ShaderHandle
import com.liamcoalstudio.aurora.shader.ShaderInput
import kotlin.properties.Delegates

class ShaderRef<T>(val vertexSource: String, val fragmentSource: String, val inputs: List<ShaderInput<*, T>>) : Reference<ShaderHandle<T>> {
    private var value: ShaderHandle<T>? = null

    override fun get() = value ?: throw IllegalStateException("value is $value")

    override fun fulfill(t: ShaderHandle<T>) { if(value == null) value = t }
}