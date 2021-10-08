package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.game.Game
import com.liamcoalstudio.aurora.game.run
import com.liamcoalstudio.aurora.window.WindowHandle
import com.liamcoalstudio.aurora.window.WindowOpenBuilder
import kotlin.test.Test
import kotlin.test.assertTrue

class BasicGameTest : Game() {
    override fun WindowOpenBuilder.window() {
        windowed(800, 600)
        title("Window")
    }

    override fun update(window: WindowHandle) {
        window.shouldClose = true
        assertTrue(window.shouldClose, "window would not close")
    }

    @Test
    fun basicGame_test() = run()
}
