package io.github.skywalkerdarren.simpleaccounting.model.entity

import com.google.gson.annotations.Expose
import java.io.Serializable

data class CurrenciesInfo @JvmOverloads constructor(
        @Expose val success: String? = null,
        @Expose val timestamp: String? = null,
        @Expose val quotes: List<Currency>? = null,
        @Expose val error: Map<String, String>? = null
) : Serializable