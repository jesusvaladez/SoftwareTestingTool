package org.apache.ambari.funtest.server;
public enum AmbariUserRole {

    NONE,
    CLUSTER_USER() {
        @java.lang.Override
        public java.lang.String toString() {
            return "CLUSTER.USER";
        }
    },
    SERVICE_OPERATOR() {
        @java.lang.Override
        public java.lang.String toString() {
            return "SERVICE.OPERATOR";
        }
    },
    SERVICE_ADMINISTRATOR() {
        @java.lang.Override
        public java.lang.String toString() {
            return "SERVICE.ADMINISTRATOR";
        }
    },
    CLUSTER_OPERATOR() {
        @java.lang.Override
        public java.lang.String toString() {
            return "CLUSTER.OPERATOR";
        }
    },
    CLUSTER_ADMINISTRATOR() {
        @java.lang.Override
        public java.lang.String toString() {
            return "CLUSTER.ADMINISTRATOR";
        }
    },
    AMBARI_ADMINISTRATOR() {
        @java.lang.Override
        public java.lang.String toString() {
            return "AMBARI.ADMINISTRATOR";
        }
    };}