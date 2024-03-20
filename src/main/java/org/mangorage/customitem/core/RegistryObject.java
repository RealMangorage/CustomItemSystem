package org.mangorage.customitem.core;

import java.util.function.Supplier;

public final class RegistryObject<T> implements Supplier<T> {
    public static <T> RegistryObject<T> create(String ID, T object) {
        return new RegistryObject<>(ID, object);
    }

    private final String ID;
    private final T object;
    public RegistryObject(String ID, T object) {
        this.ID = ID;
        this.object = object;
    }

    public String getID() {
        return ID;
    }

    public T get() {
        return object;
    }
}
