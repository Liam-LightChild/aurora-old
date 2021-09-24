package com.liamcoalstudio.aurora

import com.liamcoalstudio.aurora.enums.Key
import kotlinx.cinterop.*

actual class Window private constructor(val glfw: COpaquePointer) {
    init {
        glfwMakeContextCurrent(glfw.reinterpret())
        glewExperimental = 1u
        glewInit()
        glfwSetKeyCallback(glfw.reinterpret(), staticCFunction { _: COpaquePointer, key: Int, _: Int, action: Int, _: Int ->
            val k = when(key) {
                GLFW_KEY_A -> Key.A
                GLFW_KEY_B -> Key.B
                GLFW_KEY_C -> Key.C
                GLFW_KEY_D -> Key.D
                GLFW_KEY_E -> Key.E
                GLFW_KEY_F -> Key.F
                GLFW_KEY_G -> Key.G
                GLFW_KEY_H -> Key.H
                GLFW_KEY_I -> Key.I
                GLFW_KEY_J -> Key.J
                GLFW_KEY_K -> Key.K
                GLFW_KEY_L -> Key.L
                GLFW_KEY_M -> Key.M
                GLFW_KEY_N -> Key.N
                GLFW_KEY_O -> Key.O
                GLFW_KEY_P -> Key.P
                GLFW_KEY_Q -> Key.Q
                GLFW_KEY_R -> Key.R
                GLFW_KEY_S -> Key.S
                GLFW_KEY_T -> Key.T
                GLFW_KEY_U -> Key.U
                GLFW_KEY_V -> Key.V
                GLFW_KEY_W -> Key.W
                GLFW_KEY_X -> Key.X
                GLFW_KEY_Y -> Key.Y
                GLFW_KEY_Z -> Key.Z
                GLFW_KEY_SPACE -> Key.Space
                GLFW_KEY_LEFT_SHIFT -> Key.LeftShift
                GLFW_KEY_LEFT_CONTROL -> Key.LeftControl
                GLFW_KEY_RIGHT_SHIFT -> Key.RightShift
                GLFW_KEY_RIGHT_CONTROL -> Key.RightControl
                else -> Key.Unknown
            }

            if(k != Key.Unknown) {
                when(action) {
                    GLFW_PRESS -> Keyboard.press(k)
                    GLFW_RELEASE -> Keyboard.release(k)
                }
            }
        }.reinterpret())
    }

    /**
     * Completely closes the window. There is no way to recover the window after
     * calling this function.
     *
     * @see glfwDestroyWindow
     */
    actual fun close() {
        glfwDestroyWindow(glfw.reinterpret())
    }

    /**
     * Activates this window, making it the target of all GL calls.
     *
     * @see glfwMakeContextCurrent
     */
    actual fun activate() {
        glfwMakeContextCurrent(glfw.reinterpret())
    }

    /**
     * Activates this window, then runs the function. The function is inlined. Note that
     * this function leaves the window activated, and GL calls after calling this function
     * will still be pointing to this window.
     *
     * @see activate
     */
    actual inline fun <T> with(crossinline f: Window.() -> T) {
        activate()
        this.f()
    }

    actual companion object {
        /**
         * Creates a new window in windowed form, with a specified width and height (in
         * pixels).
         *
         * @param width Width in pixels of the newly created window. Does not include window
         * border elements.
         * @param height Height in pixels of the newly created window. Does not include
         * window border elements.
         * @param title Title of the created window. It will be shown above the content of
         * the window.
         * @param share An optional parameter containing a window that resources can be
         * copied from.
         *
         * @return A new window, [width] x [height], with a title from [title].
         *
         * @see glfwCreateWindow
         */
        actual fun windowed(
            title: String,
            width: Int,
            height: Int,
            share: Window?,
        ): Window {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)

            return Window(
                glfwCreateWindow(
                    width,
                    height,
                    title,
                    null,
                    share?.glfw?.reinterpret()
                )?.reinterpret() ?: throw glfwError()
            )
        }

        /**
         * Creates a new window in fullscreen form, covering the entire screen. The
         * fullscreen is achieved through opening the monitor, instead of using a
         * borderless window.
         *
         * @param title The title of the window. Shown in a few places.
         * @param share An optional parameter containing a window that resources can be
         * copied from.
         *
         * @return A new fullscreen window, taking over the monitor's contents while
         * focused, with a title from [title].
         *
         * @see glfwCreateWindow
         */
        actual fun fullscreen(
            title: String,
            share: Window?,
        ): Window {
            val v = glfwGetVideoMode(glfwGetPrimaryMonitor())!!.pointed
            return Window(
                glfwCreateWindow(
                    v.width,
                    v.height,
                    title,
                    glfwGetPrimaryMonitor(),
                    share?.glfw?.reinterpret()
                )?.reinterpret() ?: throw glfwError()
            )
        }
    }
}