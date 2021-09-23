package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.*
import com.liamcoalstudio.aurora.enums.ShaderStage
import kotlin.test.*

class ShaderTest : TestContainer() {
    @Test
    fun test_compileVertex() = run {
        val s = Shader(ShaderStage.VERTEX, "#version 150\n\nvoid main() {\n    // Nothing\n}")
        assertTrue(s.isValid)
        s.delete()
    }

    @Test
    fun test_compileFragment() = run {
        val s = Shader(ShaderStage.FRAGMENT, "#version 150\n\nvoid main() {\n    // Nothing\n}")
        assertTrue(s.isValid)
        s.delete()
    }

    @Test
    fun test_linkProgram() = run {
        val v = Shader(ShaderStage.VERTEX, "#version 150\n\nvoid main() {\n    // Nothing\n}")
        assertTrue(v.isValid)

        val f = Shader(ShaderStage.FRAGMENT, "#version 150\n\nvoid main() {\n    // Nothing\n}")
        assertTrue(f.isValid)

        val p = Program {
            shader(v)
            shader(f)
        }

        p.delete()
    }
}
