package io.github.skeptick.libres.plurals.plugin.extensions

import com.squareup.kotlinpoet.FunSpec

internal fun FunSpec.Builder.addStatements(statements: Iterable<String>): FunSpec.Builder =
    apply {
        statements.forEach(::addStatement)
    }
