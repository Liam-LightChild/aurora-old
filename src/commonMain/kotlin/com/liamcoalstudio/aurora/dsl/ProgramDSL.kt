package com.liamcoalstudio.aurora.dsl

import com.liamcoalstudio.aurora.shader.ShaderHandle
import com.liamcoalstudio.aurora.shader.ShaderType
import com.liamcoalstudio.aurora.window.WindowHandle
import com.liamcoalstudio.aurora.window.WindowOpenBuilder
import com.liamcoalstudio.aurora.window.openWindow
import kotlin.reflect.KProperty

@DslMarker @Deprecated("Replace with other annotations", ReplaceWith("Builder")) annotation class ProgramDSL

value class Asset(private val contents: ByteArray) {
    val bytes get() = contents
    val string get() = contents.decodeToString()

    @AuroraDSLMarker
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Usage discouraged.")
    infix fun <T> into(f: (String) -> T) = f(string)

    @AuroraDSLMarker
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Usage discouraged.")
    infix fun <T> into(f: (ByteArray) -> T) = f(bytes)
}

@AuroraDSLMarker
expect fun asset(path: String): Asset

@Suppress("UNCHECKED_CAST")
sealed class Resource<T : Resource<T, R>, R : Any> {
    internal lateinit var name: String
    lateinit var value: R internal set

    @AuroraDSLMarker
    fun named(string: String) {
        name = string
    }

    class Shader : Resource<Shader, ShaderHandle>() {
        @AuroraDSLMarker
        lateinit var vertex: String
        @AuroraDSLMarker
        lateinit var fragment: String

        @AuroraDSLMarker
        fun vertex(string: String) {
            vertex = string
        }

        @AuroraDSLMarker
        fun vertex(asset: Asset) = vertex(asset.string)

        @AuroraDSLMarker
        fun fragment(string: String) {
            fragment = string
        }

        @AuroraDSLMarker
        fun fragment(asset: Asset) = fragment(asset.string)
    }
}

class ProgramBuilder {
    class SpecializedWindowBuilder(val handle: WindowHandle) {
        init {
            handle.use()
        }

        @AuroraDSLMarker
        infix fun resources(f: ResourcesBuilder.() -> Unit) {
            ResourcesBuilder().also(f)
        }
    }

    class ResourcesBuilder {
        class New internal constructor()

        @AuroraDSLMarker
        val resources = mutableMapOf<String, Resource<*, *>>()
        @AuroraDSLMarker
        val shader = { Resource.Shader() }
        @AuroraDSLMarker
        val new = New()

        @AuroraDSLMarker
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

    @AuroraDSLMarker
    val window get() = SpecializedWindowBuilder(windows.values.single())
    
    @AuroraDSLMarker
    @Deprecated("Use `asset` instead", ReplaceWith("asset", "com.liamcoalstudio.aurora.dsl.asset"))
    inline fun from(path: String) = asset(path)

    @AuroraDSLMarker
    fun window(name: String, f: WindowOpenBuilder.() -> Unit) {
        windows[name] = openWindow(f)
    }

    @AuroraDSLMarker
    fun window(name: String): SpecializedWindowBuilder {
        return SpecializedWindowBuilder(windows[name]!!)
    }

    internal fun run() {
        // TODO
    }
}

@AuroraDSLMarker
expect fun program(f: ProgramBuilder.() -> Unit)
