package io.github.skeptick.libres.plugin.common.declarations

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

private val OptIn = ClassName("kotlin", "OptIn")

private val ExperimentalObjCName = ClassName("kotlin.experimental", "ExperimentalObjCName")

private val ObjCName = ClassName("kotlin.native", "ObjCName")

internal fun TypeSpec.Builder.addExperimentalObjCNameAnnotation(condition: Boolean): TypeSpec.Builder {
    return if (!condition) this
    else addAnnotation(
        AnnotationSpec.builder(OptIn)
            .addMember("%T::class", ExperimentalObjCName)
            .build()
    )
}

internal inline fun PropertySpec.Builder.addObjCNameAnnotation(
    condition: Boolean,
    nameBuilder: () -> String
): PropertySpec.Builder {
    return if (!condition) this
    else addAnnotation(
        AnnotationSpec.builder(ObjCName)
            .addMember("name = %S", nameBuilder())
            .build()
    )
}