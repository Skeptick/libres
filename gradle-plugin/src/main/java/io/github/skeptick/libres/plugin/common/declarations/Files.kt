@file:Suppress("FunctionName")

package io.github.skeptick.libres.plugin.common.declarations

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File

internal fun ResourcesFile(
    packageName: String,
    name: String,
    resourcesObjectBuilder: TypeSpec.Builder
): FileSpec {
    return FileSpec.builder(packageName, name)
        .addType(resourcesObjectBuilder.build())
        .build()
}

internal fun FileSpec.saveToDirectory(directory: File) {
    directory.mkdirs()
    File(directory, "$name.${if (isScript) "kts" else "kt"}").writer().use(::writeTo)
}