## Changing Localization

As mentioned in the [README](../README.md#strings), the language to which the strings belong
is indicated in the file name before the extension (e.g. `strings_en.xml`).

During the application runtime, the localization selection is based on the system settings
and changing the language should also be done at the application instance level.

Examples of setting English language for all supported platforms:

**Android & JVM**
```kotlin
Locale.setDefault(Locale("en"))
```
> **Note**
> For Android, don't forget to update the configuration as well.

**iOS & MacOS**
```swift
UserDefaults.standard.set(["en"], forKey: "AppleLanguages")
```
> **Note**
> All used localizations must be declared in XCode project settings.

**JS**

In JS, there is no such possibility to change the language (without changing the language in the operating system or browser),
so the best option is to use `LibresSettings`:

```kotlin
LibresSettings.languageCode = "en"
```

This solution will also work for all other platforms but remember that this setting applies
only to the selection of localization for strings in Libres, and some
system components may remain untranslated.
> **Warning**
> The value in `LibresSettings.languageCode` has higher priority than system settings.