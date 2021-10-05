package com.liamcoalstudio.aurora.dsl

import com.liamcoalstudio.aurora.shader.ShaderHandle
import com.liamcoalstudio.aurora.shader.ShaderType
import com.liamcoalstudio.aurora.window.WindowHandle
import com.liamcoalstudio.aurora.window.WindowOpenBuilder
import com.liamcoalstudio.aurora.window.openWindow
import kotlin.reflect.KProperty

value class Asset(private val contents: ByteArray) {
    val bytes get() = contents
    val string get() = contents.decodeToString()

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Usage discouraged.")
    infix fun <T> into(f: (String) -> T) = f(string)

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Usage discouraged.")
    infix fun <T> into(f: (ByteArray) -> T) = f(bytes)
}

expect fun asset(path: String): Asset

@Suppress("UNCHECKED_CAST")
sealed class Resource<T : Resource<T, R>, R : Any> {
    internal lateinit var name: String
    lateinit var value: R internal set

    fun named(string: String) {
        name = string
    }

    class Shader : Resource<Shader, ShaderHandle>() {
        lateinit var vertex: String
        lateinit var fragment: String

        fun vertex(string: String) {
            vertex = string
        }

        fun vertex(asset: Asset) = vertex(asset.string)

        fun fragment(string: String) {
            fragment = string
        }

        fun fragment(asset: Asset) = fragment(asset.string)
    }
}

class ProgramBuilder {
    class SpecializedWindowBuilder(val handle: WindowHandle) {
        init {
            handle.use()
        }

        infix fun resources(f: ResourcesBuilder.() -> Unit) {
            ResourcesBuilder().also(f)
        }
    }

    class ResourcesBuilder {
        class New internal constructor()

        val resources = mutableMapOf<String, Resource<*, *>>()
        val shader = { Resource.Shader() }
        val new = New()

        infix fun New.shader(fn: Resource.Shader.() -> Unit) {
            val r = Resource.Shader().also(fn)
            val s = ShaderHandle.open()
            s.addShader(ShaderType.VERTEX, r.vertex)
            s.addShader(ShaderType.FRAGMENT, r.fragment)
            s.finish()
            r.value = s
            resources[r.name] = r
        }

        @Suppress("UNCHECKED_CAST")
        inline operator fun <R> String.getValue(t: Any?, p: KProperty<*>): R {
            return resources[this]?.value as R? ?: throw IllegalStateException("$this expected, but did not exist")
        }
    }

    private var windows = mutableMapOf<String, WindowHandle>()

    val window get() = SpecializedWindowBuilder(windows.values.single())
    
    @Deprecated("Use `asset` instead", ReplaceWith("asset", "com.liamcoalstudio.aurora.dsl.asset"))
    inline fun from(path: String) = asset(path)

    fun window(name: String, f: WindowOpenBuilder.() -> Unit) {
        windows[name] = openWindow(f)
    }

    fun window(name: String): SpecializedWindowBuilder {
        return SpecializedWindowBuilder(windows[name]!!)
    }

    internal fun run() {
        // TODO
    }
}

@Deprecated("Use new \"game\" system")
expect fun program(f: ProgramBuilder.() -> Unit)
