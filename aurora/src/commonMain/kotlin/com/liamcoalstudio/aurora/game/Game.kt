package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.*
import com.liamcoalstudio.aurora.shader.ShaderHandle
import com.liamcoalstudio.aurora.shader.ShaderType
import com.liamcoalstudio.aurora.texture.Texture
import com.liamcoalstudio.aurora.window.WindowHandle
import com.liamcoalstudio.aurora.window.WindowOpenBuilder
import com.liamcoalstudio.aurora.window.WindowSpan
import com.liamcoalstudio.aurora.window.getFullscreenSize
import kotlin.reflect.KProperty

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

    inline fun build(ref: TextureRef) {
        ref.fulfill(Texture.open(ref.image))
    }
}

