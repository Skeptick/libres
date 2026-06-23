@file:Suppress("FunctionName")

package io.github.skeptick.libres.plugin.declarations

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.skeptick.libres.plugin.models.ResourcesSettings
import io.github.skeptick.libres.plugin.models.TextResource
import io.github.skeptick.libres.plugin.models.className

/**
 * ```
 * public interface Strings {
 *
 *   public val simple_string: String?
 *
 *   public val format_string: LibresFormatFormatString?
 *
 *   public val plural_string: LibresFormatPluralString?
 *
 * }
 * ```
 */
internal fun StringsInterface(
    settings: ResourcesSettings,
    resources: List<TextResource>
): FileSpec {
    return FileSpec.builder(settings.stringsPackageName, "Strings")
        .addType(
            TypeSpec.interfaceBuilder("Strings")
                .addProperties(resources.map {
                    PropertySpec.builder(it.name, it.className(settings).copy(nullable = true)).build()
                })
                .build()
        )
        .build()
}