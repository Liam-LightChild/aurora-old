package com.liamcoalstudio.aurora

import kotlin.reflect.KProperty

class ArrayBuilder<T> {
    private val list = mutableListOf<T>()

    operator fun plus(t: T) {
        list += t
    }

    internal val value get() = list.toList()
}

class ProgramBuilder {
    internal val shaders = mutableListOf<Shader>()
    internal val attributes = mutableListOf<String>()
    internal val outputs = mutableMapOf<String, Int>()

    fun shader(shader: Shader) {
        shaders += shader
    }

    fun attributes(f: ArrayBuilder<String>.() -> Unit) {
        attributes.addAll(ArrayBuilder<String>().apply(f::invoke).value)
    }

    // TODO outputs {
    //          +String /* Name */ into Int /* ColorNumber */
    //      }
}

fun ProgramBuilder.test() {
    attributes {

    }
}

expect class Program(builder: ProgramBuilder.() -> Unit) {
    fun use()
    fun delete()

    inline fun with(crossinline f: () -> Unit)
}
