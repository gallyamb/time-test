package org.time.test.core.test;

import java.util.function.Supplier;

@FunctionalInterface
public interface TrashSupplier<T> extends Supplier<T> {
    @Override
    default T get() {
        try {
            return getWithExceptions();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw Exceptions.sneakyThrow(ex);
        }
    }

    T getWithExceptions() throws Exception;
}
