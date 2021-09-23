package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.Window
import com.liamcoalstudio.aurora.init
import com.liamcoalstudio.aurora.terminate
import kotlin.test.AfterClass
import kotlin.test.BeforeClass
import kotlin.test.Test

class WindowTest {
    companion object {
        @BeforeClass
        fun before() {
            init()
        }

        @AfterClass
        fun after() {
            terminate()
        }
    }

    @Test
    fun test_Window_windowed() {
        val w = Window.windowed("Test Window", 16, 16, null)
        w.close()
    }

    @Test
    fun test_Window_fullscreen() {
        val w = Window.fullscreen("Test Window", null)
        w.close()
    }
}
