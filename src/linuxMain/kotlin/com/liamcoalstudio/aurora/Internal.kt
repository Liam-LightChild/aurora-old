package com.liamcoalstudio.aurora

import kotlinx.cinterop.pointed
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toByte

internal actual object Internal {
    actual fun openWindow(
        name: String,
        fullscreen: Boolean,
        width: Int?,
        height: Int?,
        share: WindowHandle?,
        config: Map<WindowConfig, Any>
    ): WindowHandle {
        glfwWindowHint(GLFW_VISIBLE, if(config[WindowConfig.WINDOW_VISIBLE] == true) 1 else 0)

        return WindowHandle(if(fullscreen) {
            val v = glfwGetVideoMode(glfwGetPrimaryMonitor())!!.pointed
            glfwCreateWindow(v.width, v.height, name, glfwGetPrimaryMonitor(), share?.handle?.reinterpret())
        } else {
            glfwCreateWindow(width!!, height!!, name, null, share?.handle?.reinterpret())
        }!!.reinterpret())
    }
}
