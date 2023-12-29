plugins {
    kotlin("jvm") version "1.9.21"
}

group = "net.cjsah"
version = "0.0.0"

repositories {
    mavenCentral()
}

val ktor = "2.3.7"
dependencies {
    implementation("com.google.code.gson:gson:2.9.1")
    implementation("io.ktor:ktor-server-core-jvm:${ktor}")
    implementation("io.ktor:ktor-server-cio-jvm:${ktor}")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<Jar> {
    manifest {
        attributes(Pair("Main-Class", "net.cjsah.test.Main"))
    }
}

