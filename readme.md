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
  * `jvm` (`main` and `test`) **UNIMPLEMENTED**
  * `desktop` (`main` and `test`) Common code across native desktop platforms.
    * `linux` (`main` and `test`) Linux Aurora implementation
    * `macos` (`main` and `test`) macOS Aurora implementation
    * `windows` (`main` and `test`) **UNIMPLEMENTED**

## Basic usage

Aurora is quite simple to get started with. The following code opens a window
and adds a simple shader to it:

```kotlin
import com.liamcoalstudio.aurora.*

fun main() = project {
    // The text here is NOT the title of the window; it's the id!
    window("window.default") {
        title("Window")
        windowed(800, 600) // Short for span(WindowSpan.Windowed(800, 600))
    }

    // When there is a single window, just write `window` here.
    // Otherwise, write `window("<id>") resources {...}`
    window resources {
        add shader {
            // Resources have id's they are referred to by.
            named("shader.default")
            // `from("")` loads assets from the `assets/` directory.
            vertex(from("shader/default/vertex.glsl"))
            // `from` just calls `asset`, so this is equivalent
            fragment(asset("shader/default/fragment.glsl"))
        }

        // Example of getting a shader's value by id (explicit typing is
        // required)
        val shader: ShaderHandle by "shader.default"
    }
}
```
