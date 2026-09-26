import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    id("application")
}

application {
    mainClass.set("net.janrupf.ujr.example.smoke.SmokeTest")
    applicationDefaultJvmArgs = listOf("-Xcheck:jni")

    if (Os.isFamily(Os.FAMILY_MAC)) {
        applicationDefaultJvmArgs += listOf("-XstartOnFirstThread")
    }
}

dependencies {
    implementation(project(":modules:ultralight-java-reborn-core"))
    runtimeOnly(project(":modules:ultralight-java-reborn-platform-jni"))
}

tasks.named<JavaExec>("run") {
    // The Ultralight runtime isn't bundled, see JniPlatformOptions
    val ultralightDirectory = providers.gradleProperty("ujr.ultralightDirectory").orNull ?: System.getenv("ULTRALIGHT_DIR")
    if (ultralightDirectory != null) {
        systemProperty("ujr.ultralightDirectory", file(ultralightDirectory).absolutePath)
    }
}
