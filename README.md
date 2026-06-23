# Libres

Libres is a Gradle plugin that generates Kotlin accessors for localized string
resources. It is designed primarily for Kotlin Multiplatform projects, but it
can also be used in Android and Kotlin/JVM modules.

Generated accessors expose plain `String` values for simple resources and small
formatting helpers for resources with parameters. This keeps UI code close to
regular Kotlin string usage while still allowing locale-aware resource lookup.

> [!IMPORTANT]
> Libres 2.0.0 and newer support string resources only. Image resource sharing
> was removed. If your project used Libres for images, migrate those assets to a
> different solution, for example
> [Compose Multiplatform Resources](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-resources.html).
>
> The string API remains the focus of the plugin: it provides direct access to
> strings without forcing UI code through resource wrappers. The old image
> implementation depended on the CocoaPods plugin on iOS and was hard to keep
> stable across platforms, so it is no longer maintained.

## Installation

Add the Libres Gradle plugin artifact to the project buildscript:

```kotlin
// build.gradle.kts (project)

buildscript {
    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath("io.github.skeptick.libres:gradle-plugin:2.0.0-beta01")
    }
}
```

Apply the plugin in the module that owns your string resources:

```kotlin
// build.gradle.kts (module)

plugins {
    id("io.github.skeptick.libres")
}

libres {
    generatedClassName = "MainRes"
    generateNamedArguments = true
    baseLocaleTag = "en"
    generatedClassPackageName = "com.example.application"
    camelCaseNamesForAppleFramework = false
}
```

### Configuration

| Option                            | Default                                                                           | Description                                                                                                                                                                                              |
|-----------------------------------|-----------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `generatedClassName`              | `"Res"`                                                                           | Name of the generated root object.                                                                                                                                                                       |
| `generatedClassPackageName`       | Android namespace, or `"libres.resources"` when no Android namespace is available | Package for the generated root object.                                                                                                                                                                   |
| `generateNamedArguments`          | `false`                                                                           | When `true`, resources with template parameters get generated formatter classes with named `format(...)` arguments. When `false`, formatted resources use generic `format(vararg args: String)` helpers. |
| `baseLocaleTag`                   | `"en"`                                                                            | Locale that must contain every resource and acts as the final fallback.                                                                                                                                  |
| `camelCaseNamesForAppleFramework` | `false`                                                                           | Adds Swift/Objective-C camelCase names for generated string properties. Kotlin property names stay unchanged.                                                                                            |

## Supported Projects

Libres supports Kotlin Multiplatform projects and generates common source code
that can be used from all configured targets. The repository currently includes
Android, JVM, iOS, watchOS, tvOS, macOS, Linux, Windows, JavaScript, WasmJS, and
WasmWASI targets in its [setup](buildSrc/src/main/kotlin/multiplatform-setup.gradle.kts).

The plugin can also be applied to non-multiplatform Android and Kotlin/JVM
modules that use the standard `main` source set layout.

## Resource Location

For Kotlin Multiplatform projects, put string resources in `commonMain`:

```text
├── src/
│   └── commonMain/
│       ├── kotlin/
│       └── libres/
│           └── strings/
│               ├── strings_en.xml
│               ├── strings_en-US.xml
│               ├── strings_zh-Hans-CN.xml
│               └── strings_ru.xml
```

For Android or Kotlin/JVM modules, put them in `main`:

```text
├── src/
│   └── main/
│       ├── kotlin/
│       └── libres/
│           └── strings/
│               ├── strings_en.xml
│               ├── strings_en-US.xml
│               ├── strings_zh-Hans-CN.xml
│               └── strings_ru.xml
```

## XML Format

String files use an Android-like `<resources>` structure:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_title">Libres sample</string>
    <string name="welcome_message">Hello ${name}!</string>
    <plurals name="resource_count">
        <item quantity="one">${count} resource</item>
        <item quantity="other">${count} resources</item>
    </plurals>
</resources>
```

Supported resource tags:

- `<string name="...">...</string>`
- `<plurals name="...">...</plurals>` with `zero`, `one`, `two`, `few`, `many`, or `other` quantity items

### Locale File Names

The locale tag is parsed from the part of the file name after the last underscore:

```text
strings_en.xml
strings_en-US.xml
my_feature_strings_zh-Hans-CN.xml
```

Locale tags use the `language[-Script][-REGION]` BCP 47 shape:

- `language`: required, 2 or 3 letters, for example `en`, `ru`, `zh`
- `Script`: optional, 4 letters, for example `Hans`, `Cyrl`
- `REGION`: optional, 2 letters, for example `US`, `CN`

You can split one locale across multiple files. Files for the same locale are merged during generation.

Every resource must exist in the base locale selected by `baseLocaleTag`. Other
locales may omit resources; Libres falls back per resource at runtime.

### Template Parameters

Use Libres template parameters for formatted strings:

```xml
<string name="welcome_message">Hello ${name}!</string>
```

Each localized version of the same resource must use
the same set of parameter names as the base locale. The order may differ between locales.

Java/Android format specifiers such as `%s` and `%1$s` are not supported in XML
resources. Use `${name}` style parameters instead.

## Generated Accessors

With the configuration shown above, the generated root object is `MainRes`.
The examples below assume `generateNamedArguments = true`.

```kotlin
import com.example.application.MainRes

val title: String = MainRes.string.app_title
val welcome: String = MainRes.string.welcome_message.format(name = "John")
val count: String = MainRes.string.resource_count.format(number = 5, count = "5")
```

If `generateNamedArguments = false`, formatted resources use generic helpers:

```kotlin
MainRes.string.welcome_message.format("John")
MainRes.string.resource_count.format(5, "5")
```

From Swift, Kotlin objects are exposed through `shared`:

```swift
MainRes.shared.string.app_title
```

If `camelCaseNamesForAppleFramework = true`, generated string properties get
camelCase names in the Apple framework:

```swift
MainRes.shared.string.appTitle
```

> [!NOTE]
> Simple string accessors return the resolved `String`, not a long-lived resource
> wrapper. Read them close to where the UI is rendered. If you cache a returned
> string in your own state, your code is responsible for updating that cache when
> the app locale changes.

## Locale Selection

Libres chooses the current locale from the platform when possible:

| Platform                 | Default behavior                                                                              |
|--------------------------|-----------------------------------------------------------------------------------------------|
| Android                  | Reads the current Android locale when a resource is accessed.                                 |
| JVM                      | Reads `Locale.getDefault()` when a resource is accessed.                                      |
| Apple platforms          | Reads the app bundle preferred localization once at process start.                            |
| JS and WasmJS            | Reads `navigator.languages` / `navigator.language` once at application start.                 |
| Linux, Windows, WasmWASI | No platform locale is detected automatically; the base locale is used unless you provide one. |

Apple apps must declare supported localizations in Xcode project settings or in
the app bundle metadata. Otherwise Apple APIs may not report the expected app
localization to Libres.

You can override locale selection on any platform with `LibresSettings`:

```kotlin
import io.github.skeptick.libres.LibresLocale
import io.github.skeptick.libres.LibresSettings

LibresSettings.setLocaleProvider {
    LibresLocale(language = "en", script = null, region = null)
}
```

The provider has priority over the platform locale and is called when Libres
resolves resources, so it can read from your app settings or state holder.

At runtime, Libres first looks for the best same-language match for the current
locale and then falls back to the base locale. For example, `en-US` prefers an
exact `en-US` resource, then other suitable `en` resources, and finally the`baseLocaleTag` resource.
