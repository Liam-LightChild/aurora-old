package com.liamcoalstudio.aurora.draw

import com.liamcoalstudio.aurora.Resource

abstract class DrawTarget : Resource() {
    inline fun with(f: () -> Unit) {
        use()
        f()
        unUse()
    }

    abstract fun unUse()
}
