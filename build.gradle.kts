plugins {
    kotlin("multiplatform") version "1.5.30"
}

group = "com.liamcoalstudio"
version = "2022.1-2021.09.23.1"

repositories {
    mavenCentral()
}

kotlin {
//    jvm {
//        withJava()
//    }

    linuxX64 {
        compilations.getByName("main") {
            val external by cinterops.creating {
                defFile("src/linuxX64Main/external.def")
                packageName("com.liamcoalstudio.aurora")
                includeDirs("/usr/include", "/usr/local/include", "/usr/include/x86_64-linux-gnu/")
            }
        }
    }

    macosX64 {
        compilations.getByName("main") {
            val external by cinterops.creating {
                defFile("src/macosX64Main/external.def")
                packageName("com.liamcoalstudio.aurora")
                includeDirs("/usr/local/include")
            }
        }
    }

    mingwX64()

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
                implementation("com.squareup.okio:okio-multiplatform:2.10.0")
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

//        val jvmMain by getting // TODO
//        val jvmTest by getting

        val linuxX64Main by getting
        val linuxX64Test by getting

        val mingwX64Main by getting // TODO
        val mingwX64Test by getting

        val macosX64Main by getting
        val macosX64Test by getting
    }
}
