package com.gchristov.newsfeed.multiplatform.common.kotlin

import kotlinx.serialization.json.Json

sealed class JsonSerializer {
    abstract val json: Json

    data object Default : JsonSerializer() {
        override val json: Json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            explicitNulls = false
        }
    }

    data object ExplicitNulls : JsonSerializer() {
        override val json: Json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            explicitNulls = true
        }
    }
}