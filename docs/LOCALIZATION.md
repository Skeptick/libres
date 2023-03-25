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
UserDefaults.standard.set("en", forKey: "AppleLanguages")
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

## Plural rules

In Android, the SDK solution is used to determine the plural form,
in JVM 3rd-party library is used. But in Apple this API is closed and in JS it's completely absent,
so to avoid burdening the library with heavy solutions, "out of the box" only rules are provided
for [some languages](../libres-core/src/appleAndJsMain/kotlin/io/github/skeptick/libres/strings/PluralRules.kt).

You can create a Pull Request with the required languages or define these values at runtime:

```kotlin
import io.github.skeptick.libres.strings.PluralRules
import io.github.skeptick.libres.strings.PluralForm

PluralRules["en"] = PluralRule { number ->
    when (number) {
        1 -> PluralForm.One
        else -> PluralForm.Other
    }
}
```

When defining rules you can refer to
[this document](https://unicode-org.github.io/cldr-staging/charts/37/supplemental/language_plural_rules.html).