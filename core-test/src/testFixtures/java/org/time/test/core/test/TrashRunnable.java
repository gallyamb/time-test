package org.time.test.core.test;

@FunctionalInterface
public interface TrashRunnable extends Runnable {
    @Override
    default void run() {
        try {
            runWithExceptions();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw Exceptions.sneakyThrow(ex);
        }
    }

    void runWithExceptions() throws Exception;
}
