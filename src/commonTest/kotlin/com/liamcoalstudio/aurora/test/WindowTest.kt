package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.Window
import com.liamcoalstudio.aurora.init
import com.liamcoalstudio.aurora.terminate
import kotlin.test.Test

class WindowTest {
    @Test
    fun test_Window_windowed() {
        init()
        val w = Window.windowed("Test Window", 16, 16, null)
        w.close()
        terminate()
    }

    @Test
    fun test_Window_fullscreen() {
        init()
        val w = Window.fullscreen("Test Window", null)
        w.close()
        terminate()
    }
}
