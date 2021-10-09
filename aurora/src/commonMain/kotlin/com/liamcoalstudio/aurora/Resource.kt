package com.liamcoalstudio.aurora

abstract class Resource {
    private val useHandlers = mutableListOf<() -> Unit>()
    private val deleteHandlers = mutableListOf<() -> Unit>()

    protected abstract fun resourceUse()
    protected abstract fun resourceDelete()

    fun use() {
        resourceUse()
        useHandlers.forEach { it() }
    }

    fun delete() {
        resourceDelete()
        deleteHandlers.forEach { it() }
    }

    fun onUse(f: () -> Unit) { useHandlers.add(f) }
    fun onDelete(f: () -> Unit) { deleteHandlers.add(f) }
}

abstract class IndexBoundResource {
    private val useHandlers = mutableListOf<(i: Int) -> Unit>()
    private val deleteHandlers = mutableListOf<() -> Unit>()

    protected abstract fun resourceUse(i: Int)
    protected abstract fun resourceDelete()

    fun use(i: Int) {
        resourceUse(i)
        useHandlers.forEach { it(i) }
    }

    fun delete() {
        resourceDelete()
        deleteHandlers.forEach { it() }
    }

    fun onUse(f: (index: Int) -> Unit) { useHandlers.add(f) }
    fun onDelete(f: () -> Unit) { deleteHandlers.add(f) }
}
