plugins {
    kotlin("multiplatform") version "1.5.31"
    id("maven-publish")
    kotlin("plugin.serialization") version "1.5.31"
}

group = "com.liamcoalstudio"
version = "2022.1-2021.10.07.2"

repositories {
    mavenCentral()
}

kotlin {
//    jvm()

    metadata {
        compilations.all {
            kotlinOptions {
                freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
            }
        }
    }

    // DESKTOP

    linuxX64("linux") {
        compilations.getByName("main") {
            val external by cinterops.creating {
                defFile("external.def")
                packageName("com.liamcoalstudio.aurora")
                includeDirs("/usr/include", "/usr/local/include", "/usr/include/x86_64-linux-gnu/")
                project.tasks[interopProcessingTaskName].enabled = System.getProperty("os.name") == "Linux"
            }
        }

        compilations.all {
            kotlinOptions {
                freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
            }

            compileKotlinTask.enabled = System.getProperty("os.name") == "Linux"
        }
    }

    macosX64("macos") {
        compilations.getByName("main") {
            val external by cinterops.creating {
                defFile("external.def")
                packageName("com.liamcoalstudio.aurora")
                includeDirs("/usr/include", "/usr/local/include", "/usr/include/x86_64-linux-gnu/")
                project.tasks[interopProcessingTaskName].enabled = System.getProperty("os.name") == "Mac OS X"
            }
        }

        compilations.all {
            kotlinOptions {
                freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
            }

            compileKotlinTask.enabled = System.getProperty("os.name") == "Mac OS X"
        }
    }

//    mingwX64("windows") {
//        compilations.getByName("main") {
//            val external by cinterops.creating {
//                defFile("external.def")
//                packageName("com.liamcoalstudio.aurora")
//                includeDirs("/usr/include", "/usr/local/include", "/usr/include/x86_64-linux-gnu/")
//            }
//        }
//        compilations.all {
//            kotlinOptions {
//                freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
//            }
//        }
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common:1.5.31")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.0")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val linuxMain by getting
        val linuxTest by getting
//        val windowsMain by getting
//        val windowsTest by getting
        val macosMain by getting
        val macosTest by getting

        val desktopMain by creating {
            dependsOn(commonMain)
            linuxMain.dependsOn(this)
//            windowsMain.dependsOn(this)
            macosMain.dependsOn(this)
        }

        val desktopTest by creating {
            dependsOn(commonTest)
            linuxTest.dependsOn(this)
//            windowsTest.dependsOn(this)
            macosTest.dependsOn(this)
        }
    }

    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/LiamCoal/aurora")

                credentials {
                    username = project.findProperty("gpr.user")?.toString() ?: System.getenv("USERNAME")
                    password = project.findProperty("gpr.key")?.toString() ?: System.getenv("TOKEN")
                }
            }
        }

        publications {}
    }
}
