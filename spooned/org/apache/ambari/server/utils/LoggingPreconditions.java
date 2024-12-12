package org.apache.ambari.server.utils;
public class LoggingPreconditions {
    private final org.slf4j.Logger logger;

    public LoggingPreconditions(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    public void checkNotNull(java.lang.Object o, java.lang.String errorMessage, java.lang.Object... messageParams) {
        if (o == null) {
            logAndThrow(java.lang.NullPointerException::new, errorMessage, messageParams);
        }
    }

    public void checkArgument(boolean expression, java.lang.String errorMessage, java.lang.Object... messageParams) {
        if (!expression) {
            logAndThrow(java.lang.IllegalArgumentException::new, errorMessage, messageParams);
        }
    }

    public void checkState(boolean expression, java.lang.String errorMessage, java.lang.Object... messageParams) {
        if (!expression) {
            logAndThrow(java.lang.IllegalStateException::new, errorMessage, messageParams);
        }
    }

    public <T> T wrapInUnchecked(java.lang.Exception exception, java.util.function.BiFunction<java.lang.String, java.lang.Exception, java.lang.RuntimeException> uncheckedWrapper, java.lang.String errorMessage, java.lang.Object... messageParams) {
        logAndThrow(msg -> uncheckedWrapper.apply(msg, exception), errorMessage, messageParams);
        return null;
    }

    public void logAndThrow(java.util.function.Function<java.lang.String, java.lang.RuntimeException> exceptionCreator, java.lang.String errorMessage, java.lang.Object... messageParams) {
        java.lang.String msg = java.lang.String.format(errorMessage, messageParams);
        logger.error(msg);
        throw exceptionCreator.apply(msg);
    }
}