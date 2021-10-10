package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.window.WindowHandle
import com.liamcoalstudio.aurora.window.openWindow

internal expect fun startGame()
internal expect fun endGame()
internal expect fun startFrame(windowHandle: WindowHandle)
internal expect fun endFrame(windowHandle: WindowHandle)
fun Game.run() {
    val window = openWindow { window() }

    window.use()
    init(window)
    try {
        while(!window.shouldClose) {
            startFrame(window)
            draw(window)
            update(window)
            endFrame(window)
        }
    } finally {
        quit(window)
        window.close()
        endGame()
    }
}