package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.program
import kotlin.test.Test

class ProgramTest {
    @Test
    fun test_singleWindow() = program {
        window("window.default") {
            windowed(800, 600)
            title("Default Window")
        }
    }

    @Test
    fun test_singleWindow_invisible() = program {
        window("window.default") {
            windowed(800, 600)
            title("Default Window")
            visible(false)
        }
    }

    @Test
    fun test_singleWindowFullscreen() = program {
        window("window.default") {
            fullscreen()
            title("Default Window")
        }
    }

    @Test
    fun test_singleWindowFullscreen_invisible() = program {
        window("window.default") {
            fullscreen()
            title("Default Window")
            visible(false)
        }
    }

    @Test
    fun test_windowShare() = program {
        window("window.share") {
            invisible()
            title("Share Window")
        }

        window("window.default") {
            windowed(800, 600)
            title("Default Window")
            share(window("window.share").handle)
        }
    }
}
