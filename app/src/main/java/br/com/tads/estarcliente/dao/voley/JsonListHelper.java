package br.com.tads.estarcliente.dao.voley;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class JsonListHelper<T> implements ParameterizedType {

    private Class<?> wrapped;

    public JsonListHelper(Class<T> wrapped) {
        this.wrapped = wrapped;
    }

    public Type[] getActualTypeArguments() {
        return new Type[] {wrapped};
    }

    public Type getRawType() {
        return List.class;
    }

    public Type getOwnerType() {
        return null;
    }

}
