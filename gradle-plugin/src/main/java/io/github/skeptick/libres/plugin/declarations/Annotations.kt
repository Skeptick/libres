@file:Suppress("FunctionName")

package io.github.skeptick.libres.plugin.declarations

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import io.github.skeptick.libres.InternalLibresApi

private val OptIn = ClassName("kotlin", "OptIn")

private val ExperimentalObjCName = ClassName("kotlin.experimental", "ExperimentalObjCName")

private val ObjCName = ClassName("kotlin.native", "ObjCName")

internal val InternalLibresApiAnnotation = AnnotationSpec.builder(OptIn)
    .addMember("%T::class", InternalLibresApi::class.asClassName())
    .build()

internal val ExperimentalObjCNameAnnotation = AnnotationSpec.builder(OptIn)
    .addMember("%T::class", ExperimentalObjCName)
    .build()

internal fun ObjCNameAnnotation(name: String) = AnnotationSpec.builder(ObjCName)
    .addMember("name = %S", name)
    .build()