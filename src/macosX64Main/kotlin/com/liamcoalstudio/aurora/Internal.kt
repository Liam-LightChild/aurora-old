package com.liamcoalstudio.aurora

import com.liamcoalstudio.aurora.window.WindowConfig
import com.liamcoalstudio.aurora.window.WindowHandle
import kotlinx.cinterop.pointed
import kotlinx.cinterop.reinterpret

actual object Internal {
    actual fun openWindow(
        name: String,
        fullscreen: Boolean,
        width: Int?,
        height: Int?,
        share: WindowHandle?,
        config: Map<WindowConfig, Any>
    ): WindowHandle {
        glfwWindowHint(GLFW_VISIBLE, if(config[WindowConfig.Visible] == false) 0 else 1)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)

        return WindowHandle(
            if (fullscreen) {
                val v = glfwGetVideoMode(glfwGetPrimaryMonitor())!!.pointed
                glfwCreateWindow(v.width, v.height, name, glfwGetPrimaryMonitor(), share?.handle?.reinterpret())
            } else {
                glfwCreateWindow(width!!, height!!, name, null, share?.handle?.reinterpret())
            }!!.reinterpret()
        )
    }
}
