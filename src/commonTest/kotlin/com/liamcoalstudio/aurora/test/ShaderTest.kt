package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.*
import com.liamcoalstudio.aurora.enums.ShaderStage
import kotlin.native.concurrent.isFrozen
import kotlin.properties.Delegates
import kotlin.test.AfterClass
import kotlin.test.BeforeClass
import kotlin.test.Test

class ShaderTest {
    val w = Window.windowed("Test", 800, 600, null)

    companion object {
        @BeforeClass fun before() = init()
        @AfterClass fun after() = terminate()
    }

    @Test
    fun test_compileVertex() {
        w.with {
            val s = Shader(ShaderStage.VERTEX, "#version 150\n\nvoid main() {\n    // Nothing\n}")
            assert(s.isValid)
            s.delete()
        }
    }

    @Test
    fun test_compileFragment() {
        w.with {
            val s = Shader(ShaderStage.FRAGMENT, "#version 150\n\nvoid main() {\n    // Nothing\n}")
            assert(s.isValid)
            s.delete()
        }
    }

    @Test
    fun test_linkProgram() {
        w.with {
            val v = Shader(ShaderStage.VERTEX, "#version 150\n\nvoid main() {\n    // Nothing\n}")
            assert(v.isValid)

            val f = Shader(ShaderStage.FRAGMENT, "#version 150\n\nvoid main() {\n    // Nothing\n}")
            assert(f.isValid)

            val p = Program {
                shader(v)
                shader(f)
            }

            p.delete()
        }
    }
}
