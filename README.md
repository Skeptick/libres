# Libres

Resources generation in Kotlin Multiplatform.

## Setup

```kotlin
// build.gradle.kts (project)

buildscript {
    dependencies {
        classpath("io.github.skeptick.libres:gradle-plugin:1.2.0-beta01")
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

## Jetpack Compose

```kotlin
// build.gradle.kts (module)

kotlin {
    commonMain {
        dependencies {
            implementation("io.github.skeptick.libres:libres-compose:1.2.0-beta01")
        }
    }
}
```

This artifact provides `painterResource` function that can be used
to get `Painter` from `io.github.skeptick.libres.Image` in common code.

## Supported platforms

- **Android**, **JVM**, **iOS**, **MacOS** and **JS** in Kotlin Multiplatform projects.  
- Pure Android or JVM projects with Kotlin.

## Requirements

|                       |                               |
|-----------------------|-------------------------------|
| Kotlin                | 1.6.20+                       |
| Android               | 4.1+ (API level 16)           |
| Android Gradle Plugin | 7.0+                          |
| iOS                   | 13+ (when using vector icons) |

\
:bangbang: Also you need to use [CocoaPods](https://cocoapods.org/) and 
[CocoaPods Gradle plugin](https://kotlinlang.org/docs/native-cocoapods-dsl-reference.html) 
to export images to iOS application.  
If you aren't reusing images in iOS then this condition is optional.

## Known issues

### iOS
:warning: Cocoapods caches list of resource directories when `pod install` is called,
so bundles directory must exist at this time.
You won't see this issue if your `.podspec` is in `.gitignore`
in any case it's best to be safe with this hook in your Podfile.

Suppose your Kotlin Framework is added like this:
```ruby
pod 'umbrella', :path => '../common/umbrella/umbrella.podspec'
```

Then this hook will look like this:
```ruby
pre_install do |installer|
  FileUtils.mkdir_p(installer.sandbox.root.to_s + '/../../common/umbrella/build/generated/libres/apple/libres-bundles')
end
```

## Usage

Resources must be stored in `{yourSourceSetName}/libres`

Multiplatform:
```
├── commonMain
│   ├── kotlin
│   └── libres
│       ├── images
│       │   ├── vector_image.svg
│       │   └── raster_image.png
│       └── strings
│           ├── strings_en.xml
│           └── strings_ru.xml
```

Android or JVM:
```
├── main
│   ├── java
│   └── libres
│       ├── images
│       │   ├── vector_image.svg
│       │   └── raster_image.png
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

### Images

Supported formats:
- PNG
- JPG
- WEBP (Android 4.3+, iOS 14+)
- SVG (iOS 13+)

For Android SVGs are converted to 
[vector drawable](https://developer.android.com/develop/ui/views/graphics/vector-drawable-resources), 
for other platforms they copied as is.

#### Image modifiers
The image filename can contain modifiers in parentheses listed through underscores.

> **orig**
> 
> Used for iOS. Marks image as `Original` (similar to `Render As: Original Image` in XCode Assets Manager). 
Recommended for multicolor images.

> **size**
> 
> Applies to bitmaps. Reduces image resolution to specified size.
> - For Android generate images from mdpi to xxxhdpi (where mdpi is 1:1 in pixels to specified size)
> - For iOS generate images from @1x to @3x (where @1x is 1:1 in pixels to specified size)
> - For JVM and JS a single image of specified size is generated.

Filename examples:
```
some_hd_image_(100).jpg
app_logo_(orig).svg
my_colorful_bitmap_(orig)_(150).png
```
Kotlin:
```kotlin
MainRes.image.some_hd_image
MainRes.image.app_logo
MainRes.image.my_colorful_bitmap
```
Swift:
```swift
MainRes.shared.image.some_hd_image
// or MainRes.shared.image.someHdImage if `camelCaseNamesForAppleFramework` enabled
```

<details>
  <summary><h3>Why do I see a black/blue box instead of my picture?</h3></summary>

I'm pretty sure your picture is multicolor JPG/SVG with opaque background.  
This happens because UIKit recolors this image with accent color.

Solution: add (orig) modifier to image filename, e.g.: `my_image_(orig).png`
</details>

<details>
  <summary><h3>How to export bitmap from Figma?</h3></summary>

To obtain bundle of PNG images with different resolutions (mdpi-xxxhdpi for Android and @1x-@3x for iOS) do following steps:
1. Export image from Figma with x4 scale (it matches to the biggest used size — xxxhdpi on Android)
2. Put it to `libres/images` package
3. Remember the biggest side value of image represented in Figma
4. Rename image with the value of the biggest side:  
**pic.png** -> **pic_(orig)_({side_value}).png** or **pic_({side_value}).png**

Sample:
Image size in Figma is **240x89**. Final image name is **pic_(orig)_(240).png**
</details>
