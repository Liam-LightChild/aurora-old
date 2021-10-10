package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.shader.ShaderInput
import com.liamcoalstudio.aurora.shader.ShaderInputType
import kotlin.reflect.KProperty1

class CreatingShader<T> internal constructor(private val game: Game) {
    @Deprecated("not recommended")
    constructor(game: Game, f: CreatingShader<T>.() -> Unit) : this(game) {
        f()
    }

    private lateinit var vertex: String
    private lateinit var fragment: String

    val inputs = mutableListOf<ShaderInput<*, T>>()

    fun vertex(s: String) { vertex = s }
    fun fragment(s: String) { fragment = s }

    fun vertex(s: () -> String) { vertex = s() }
    fun fragment(s: () -> String) { fragment = s() }

    fun <V> input(p: KProperty1<T, V>, type: ShaderInputType<V>) {
        inputs.add(ShaderInput(p.name, type, p::get))
    }

    internal fun build() = ShaderRef(vertex, fragment, inputs)
}