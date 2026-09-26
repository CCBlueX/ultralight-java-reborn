[Ultralight Discord](https://chat.ultralig.ht) | [Ultralight Upstream](https://github.com/Ultralight-ux/Ultralight)

# Ultralight Java Reborn

###### Complete rewrite of the original Java wrapper for the [Ultralight](https://ultralig.ht) web engine

# About

This project is a Java wrapper for the [Ultralight](https://ultralig.ht) web engine which focuses on providing a 
simple, easy-to-use, and lightweight API for embedding web content in Java applications.

## What is Ultralight?

Ultralight is a lightweight, cross-platform, standards-compliant HTML UI engine. It's not a full browser, it's
designed specifically for creating native applications with HTML, CSS, and JavaScript. Ultralight is written in C++
and meant to be embedded into native applications (on desktop or console platforms). It's a great solution for
projects that want a UI that runs off of HTML but doesn't want to deal with the complexity of embedding a full
browser like Chromium or WebKit.

## What makes Ultralight Java Reborn different from other solutions?

Other than solutions like JCEF and the JavaFX WebView, Ultralight is meant to be embedded into applications and
games. As such, it focuses on speed and integration with custom code. Ultralight Java Reborn is a wrapper around
the Ultralight C++ API.

## Contact

If you have any questions, feel free to join the [Ultralight Discord](https://chat.ultralig.ht) and ask in the
`#java` channel.

# Using the library

Releases are published to https://maven.ccbluex.net/releases.

## Gradle

Add the repository:
```kotlin
repositories {
    maven("https://maven.ccbluex.net/releases")
}
```

Add the dependencies:
```kotlin
dependencies {
    // Java libraries, these are needed for compilation and runtime
    implementation("net.ccbluex.ultralight:ultralight-java-reborn-core:0.1.0")
    implementation("net.ccbluex.ultralight:ultralight-java-reborn-platform-jni:0.1.0")

    // The native library, needed at runtime only (it contains no Java code)
    runtimeOnly("net.ccbluex.ultralight:ultralight-java-reborn-platform-jni:0.1.0:linux-x64")
    // or win-x64, mac-x64
}
```

## The Ultralight runtime

The Ultralight license only allows applications to pass Ultralight on to their users, so the natives contain
Ultralight Java Reborn's own library, not Ultralight itself. Ship the Ultralight runtime with your application:
the `bin/` and `resources/` directories of the SDK. Then point Ultralight Java Reborn at it before loading:

```java
PlatformEnvironmentOptionContainer options = new PlatformEnvironmentOptionContainer();
options.addOption(new PlatformEnvironmentOption<>(
        JniPlatformOptions.class,
        new JniPlatformOptions().ultralightDirectory(Paths.get("path/to/ultralight"))
));

PlatformEnvironment environment = PlatformEnvironment.load(options);
```

The `ujr.ultralightDirectory` system property and the `ULTRALIGHT_DIR` environment variable work as well.

| Ultralight Java Reborn | Ultralight |
|---|---|
| 0.1.0 | 1.3 (`c909371`) |

Ultralight 1.3's `libWebCore.so` requires an executable stack, which glibc 2.41 and newer refuse to load.

## Building

The build downloads the Ultralight SDK it targets. To build against another copy, pass the archive or an extracted
SDK:

```sh
./gradlew -Pujr.ultralightSdk=path/to/ultralight-sdk-c909371-linux-x64.7z assemble
```

To run the smoke test, also pass the runtime, for example the extracted SDK: `-Pujr.ultralightDirectory=path/to/sdk
:examples:ultralight-java-reborn-example-smoke:run`.

# Licensing

This wrapper is licensed under the LGPLv3 license. See the [LICENSE](LICENSE) file for more information.

**However, Ultralight itself requires a commercial license for use in commercial products.** See the
official [Ultralight website](https://ultralig.ht) for more information.

Moreover, some of the Jar files produced by this project contain the non-free proprietary binaries of Ultralight.

