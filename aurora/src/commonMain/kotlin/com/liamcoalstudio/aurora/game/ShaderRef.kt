package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.shader.ShaderHandle
import com.liamcoalstudio.aurora.shader.ShaderInput
import kotlin.reflect.KProperty

class ShaderRef<T>(val vertexSource: String, val fragmentSource: String, val inputs: List<ShaderInput<*, T>>) : Reference<ShaderHandle<T>> {
    private var value: ShaderHandle<T>? = null

    override fun get() = value ?: throw IllegalStateException("value is $value")
    override fun fulfill(t: ShaderHandle<T>) { if(value == null) value = t }
}

fun <T> Game.shader(f: CreatingShader<T>.() -> Unit) = object : Delegatable<ShaderRef<T>> {
    val value = CreatingShader<T>(this@shader).also(f).build()

    override fun getValue(t: Any?, p: KProperty<*>) = value
}
