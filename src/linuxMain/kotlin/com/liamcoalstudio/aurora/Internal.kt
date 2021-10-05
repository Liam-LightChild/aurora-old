package com.liamcoalstudio.aurora

import com.liamcoalstudio.aurora.window.WindowConfig
import com.liamcoalstudio.aurora.window.WindowHandle
import kotlinx.cinterop.*

actual object Internal {
    actual fun openWindow(
        name: String,
        fullscreen: Boolean,
        width: Int?,
        height: Int?,
        share: WindowHandle?,
        config: Map<WindowConfig, Any>
    ): WindowHandle {
        glfwWindowHint(GLFW_VISIBLE, if(config[WindowConfig.Visible] == true) 1 else 0)

        return (WindowHandle(
            if (fullscreen) {
                val v = glfwGetVideoMode(glfwGetPrimaryMonitor())!!.pointed
                glfwCreateWindow(v.width, v.height, name, glfwGetPrimaryMonitor(), share?.handle?.reinterpret())
            } else {
                glfwCreateWindow(width!!, height!!, name, null, share?.handle?.reinterpret())
            }?.reinterpret()
                ?: throw IllegalStateException(kotlin.run {
                    val c = nativeHeap.alloc<CPointerVar<ByteVar>>()
                    glfwGetError(c.ptr)
                    c.pointed?.ptr?.toKString()
                }))
            .also {
                it.use()
                glewExperimental = 1u
                glewInit()
            }
        )
    }
}
