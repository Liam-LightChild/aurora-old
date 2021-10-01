package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.AuroraDSLMarker

@AuroraDSLMarker
expect fun withGlfw(f: () -> Unit)
