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
