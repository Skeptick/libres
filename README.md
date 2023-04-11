# Libres

Resources generation in Kotlin Multiplatform.

## Setup

```kotlin
// build.gradle.kts (project)

buildscript {
    dependencies {
        classpath("io.github.skeptick.libres:gradle-plugin:1.1.8")
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
    camelCaseNamesForAppleFramework = true // false by default
}
```

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

Also you need to use [CocoaPods](https://cocoapods.org/) and 
[CocoaPods Gradle plugin](https://kotlinlang.org/docs/native-cocoapods-dsl-reference.html) 
to export images to iOS project.  
If you aren't reusing images in iOS then this condition is optional.

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
Strings are stored in the usual for Android form in `xml` files.  
The file postfix must contain the code of the language for which the strings are intended. E.g.: `my_app_strings_en.xml`  
For each of the languages you can create several files and they will be merged during compilation.
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
```
***
> **Note**
> In the example above `MainRes.string.simple_string` will return a string, 
> so for better localization support it's not recommended to store the value. 
> Get it directly at the place where it's displayed in the UI. 
> This seems familiar but the ability to work directly with strings instead of resource IDs can be misused.
***
#### [More about localization](docs/LOCALIZATION.md)

### Images

Supported formats:
- PNG
- JPG
- WEBP (Android 4.3+, iOS 14+)
- SVG (iOS 13+)

For Android SVG are converted to 
[vector drawable](https://developer.android.com/develop/ui/views/graphics/vector-drawable-resources), 
for other platforms they copied as is.

#### Image modifiers
The image filename can contain modifiers in parentheses listed through underscores.

> **orig**
> 
> Used for iOS. Marks the image as `Original` (similar to `Render As: Original Image` in the Assets Manager in XCode). 
Recommended for multicolor images.

> **size**
> 
> Applies to bitmaps. Reduces the image resolution to the specified size.
> - For Android images are generated from mdpi to xxxhdpi (where mdpi is 1:1 in pixels to the specified size)
> - For iOS images are generated from 1x to 3x (where 1x is 1:1 in pixels to the specified size)
> - For JVM and JS a single image of the specified size is generated.

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
MainRes.shared.image.some_hd_image // -> UIImage
```

<details>
  <summary>How to export an image from Figma</summary>

To obtain bundle of png images with different resolutions (e.g. mdpi - xxxhdpi on Android) do the following steps
1. Export image from Figma with x4 scale (it matches to the biggest used size (xxxhdpi on Android))
2. Put it to libres/images package
3. Remember the biggest side value of image represented in Figma
4. Rename image with the value of the biggest side: from **pic.png** to **pic_(orig)_({side_value})** or **pic_({side_value})**

Sample:
Image size in Figma is **240x89**. Final image name is **pic_(orig)_(240).png**
</details>

## Jetpack Compose

```kotlin
// build.gradle.kts (module)

kotlin {
    commonMain {
        dependencies {
            implementation("io.github.skeptick.libres:libres-compose:1.1.8")
        }
    }
}
```

This artifact provides `painterResource` function that can be used
to get `Painter` from `io.github.skeptick.libres.Image` in common code.

## Known issues

### iOS
⚠️ Add the following code to your Podfile to avoid issues with the release build of your iOS app:
```ruby
post_install do |installer|
 installer.pods_project.targets.each do |target|
  if target.respond_to?(:product_type) and target.product_type == "com.apple.product-type.bundle"
   target.build_configurations.each do |config|
     config.build_settings['CODE_SIGNING_ALLOWED'] = 'NO'
   end
  end
 end
end
```
***

### Android
⚠️ In AGP 7+ there is a bug due to which the directory with generated images
is not included in the build sourceset. Bug fixed in 8-beta.  
Workaround for versions 7+:
```
// build.gradle.kts (module)

android {
    ...
    ...
    
    sourceSets {
        named("main") {
            res.srcDir("build/generated/libres/android/resources")
        }
    }
}
```