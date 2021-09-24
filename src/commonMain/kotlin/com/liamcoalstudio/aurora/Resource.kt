package com.liamcoalstudio.aurora

abstract class Resource {
    abstract fun delete()
    abstract fun bind()

    abstract val isValid: Boolean

    inline fun with(f: () -> Unit) {
        bind()
        f()
    }

    inline fun thenDelete(f: () -> Unit) {
        with(f)
        delete()
    }
}

abstract class ResourceWithLocation<T, L> {
    abstract fun delete()
    abstract fun bind(location: L)

    abstract var value: T

    inline fun with(at: L, f: () -> Unit) {
        bind(at)
        f()
    }

    inline fun thenDelete(at: L, f: () -> Unit) {
        with(at, f)
        delete()
    }
}
