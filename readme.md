# Aurora Game Engine

_It's pretty cool, I suppose._

First release version is currently work-in-progress. When it is released,
there will be actual documentation, including an actual readme file.

## Building

_The building blocks of graphics come in separate pieces. It's up to you to
put them together in the way you want._

Each platform should be build **on the platform it is designed for**, so that
all required libraries and headers are present at build time and possibly run
time.

Build and Test everything that can be built on the host platform
(currently not working properly): `./gradlew build`  
Build Linux only: `./gradlew linuxMainKlibrary`  
Test Linux only: `./gradlew linuxTest`  
Build macOS only: `./gradlew macosMainKlibrary`  
Test macOS only: `./gradlew macosTest`

### Multi-platform setup

_If you `expect fun`, you shall have more `actual fun` than `expect`ed._

Aurora uses a hierarchical multi-platform setup. It is arranged in this way:

* `common` (`main` and `test`) Common code across all platforms. Contains the
  main API elements.
    * `desktop` (`main` and `test`) Common code across native desktop platforms.
        * `linux` (`main` and `test`) Linux Aurora implementation
        * `macos` (`main` and `test`) macOS Aurora implementation

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
