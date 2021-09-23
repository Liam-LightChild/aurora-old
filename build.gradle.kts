plugins {
    kotlin("multiplatform") version "1.5.30"
}

group = "com.liamcoalstudio"
version = "1.0-SNAPSHOT"

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
    mingwX64()
    macosX64()

    sourceSets {
//        val nativeMain by getting {
//            dependencies {
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt")
//                implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.21")
//                implementation("com.squareup.okio:okio-multiplatform:2.10.0")
//            }
//        }
//
//        val nativeTest by getting

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
