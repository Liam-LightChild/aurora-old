package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.RenderSystem
import com.liamcoalstudio.aurora.window.WindowHandle

abstract class RenderSystemBasedGame : Game() {
    private var renderReal: RenderSystem? = null

    var render
        get() = renderReal ?: throw IllegalStateException("RenderSystem not set")
        private set(value) {
            renderReal?.unimplement() // possibly null!
            renderReal = value
            renderReal!!.implement()
        }

    abstract fun initial(): RenderSystem

    override fun init(window: WindowHandle) {
        super.init(window)
        render = initial().also { it.implement() }
    }

    override fun draw(window: WindowHandle) {
        super.draw(window)
        render.draw()
    }

    override fun update(window: WindowHandle) {
        super.update(window)
        render.update()
    }

    override fun quit(window: WindowHandle) {
        super.quit(window)
        render.unimplement()
    }
}
