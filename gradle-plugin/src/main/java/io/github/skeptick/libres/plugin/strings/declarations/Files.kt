@file:Suppress("FunctionName")

package io.github.skeptick.libres.plugin.strings.declarations

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.skeptick.libres.plugin.ResourcesPlugin
import io.github.skeptick.libres.strings.*

internal fun StringsInterfaceFile(packageName: String, stringsInterfaceBuilder: TypeSpec.Builder): FileSpec {
    return FileSpec.builder(packageName, "Strings")
        .addType(stringsInterfaceBuilder.build())
        .build()
}

internal fun StringsObjectFile(packageName: String, stringsObjectBuilder: TypeSpec.Builder): FileSpec {
    return stringsObjectBuilder.build().let { stringsObject ->
        FileSpec.builder(packageName, stringsObject.name!!)
            .addImport(ResourcesPlugin.STRINGS_PACKAGE_NAME, "getCurrentLanguageCode", "PluralForms")
            .addType(stringsObject)
            .build()
    }
}

internal fun FormattedClassesFile(packageName: String, classes: Iterable<TypeSpec.Builder>): FileSpec {
    return FileSpec.builder(packageName, "FormattedClasses")
        .addImport(ResourcesPlugin.STRINGS_PACKAGE_NAME, "formatString", "getPluralizedString")
        .addTypes(classes)
        .build()
}

private fun FileSpec.Builder.addTypes(types: Iterable<TypeSpec.Builder>): FileSpec.Builder {
    for (type in types) addType(type.build())
    return this
}
