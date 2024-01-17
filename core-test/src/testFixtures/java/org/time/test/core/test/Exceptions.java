package org.time.test.core.test;

import java.util.Objects;

public class Exceptions {
    public static <E extends Throwable> E sneakyThrow(Throwable e) throws E {
        Objects.requireNonNull(e, "Provided exception is null");
        throw (E) e;
    }

    public static <T> T sneakyRethrow(TrashSupplier<T> action) {
        return action.get();
    }

    public static void sneakyRethrow(TrashRunnable action) {
        action.run();
    }

}
