package io.github.skeptick.libres.plurals.plugin.generator

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val json by lazy {
    Json {
        ignoreUnknownKeys = true
    }
}

@Serializable
internal data class Plurals(
    @SerialName("supplemental") val supplemental: Supplemental
) {

    @Serializable
    data class Supplemental(
        @SerialName("plurals-type-cardinal") val cardinal: Map<String, PluralRules>
    )

    @Serializable
    data class PluralRules(
        @SerialName("pluralRule-count-zero") val zero: String? = null,
        @SerialName("pluralRule-count-one") val one: String? = null,
        @SerialName("pluralRule-count-two") val two: String? = null,
        @SerialName("pluralRule-count-few") val few: String? = null,
        @SerialName("pluralRule-count-many") val many: String? = null,
        @SerialName("pluralRule-count-other") val other: String? = null
    )

}

internal fun parseJson(jsonBody: String): Plurals {
    return json.decodeFromString<Plurals>(jsonBody)
}