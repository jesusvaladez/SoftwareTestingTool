package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class EncryptionKeyUpdateEvent extends org.apache.ambari.server.events.STOMPEvent {
    private final java.lang.String encryptionKey;

    public EncryptionKeyUpdateEvent(java.lang.String encryptionKey) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.ENCRYPTION_KEY_UPDATE);
        this.encryptionKey = encryptionKey;
    }

    public java.lang.String getEncryptionKey() {
        return encryptionKey;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.events.EncryptionKeyUpdateEvent that = ((org.apache.ambari.server.events.EncryptionKeyUpdateEvent) (o));
        return java.util.Objects.equals(encryptionKey, that.encryptionKey);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(encryptionKey);
    }
}