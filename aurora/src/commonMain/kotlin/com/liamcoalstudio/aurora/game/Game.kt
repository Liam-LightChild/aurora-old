package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.*
import com.liamcoalstudio.aurora.shader.ShaderHandle
import com.liamcoalstudio.aurora.shader.ShaderInput
import com.liamcoalstudio.aurora.shader.ShaderInputType
import com.liamcoalstudio.aurora.shader.ShaderType
import com.liamcoalstudio.aurora.window.*
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

    protected val aspectRatioCompensation: Matrix4x4<Float>
        get() {
            return when (val span = WindowOpenBuilder().also { it.window() }.span) {
                is WindowSpan.Windowed -> {
                    Matrix.scale(Vector2f(span.height.toFloat() / span.width.toFloat(), 1f)).toMatrix4x4()
                }
                is WindowSpan.Fullscreen -> {
                    val f = getFullscreenSize()
                    Matrix.scale(Vector2f(f.y.toFloat() / f.x.toFloat(), 1f)).toMatrix4x4()
                }
                else -> Matrix4x4.identity
            }
        }

    internal inline fun <T : Reference<*>> add(name: String, a: T) = a.also { resources[name] = a }

    open fun init(window: WindowHandle) {}
    open fun draw(window: WindowHandle) {}
    open fun update(window: WindowHandle) {}
    open fun quit(window: WindowHandle) {}

    private val shaderCreators = mutableMapOf<String, CreatingShader<*>>()

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

    window.use()
    init(window)
    try {
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
