group = "net.ccbluex.ultralight"
version = determineProjectVersion()

fun determineProjectVersion(): String {
    // Attempt to read the version from the project properties
    val propertyVersion = providers.gradleProperty("ujr.version").orNull
    if (!propertyVersion.isNullOrEmpty()) {
        return propertyVersion
    }

    // Else attempt to read the version from the commit hash
    fun git(vararg args: String) = providers.exec {
        commandLine("git", *args)
        isIgnoreExitValue = true
    }

    val revision = git("rev-parse", "--short=16", "HEAD")
    if (revision.result.get().exitValue != 0) {
        return "0.0.0-UNKNOWN"
    }

    val diff = git("diff", "--stat")
    val isDirty = diff.result.get().exitValue != 0 || diff.standardOutput.asText.get().isNotBlank()

    return "0.0.0${if (isDirty) "-DIRTY" else ""}-${revision.standardOutput.asText.get().trim()}"
}

subprojects {
    plugins.withId("java") {
        extensions.getByType(JavaPluginExtension::class.java).apply {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8

            withJavadocJar()
            withSourcesJar()
        }

        tasks.withType(JavaCompile::class.java) {
            options.encoding = "UTF-8"
        }

        tasks.named<Javadoc>("javadoc") {
            (options as StandardJavadocDocletOptions).addBooleanOption("Xdoclint:-missing", true)
        }
    }

    group = rootProject.group
    version = rootProject.version
}
