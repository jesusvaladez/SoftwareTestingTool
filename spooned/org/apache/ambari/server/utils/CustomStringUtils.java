package org.apache.ambari.server.utils;
public class CustomStringUtils {
    public CustomStringUtils() {
        super();
    }

    public static boolean containsCaseInsensitive(java.lang.String s, java.util.List<java.lang.String> l) {
        for (java.lang.String listItem : l) {
            if (listItem.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public static void toLowerCase(java.util.List<java.lang.String> l) {
        java.util.ListIterator<java.lang.String> iterator = l.listIterator();
        while (iterator.hasNext()) {
            iterator.set(iterator.next().toLowerCase());
        } 
    }

    public static void toUpperCase(java.util.List<java.lang.String> l) {
        java.util.ListIterator<java.lang.String> iterator = l.listIterator();
        while (iterator.hasNext()) {
            iterator.set(iterator.next().toUpperCase());
        } 
    }
}