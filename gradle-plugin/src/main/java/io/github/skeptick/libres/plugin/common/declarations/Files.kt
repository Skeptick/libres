@file:Suppress("FunctionName")

package io.github.skeptick.libres.plugin.common.declarations

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import org.gradle.api.file.Directory
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

internal fun FileSpec.saveTo(directory: Directory) {
    directory.asFile.let { file ->
        if (!file.exists()) file.mkdirs()
        File(file, "$name.${if (isScript) "kts" else "kt"}").writer().use(::writeTo)
    }
}