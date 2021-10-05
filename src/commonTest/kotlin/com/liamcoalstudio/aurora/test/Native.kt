package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.dsl.AuroraDSLMarker

@AuroraDSLMarker
expect fun withGlfw(f: () -> Unit)
