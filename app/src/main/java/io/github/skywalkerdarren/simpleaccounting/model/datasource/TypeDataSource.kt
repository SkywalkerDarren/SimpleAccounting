package io.github.skywalkerdarren.simpleaccounting.model.datasource

import io.github.skywalkerdarren.simpleaccounting.model.entity.Type
import java.util.*

interface TypeDataSource {
    fun getType(uuid: UUID, callBack: (Type?) -> Unit)
    fun getTypes(isExpense: Boolean, callBack: (List<Type>?) -> Unit)
    fun delType(uuid: UUID)
}