package org.apache.ambari.server.utils;
public class Assertions {
    public static <T extends java.lang.Exception> T assertThrows(java.lang.Class<T> expectedException, java.lang.Runnable code) {
        try {
            code.run();
        } catch (java.lang.Exception e) {
            if (expectedException.isInstance(e)) {
                return expectedException.cast(e);
            }
            throw new java.lang.AssertionError(((("Expected exception: " + expectedException) + " but ") + e.getClass()) + " was thrown instead", e);
        }
        throw new java.lang.AssertionError(("Expected exception: " + expectedException) + ", but was not thrown");
    }
}