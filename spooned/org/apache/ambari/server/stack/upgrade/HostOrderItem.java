package org.apache.ambari.server.stack.upgrade;
import org.apache.commons.lang.StringUtils;
public class HostOrderItem {
    public enum HostOrderActionType {

        SERVICE_CHECK,
        HOST_UPGRADE;}

    private final org.apache.ambari.server.stack.upgrade.HostOrderItem.HostOrderActionType m_type;

    private final java.util.List<java.lang.String> m_actionItems;

    public HostOrderItem(org.apache.ambari.server.stack.upgrade.HostOrderItem.HostOrderActionType type, java.util.List<java.lang.String> actionItems) {
        m_type = type;
        m_actionItems = actionItems;
    }

    public org.apache.ambari.server.stack.upgrade.HostOrderItem.HostOrderActionType getType() {
        return m_type;
    }

    public java.util.List<java.lang.String> getActionItems() {
        return m_actionItems;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("type", m_type).add("items", org.apache.commons.lang.StringUtils.join(m_actionItems, ", ")).omitNullValues().toString();
    }
}