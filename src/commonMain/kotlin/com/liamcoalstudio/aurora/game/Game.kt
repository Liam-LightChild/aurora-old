package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.shader.ShaderHandle
import com.liamcoalstudio.aurora.shader.ShaderInput
import com.liamcoalstudio.aurora.shader.ShaderInputType
import com.liamcoalstudio.aurora.shader.ShaderType
import com.liamcoalstudio.aurora.window.WindowHandle
import com.liamcoalstudio.aurora.window.WindowOpenBuilder
import com.liamcoalstudio.aurora.window.openWindow
import kotlin.reflect.KProperty
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

class CreatingWindow internal constructor(private val f: WindowOpenBuilder.() -> Unit) {
    operator fun getValue(t: Any?, p: KProperty<*>): WindowHandle = build()

    internal inline fun build(): WindowHandle {
        return openWindow(f)
    }
}

interface Delegatable<T> {
    operator fun getValue(t: Any?, p: KProperty<*>): T
}

abstract class Game {
    init {
        startGame()
    }

    abstract fun WindowOpenBuilder.window()

    internal val resources = mutableMapOf<String, Reference<*>>()

    internal inline fun <T : Reference<*>> add(name: String, a: T) = a.also { resources[name] = a }

    open fun init(window: WindowHandle) {}
    open fun draw(window: WindowHandle) {}
    open fun update(window: WindowHandle) {}
    open fun quit(window: WindowHandle) {}

    private val shaderCreators = mutableMapOf<String, CreatingShader<*>>()

    fun <T> shader(f: CreatingShader<T>.() -> Unit) = object : Delegatable<ShaderRef<T>> {
        val value = CreatingShader<T>(this@Game).also(f).build()

        override fun getValue(t: Any?, p: KProperty<*>) = value
    }

    fun window(f: WindowOpenBuilder.() -> Unit) = object : Delegatable<WindowHandle> {
        val value = CreatingWindow(f).build()

        override fun getValue(t: Any?, p: KProperty<*>) = value
    }

    inline fun <T> build(ref: ShaderRef<T>) {
        ref.fulfill(ShaderHandle.open<T>().also {
            it.addShader(ShaderType.VERTEX, ref.vertexSource)
            it.addShader(ShaderType.FRAGMENT, ref.fragmentSource)
            ref.inputs.forEach(it::addInput)
            it.finish()
        })
    }
}

internal expect fun startGame()
internal expect fun endGame()
internal expect fun startFrame(windowHandle: WindowHandle)
internal expect fun endFrame(windowHandle: WindowHandle)

fun Game.run() {
    val window = openWindow { window() }

    try {
        window.use()
        init(window)

        while(!window.shouldClose) {
            startFrame(window)
            draw(window)
            update(window)
            endFrame(window)
        }
    } finally {
        quit(window)
        window.close()
        endGame()
    }
}
