package com.liamcoalstudio.aurora.buffer

import com.liamcoalstudio.aurora.dsl.AuroraDSLMarker
import com.liamcoalstudio.aurora.buffer.Buffer.Companion.build as buildBuffer

class BufferConstructor<T : Buffered<T>>(f: ConstructorBuilder<T>.() -> Unit) {
    val operations = ConstructorBuilder<T>().also(f).operations

    class ConstructorBuilder<T> {
        val operations = mutableListOf<(T, Buffer) -> Unit>()

        @AuroraDSLMarker
        inline fun from(crossinline p: T.() -> Float) {
            operations.add { t: T, buffer: Buffer -> buffer.writeFloat(t.p()) }
        }

        @AuroraDSLMarker
        inline fun from(crossinline p: T.() -> Int) {
            operations.add { t: T, buffer: Buffer -> buffer.writeInt(t.p()) }
        }

        @AuroraDSLMarker
        inline fun from(crossinline p: T.() -> Byte) {
            operations.add { t: T, buffer: Buffer -> buffer.writeByte(t.p()) }
        }

        @AuroraDSLMarker
        inline fun constant(f: Float) {
            operations.add { _: T, buffer: Buffer -> buffer.writeFloat(f) }
        }

        @AuroraDSLMarker
        inline fun constant(i: Int) {
            operations.add { _: T, buffer: Buffer -> buffer.writeInt(i) }
        }

        @AuroraDSLMarker
        inline fun constant(b: Byte) {
            operations.add { _: T, buffer: Buffer -> buffer.writeByte(b) }
        }
    }

    inline fun construct(v: T): Buffer {
        val b = Buffer.empty
        operations.forEach { it(v, b) }
        return b
    }

}

abstract class Buffered<T : Buffered<T>> {
    abstract class ConstructorContainer<T : Buffered<T>> {
        abstract val constructor: BufferConstructor<T>
    }

    abstract val container: ConstructorContainer<T>

    @Suppress("UNCHECKED_CAST")
    inline val buffer get() = container.constructor.construct(this as T)
    inline val bytes get() = buffer.byteArray
    inline fun push(handle: BufferHandle) = buffer.push(handle)
}

inline val<T, B : Buffered<T>, A : Iterable<B>> A.buffer: Buffer get() = buildBuffer { forEach { bytes(it.bytes) } }

inline val<T, B : Buffered<T>, A : Iterable<B>> A.bytes: ByteArray get() {
    return flatMap { it.bytes.toList() }.toByteArray()
}

inline fun<T, B : Buffered<T>, A : Iterable<B>> A.push(handle: BufferHandle) {
    buffer.push(handle)
}

class Test(val value: Float, val also: Float) : Buffered<Test>() {
    companion object : ConstructorContainer<Test>() {
        override val constructor = BufferConstructor<Test> {
            from(Test::value::get)
            from(Test::also::get)
        }
    }

    override val container get() = Test
}
