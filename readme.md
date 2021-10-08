# Aurora Game Engine

First release version is currently work-in-progress. When it is released,
there will be actual documentation, including an actual readme file.

You can use pre-release versions in the meantime.

## Building

Each platform should be built **on the platform it is designed for**, so that
all required libraries and headers are present at build time and possibly run
time.

**Build Linux:** `./gradlew linuxMainKlibrary`  
**Test Linux:** `./gradlew linuxTest`

**Build macOS:** `./gradlew macosMainKlibrary`  
**Test macOS:** `./gradlew macosTest`

### Multi-platform setup

Aurora uses a hierarchical multi-platform setup. It is arranged in this way:

* `common` (`main` and `test`) Common code across all platforms. Contains the
  main API elements.
    * `desktop` (`main` and `test`) Common code across native desktop platforms.
        * `linux` (`main` and `test`) Linux implementation
        * `macos` (`main` and `test`) macOS implementation

## Basic usage

Aurora is easy to get started with. The following code opens a window
and adds a simple shader to it:

```kotlin
class ShaderGame : Game() {
    // Config options for the window
    // Run by `Game`
    override fun WindowOpenBuilder.window() {
        invisible()
        title("Shader Game")
    }

    // Shader that is created when possible
    // `Nothing` here means no input is
    // configured.
    val shader by shader<Nothing> {
        vertex("""
                #version 150 core
                
                void main() {
                    gl_Position = vec4(0, 0, 0, 1);
                }
            """.trimIndent())

        // Sometimes called a pixel shader
        fragment("""
                #version 150 core
                
                void main() { }
            """.trimIndent())
    }

    override fun init(window: WindowHandle) {
        build(shader)
        window.shouldClose = true
    }
    
    // Empty frame handlers can be omitted.
    // override fun draw(window: WindowHandle) {}
    // override fun update(window: WindowHandle) {}

    override fun quit(window: WindowHandle) {
        shader.get().delete()
    }
}
```
