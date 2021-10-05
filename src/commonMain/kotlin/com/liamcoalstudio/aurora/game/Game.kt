package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.dsl.Resource
import com.liamcoalstudio.aurora.shader.ShaderHandle
import com.liamcoalstudio.aurora.shader.ShaderType
import com.liamcoalstudio.aurora.window.WindowHandle
import com.liamcoalstudio.aurora.window.WindowOpenBuilder
import com.liamcoalstudio.aurora.window.openWindow
import kotlin.reflect.KProperty

class CreatingShader internal constructor(private val game: Game) {
    @Deprecated("not recommended")
    constructor(game: Game, f: CreatingShader.() -> Unit) : this(game) {
        f()
    }

    private lateinit var vertex: String
    private lateinit var fragment: String

    fun vertex(s: String) { vertex = s }
    fun fragment(s: String) { fragment = s }

    fun vertex(s: () -> String) { vertex = s() }
    fun fragment(s: () -> String) { fragment = s() }

    internal fun build() = ShaderRef(vertex, fragment)
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

    abstract val window: WindowHandle

    internal val resources = mutableMapOf<String, Reference<*>>()

    internal inline fun <T : Reference<*>> add(name: String, a: T) = a.also { resources[name] = a }

    open fun init() {}
    open fun draw() {}
    open fun update() {}
    open fun quit() {}

    private val shaderCreators = mutableMapOf<String, CreatingShader>()

    fun shader(f: CreatingShader.() -> Unit) = object : Delegatable<ShaderRef> {
        val value = CreatingShader(this@Game).also(f).build()

        override fun getValue(t: Any?, p: KProperty<*>) = value
    }

    fun window(f: WindowOpenBuilder.() -> Unit) = object : Delegatable<WindowHandle> {
        val value = CreatingWindow(f).build()

        override fun getValue(t: Any?, p: KProperty<*>) = value
    }

    inline fun build(ref: ShaderRef) {
        ref.fulfill(ShaderHandle.open().also {
            it.addShader(ShaderType.VERTEX, ref.vertexSource)
            it.addShader(ShaderType.FRAGMENT, ref.fragmentSource)
            it.finish()
        })
    }
}

internal expect fun startGame()
internal expect fun endGame()
internal expect fun startFrame(windowHandle: WindowHandle)
internal expect fun endFrame(windowHandle: WindowHandle)

fun Game.run() {
    try {
        window.use()
        init()

        while(!window.shouldClose) {
            startFrame(window)
            draw()
            endFrame(window)
            update()
        }
    } finally {
        quit()
        window.close()
        endGame()
    }
}
