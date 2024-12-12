package org.apache.ambari.server.agent.stomp;
import org.apache.commons.lang.StringUtils;
public abstract class AgentDataHolder<T extends org.apache.ambari.server.agent.stomp.dto.Hashable> {
    protected final java.util.concurrent.locks.ReentrantLock updateLock = new java.util.concurrent.locks.ReentrantLock();

    private static final com.fasterxml.jackson.databind.ObjectMapper MAPPER = new com.fasterxml.jackson.databind.ObjectMapper();

    static {
        MAPPER.addMixIn(org.apache.ambari.server.agent.stomp.dto.Hashable.class, org.apache.ambari.server.agent.stomp.dto.HashIgnoreMixIn.class);
        MAPPER.addMixIn(org.apache.ambari.server.events.AgentConfigsUpdateEvent.class, org.apache.ambari.server.agent.stomp.dto.HashAndTimestampIgnoreMixIn.class);
    }

    protected abstract T getEmptyData();

    protected void regenerateDataIdentifiers(T data) {
        data.setHash(getHash(data));
    }

    protected boolean isIdentifierValid(T data) {
        return org.apache.commons.lang.StringUtils.isNotEmpty(data.getHash());
    }

    protected java.lang.String getHash(T data) {
        return getHash(data, "");
    }

    protected java.lang.String getHash(T data, java.lang.String salt) {
        java.lang.String json = null;
        try {
            json = org.apache.ambari.server.agent.stomp.AgentDataHolder.MAPPER.writeValueAsString(data);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new java.lang.RuntimeException("Error during mapping message to calculate hash", e);
        }
        java.lang.String generatedPassword = null;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes("UTF-8"));
            byte[] bytes = md.digest(json.getBytes("UTF-8"));
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            for (byte b : bytes) {
                sb.append(java.lang.Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (java.security.NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}