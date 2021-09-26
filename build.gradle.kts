plugins {
    kotlin("multiplatform") version "1.5.31"
}

group = "com.liamcoalstudio"
version = "2022.1-2021.09.26.1"

repositories {
    mavenCentral()
}

kotlin {
    // TODO
    // jvm {
    //     withJava()
    // }

    linuxX64("linux") {
        compilations.getByName("main") {
            val external by cinterops.creating {
                defFile("src/linuxMain/external.def")
                packageName("com.liamcoalstudio.aurora")
                includeDirs("/usr/include", "/usr/local/include", "/usr/include/x86_64-linux-gnu/")
            }
        }
    }

    // FIXME unsupported
    // macosX64("macos") {
    //     compilations.getByName("main") {
    //         val external by cinterops.creating {
    //             defFile("src/macosX64Main/external.def")
    //             packageName("com.liamcoalstudio.aurora")
    //             includeDirs("/usr/local/include")
    //         }
    //     }
    // }

    // mingwX64("windows") // TODO

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common:1.5.31")
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        // val jvmMain by getting
        // val jvmTest by getting

        val linuxMain by getting
        val linuxTest by getting

        // val windowsMain by getting
        // val windowsTest by getting

        // val macosMain by getting
        // val macosTest by getting
    }
}
