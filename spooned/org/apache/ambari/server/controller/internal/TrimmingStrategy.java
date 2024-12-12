package org.apache.ambari.server.controller.internal;
public enum TrimmingStrategy {

    DEFAULT() {
        @java.lang.Override
        public java.lang.String trim(java.lang.String stringToTrim) {
            return stringToTrim.trim();
        }
    },
    DIRECTORIES() {
        @java.lang.Override
        public java.lang.String trim(java.lang.String stringToTrim) {
            return stringToTrim.replaceAll("\\s*,+\\s*", ",").trim();
        }
    },
    PASSWORD() {
        @java.lang.Override
        public java.lang.String trim(java.lang.String stringToTrim) {
            return stringToTrim;
        }
    },
    DELETE_SPACES_AT_END() {
        @java.lang.Override
        public java.lang.String trim(java.lang.String stringToTrim) {
            if (" ".equals(stringToTrim)) {
                return stringToTrim;
            }
            return stringToTrim.replaceAll("\\s+$", "");
        }
    };
    public abstract java.lang.String trim(java.lang.String stringToTrim);
}