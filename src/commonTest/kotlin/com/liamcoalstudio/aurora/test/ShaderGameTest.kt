package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.game.Game
import com.liamcoalstudio.aurora.game.run
import com.liamcoalstudio.aurora.window.WindowHandle
import com.liamcoalstudio.aurora.window.WindowOpenBuilder
import kotlin.test.Test

class ShaderGameTest : Game() {
    override fun WindowOpenBuilder.window() {
        invisible()
        title("Shader Game")
    }

    val shader by shader<Nothing> {
        vertex("""
            #version 150 core
            
            void main() {
                gl_Position = vec4(0, 0, 0, 1);
            }
        """.trimIndent())

        fragment("""
            #version 150 core
            
            void main() { }
        """.trimIndent())
    }

    override fun init(window: WindowHandle) {
        build(shader)
        window.shouldClose = true
    }

    override fun quit(window: WindowHandle) {
        shader.get().delete()
    }

    @Test
    fun shaderGame_test() = ShaderGameTest().run()
}