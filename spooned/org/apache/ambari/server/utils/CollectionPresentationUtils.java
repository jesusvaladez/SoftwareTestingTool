package org.apache.ambari.server.utils;
public class CollectionPresentationUtils {
    public static boolean isStringPermutationOfCollection(java.lang.String input, java.util.Collection<java.lang.String> expected, java.lang.String delimeter, int trimFromStart, int trimFromEnd) {
        input = input.substring(trimFromStart, input.length() - trimFromEnd);
        java.util.List<java.lang.String> parts = new java.util.ArrayList<>(java.util.Arrays.asList(input.split(java.util.regex.Pattern.quote(delimeter))));
        for (java.lang.String part : expected) {
            if (parts.contains(part)) {
                parts.remove(part);
            }
        }
        return parts.isEmpty();
    }

    public static boolean isJsonsEquals(java.lang.String input1, java.lang.String input2) {
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        com.google.gson.JsonElement input1Parsed = parser.parse(input1);
        com.google.gson.JsonElement input2Parsed = parser.parse(input2);
        return input1Parsed.equals(input2Parsed);
    }

    @org.junit.Test
    public void testIsStringPermutationOfCollection() {
        java.lang.String input1 = "{\"foo\":\"bar\",\"foobar\":\"baz\"}";
        java.lang.String input2 = "{\"foobar\":\"baz\",\"foo\":\"bar\"}";
        java.lang.String input3 = "{\"fooba\":\"baz\",\"foo\":\"bar\"}";
        java.util.Set<java.lang.String> expected = new java.util.HashSet<>(java.util.Arrays.asList(new java.lang.String[]{ "\"foo\":\"bar\"", "\"foobar\":\"baz\"" }));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.CollectionPresentationUtils.isStringPermutationOfCollection(input1, expected, ",", 1, 1));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.CollectionPresentationUtils.isStringPermutationOfCollection(input2, expected, ",", 1, 1));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.CollectionPresentationUtils.isStringPermutationOfCollection(input3, expected, ",", 1, 1));
    }
}