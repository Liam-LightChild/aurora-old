plugins {
    kotlin("multiplatform") version "1.5.31"
}

group = "com.liamcoalstudio"
version = "2022.1-2021.09.26.1"

repositories {
    mavenCentral()
}

kotlin {
    jvm()

    // DESKTOP

    linuxX64("linux") {
        compilations.getByName("main") {
            val external by cinterops.creating {
                defFile("external.def")
                packageName("com.liamcoalstudio.aurora")
                includeDirs("/usr/include", "/usr/local/include", "/usr/include/x86_64-linux-gnu/")
            }
        }
    }

    macosX64("macos") {
        compilations.getByName("main") {
            val external by cinterops.creating {
                defFile("external.def")
                packageName("com.liamcoalstudio.aurora")
                includeDirs("/usr/include", "/usr/local/include", "/usr/include/x86_64-linux-gnu/")
            }
        }
    }

    mingwX64("windows") {
        compilations.getByName("main") {
            val external by cinterops.creating {
                defFile("external.def")
                packageName("com.liamcoalstudio.aurora")
                includeDirs("/usr/include", "/usr/local/include", "/usr/include/x86_64-linux-gnu/")
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common:1.5.31")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting
        val jvmTest by getting

        val linuxMain by getting
        val linuxTest by getting
        val windowsMain by getting
        val windowsTest by getting
        val macosMain by getting
        val macosTest by getting

        val desktopMain by creating {
            dependsOn(commonMain)
            linuxMain.dependsOn(this)
            windowsMain.dependsOn(this)
            macosMain.dependsOn(this)
        }

        val desktopTest by creating {
            dependsOn(commonTest)
            linuxTest.dependsOn(this)
            windowsTest.dependsOn(this)
            macosTest.dependsOn(this)
        }
    }
}
