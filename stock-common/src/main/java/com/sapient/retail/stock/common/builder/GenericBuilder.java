package com.sapient.retail.stock.common.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Generic Builder class for using Build patterns for creating Objects.
 *
 * @param <T> the object that needs to be built.
 */
public class GenericBuilder<T> {

    private final Supplier<T> instantiator;

    private List<Consumer<T>> instanceModifiers = new ArrayList<>();

    private GenericBuilder(Supplier<T> instantiator) {
        this.instantiator = instantiator;
    }

    /**
     * Method to instantiate object to be built.
     *
     * @param instantiator the target object instantiate method
     * @param <T>          the target Object
     * @return target class instance
     */
    public static <T> GenericBuilder<T> of(Supplier<T> instantiator) {
        return new GenericBuilder<>(instantiator);
    }

    /**
     * Method to be used to build the target object.
     *
     * @param consumer the target object property method
     * @param value    the target object property value
     * @param <U>
     * @return
     */
    public <U> GenericBuilder<T> with(BiConsumer<T, U> consumer, U value) {
        Consumer<T> c = instance -> consumer.accept(instance, value);
        instanceModifiers.add(c);
        return this;
    }

    /**
     * Method to build the target object.
     *
     * @return the built target object instance
     */
    public T build() {
        T value = instantiator.get();
        instanceModifiers.forEach(modifier -> modifier.accept(value));
        instanceModifiers.clear();
        return value;
    }
}