# Libres

String resources generation in Kotlin Multiplatform.

> [!IMPORTANT]
> Starting from __version 2.0.0__, this plugin __no longer supports image sharing__.  
> If you want to continue using it for strings, you’ll need to migrate your image sharing to another solution —
> for example, [Compose Multiplatform Resources](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-resources.html).
>   
> __Why?__  
> I like my implementation for strings — having direct access to `String` is much more convenient than various wrappers in alternative solutions.  
> However, I’m not happy with my implementation for images: it turned out to be unintuitive, dependent on the CocoaPods plugin on iOS, and rather unstable.  
> I don’t have the time or motivation to maintain and improve this functionality, so I decided to remove it.

## Setup

```kotlin
// build.gradle.kts (project)

buildscript {
    dependencies {
        classpath("io.github.skeptick.libres:gradle-plugin:2.0.0-alpha02")
    }
}
```

```kotlin
// build.gradle.kts (module)

plugins {
    id("io.github.skeptick.libres")
}

libres {
    generatedClassName = "MainRes" // "Res" by default
    generateNamedArguments = true // false by default
    baseLocaleLanguageCode = "ru" // "en" by default
    camelCaseNamesForAppleFramework = false // false by default
}
```

## Supported platforms

- **Android**, **JVM**, **iOS**, **MacOS** and **JS** in Kotlin Multiplatform projects.  
- Pure Android or JVM projects with Kotlin.

## Known issues

## Usage

Resources must be stored in `{yourSourceSetName}/libres`

Multiplatform:
```
├── commonMain
│   ├── kotlin
│   └── libres
│       └── strings
│           ├── strings_en.xml
│           └── strings_ru.xml
```

Android or JVM:
```
├── main
│   ├── java
│   └── libres
│       └── strings
│           ├── strings_en.xml
│           └── strings_ru.xml
```

### Strings
Strings are stored in usual for Android form in `xml` files.  
The file postfix must contain language code for which these strings are intended. E.g.: `my_app_strings_en.xml`  
For each of languages you can create several files and they will be merged during compilation.
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="simple_string">Hello!</string>
    <string name="string_with_arguments">Hello ${name}!</string>
    <plurals name="plural_string">
        <item quantity="one">resource</item>
        <item quantity="other">resources</item>
    </plurals>
</resources>
```

Kotlin:
```kotlin
MainRes.string.simple_string
MainRes.string.string_with_arguments.format(name = "John")
MainRes.string.plural_string.format(5)
```
Swift:
```swift
MainRes.shared.string.simple_string
// or MainRes.shared.string.simpleString if `camelCaseNamesForAppleFramework` enabled
```
***
> **Note**
> In this example `MainRes.string.simple_string` will return a string, 
> so for better localization support it's not recommended to store the value. 
> Get it directly at the place where it's displayed in UI. 
> This seems familiar but ability to work directly with strings instead of resource IDs can be misused.
***
#### [More about localization](docs/LOCALIZATION.md)
