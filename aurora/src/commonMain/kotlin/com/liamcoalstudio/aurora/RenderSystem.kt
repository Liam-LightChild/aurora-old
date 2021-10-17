package com.liamcoalstudio.aurora

import com.liamcoalstudio.aurora.window.WindowHandle

interface RenderSystem {
    fun implement(window: WindowHandle)
    fun draw(window: WindowHandle)
    fun update(window: WindowHandle)
    fun unimplement(window: WindowHandle)
}