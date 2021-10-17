package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.RenderSystem
import com.liamcoalstudio.aurora.window.WindowHandle

abstract class RenderSystemBasedGame : Game() {
    private var renderReal: RenderSystem? = null
    private lateinit var window: WindowHandle

    var render
        get() = renderReal ?: throw IllegalStateException("RenderSystem not set")
        private set(value) {
            renderReal?.unimplement(window) // possibly null!
            renderReal = value
            renderReal!!.implement(window)
        }

    abstract fun initial(): RenderSystem

    override fun init(window: WindowHandle) {
        super.init(window)
        this.window = window;
        render = initial().also { it.implement(window) }
    }

    override fun draw(window: WindowHandle) {
        super.draw(window)
        render.draw(window)
    }

    override fun update(window: WindowHandle) {
        super.update(window)
        render.update(window)
    }

    override fun quit(window: WindowHandle) {
        super.quit(window)
        render.unimplement(window)
    }
}
