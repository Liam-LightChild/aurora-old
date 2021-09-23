package com.liamcoalstudio.aurora

import okio.Buffer
import kotlin.reflect.KProperty
import kotlin.collections.toList
import kotlin.collections.mutableListOf

data class Value<T>(val name: KProperty<*>, val f: Buffer.(value: T) -> Unit)

infix fun <T> KProperty<*>.written(f: Buffer.(value: T) -> Unit) = Value(this, f)

class BufferType<T>(vararg values: Value<T>) {
    val s: List<Value<T>>

    init {
        s = values.toList()
    }

    fun write(vararg values: T): ByteArray {
        val arrays = mutableListOf<ByteArray>()

        for(v in values) {
            for(w in s) {
                val buffer = Buffer()
                w.f(buffer, v)
                arrays.add(buffer.readByteArray())
            }
        }

        return arrays.reduce(ByteArray::plus)
    }
}

abstract class ShaderInput<T> {
    abstract val type: BufferType<T>

    open fun write(vararg values: T) = type.write(*values)
}
