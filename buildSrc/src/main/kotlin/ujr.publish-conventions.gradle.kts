plugins {
    `maven-publish`
}

val mavenUser: String? = System.getenv("MAVEN_TOKEN_NAME")
val mavenPassword: String? = System.getenv("MAVEN_TOKEN_SECRET")

val isSnapshot = rootProject.version.toString().endsWith("-SNAPSHOT")

publishing {
    repositories {
        if (mavenUser != null && mavenPassword != null) {
            maven {
                name = "ccbluex"
                url = uri(if (isSnapshot) "https://maven.ccbluex.net/snapshots" else "https://maven.ccbluex.net/releases")

                credentials {
                    username = mavenUser
                    password = mavenPassword
                }
            }
        } else {
            maven {
                name = "local"
                url = uri(rootProject.layout.buildDirectory.dir("maven-repo"))
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                url.set("https://github.com/CCBlueX/ultralight-java-reborn")

                licenses {
                    license {
                        name.set("LGPLv3")
                        url.set("https://www.gnu.org/licenses/lgpl-3.0.en.html")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("Janrupf")
                        email.set("business.janrupf@gmail.com")
                    }
                    developer {
                        id.set("CCBlueX")
                        url.set("https://github.com/CCBlueX")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/CCBlueX/ultralight-java-reborn.git")
                    developerConnection.set("scm:git:ssh://github.com/CCBlueX/ultralight-java-reborn.git")
                    url.set("https://github.com/CCBlueX/ultralight-java-reborn")
                }
            }
        }
    }
}
