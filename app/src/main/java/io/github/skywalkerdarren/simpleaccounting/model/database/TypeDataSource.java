package io.github.skywalkerdarren.simpleaccounting.model.database;

import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;

public interface TypeDataSource {

    void getType(UUID uuid, LoadTypeCallBack callBack);

    void getTypes(boolean isExpense, LoadTypesCallBack callBack);

    void delType(UUID uuid);

    interface LoadTypeCallBack {
        void onTypeLoaded(Type type);
    }

    interface LoadTypesCallBack {
        void onTypesLoaded(List<Type> types);
    }
}
