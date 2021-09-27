package com.liamcoalstudio.aurora

import kotlin.reflect.KProperty

@DslMarker annotation class ProgramDSL

value class Asset(private val contents: ByteArray) {
    val bytes get() = contents
    val string get() = contents.decodeToString()

    @ProgramDSL infix fun <T> into(f: (String) -> T) = f(string)
    @ProgramDSL infix fun <T> into(f: (ByteArray) -> T) = f(bytes)
}

@ProgramDSL
expect fun asset(path: String): Asset

@Suppress("UNCHECKED_CAST")
sealed class Resource<T : Resource<T, R>, R : Any> {
    internal lateinit var name: String
    lateinit var value: R internal set

    @ProgramDSL
    fun named(string: String) {
        name = string
    }

    class Shader : Resource<Shader, ShaderHandle>() {
        @ProgramDSL lateinit var vertex: String
        @ProgramDSL lateinit var fragment: String

        @ProgramDSL
        fun vertex(string: String) {
            vertex = string
        }

        @ProgramDSL fun vertex(asset: Asset) = vertex(asset.string)

        @ProgramDSL
        fun fragment(string: String) {
            fragment = string
        }

        @ProgramDSL fun fragment(asset: Asset) = fragment(asset.string)
    }
}

class ProgramBuilder {
    class SpecializedWindowBuilder(val handle: WindowHandle) {
        init {
            handle.use()
        }

        @ProgramDSL
        infix fun resources(f: ResourcesBuilder.() -> Unit) {
            ResourcesBuilder().also(f)
        }
    }

    class ResourcesBuilder {
        class New internal constructor()

        @ProgramDSL
        val resources = mutableMapOf<String, Resource<*, *>>()

        @ProgramDSL
        val shader = { Resource.Shader() }

        @ProgramDSL
        val new = New()

        @ProgramDSL
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

    @ProgramDSL
    val window get() = SpecializedWindowBuilder(windows.values.single())

    @ProgramDSL
    inline fun from(path: String) = asset(path)

    @ProgramDSL
    fun window(name: String, f: WindowOpenBuilder.() -> Unit) {
        windows[name] = Window.open(f)
    }

    @ProgramDSL
    fun window(name: String): SpecializedWindowBuilder {
        return SpecializedWindowBuilder(windows[name]!!)
    }

    internal fun run() {
        // TODO
    }
}

@ProgramDSL
expect fun program(f: ProgramBuilder.() -> Unit)
