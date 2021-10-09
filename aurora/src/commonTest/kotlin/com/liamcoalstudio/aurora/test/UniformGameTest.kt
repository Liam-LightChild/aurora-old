package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.Vector2f
import com.liamcoalstudio.aurora.Vector3f
import com.liamcoalstudio.aurora.draw.DrawCollection
import com.liamcoalstudio.aurora.game.Game
import com.liamcoalstudio.aurora.game.run
import com.liamcoalstudio.aurora.game.shader
import com.liamcoalstudio.aurora.shader.ShaderInputType
import com.liamcoalstudio.aurora.window.WindowHandle
import com.liamcoalstudio.aurora.window.WindowOpenBuilder
import kotlin.math.PI
import kotlin.math.sin
import kotlin.test.Test

class UniformGameTest : Game() {
    data class Vertex(val vPosition: Vector2f)

    override fun WindowOpenBuilder.window() {
        windowed(800, 600)
        title("Uniform Game")
    }

    private val shader by shader<Vertex> {
        vertex("""
            #version 150 core
            
            in vec2 vPosition;
            
            uniform mat4 uTransform;
            
            void main() {
                gl_Position = uTransform * vec4(vPosition, 0, 1);
            }
        """.trimIndent())

        fragment("""
            #version 150 core
            
            out vec4 oColor;
            
            uniform vec3 uColor;
            
            void main() {
                oColor = vec4(uColor, 1);
            }
        """.trimIndent())

        input(Vertex::vPosition, ShaderInputType.Vector2f)
    }

    private var frameCount = 0
    private var draw: DrawCollection<Vertex>? = null

    override fun init(window: WindowHandle) {
        build(shader)
        shader.get().use()
        draw = shader.get().constructDrawCollection(
            Vertex(Vector2f(-1f, -1f)),
            Vertex(Vector2f(1f, -1f)),
            Vertex(Vector2f(-1f, 1f)),

            Vertex(Vector2f(1f, 1f)),
            Vertex(Vector2f(1f, -1f)),
            Vertex(Vector2f(-1f, 1f)),
        )
        shader.get().uniform("uTransform").push(aspectRatioCompensation)
    }

    override fun draw(window: WindowHandle) {
        shader.get().uniform("uColor").push(Vector3f(
            sin((frameCount.toFloat() / 60f) * PI.toFloat()),
            sin((frameCount.toFloat() / 60f) * PI.toFloat() + PI.toFloat() / 2),
            sin((frameCount.toFloat() / 60f) * PI.toFloat() + PI.toFloat())
        ))
        draw?.draw()
    }

    override fun update(window: WindowHandle) {
        if(frameCount++ >= 60) window.shouldClose = true
    }

    override fun quit(window: WindowHandle) {
        draw?.delete()
        shader.get().delete()
    }

    @Test
    fun test() {
        run()
        assertNoError()
    }
}