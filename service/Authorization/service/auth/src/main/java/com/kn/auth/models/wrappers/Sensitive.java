package com.kn.auth.models.wrappers;

import com.kn.auth.annotations.SensetiveData;

public class Sensitive<T> implements Cloneable {
    public T safeReturn() {
        try {
            T copy = (T) this.clone();

            // Set fields marked with @SafeToNull to null
            for (java.lang.reflect.Field field : copy.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(SensetiveData.class)) {
                    field.setAccessible(true);
                    field.set(copy, null);
                }
            }
            return copy;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cloning not supported or error setting fields to null", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected T clone() {
        try {
            return (T) super.clone();
        } catch (CloneNotSupportedException e) {
            // Handle the exception here if needed
            return null;
        }
    }
}
