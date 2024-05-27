package com.kn.auth.models.wrappers;

import com.kn.auth.annotations.SafeToUpdate;

import java.lang.reflect.Field;

public class SafeUpdate<T> extends Sensitive<T> {
    public T safeUpdate(T updatedObject) {
        T copy = this.clone();
        for (Field field : copy.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(SafeToUpdate.class)) {
                try {
                    Field updatedField = updatedObject.getClass().getDeclaredField(field.getName());
                    field.setAccessible(true);
                    updatedField.setAccessible(true);
                    Object value = updatedField.get(updatedObject);
                    field.set(copy, value);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return copy;
    }
}
