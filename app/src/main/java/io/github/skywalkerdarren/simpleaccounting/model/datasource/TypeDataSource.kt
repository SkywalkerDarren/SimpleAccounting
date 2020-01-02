package io.github.skywalkerdarren.simpleaccounting.model.datasource

import io.github.skywalkerdarren.simpleaccounting.model.entity.Type
import java.util.*

interface TypeDataSource {
    fun getType(uuid: UUID, callBack: LoadTypeCallBack)
    fun getTypes(isExpense: Boolean, callBack: LoadTypesCallBack)
    fun delType(uuid: UUID)
    interface LoadTypeCallBack {
        fun onTypeLoaded(type: Type?)
    }

    interface LoadTypesCallBack {
        fun onTypesLoaded(types: List<Type>?)
    }
}