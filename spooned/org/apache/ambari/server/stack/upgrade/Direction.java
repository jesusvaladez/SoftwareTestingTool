package org.apache.ambari.server.stack.upgrade;
import org.apache.commons.lang.StringUtils;
public enum Direction {

    UPGRADE,
    DOWNGRADE;
    public boolean isUpgrade() {
        return this == org.apache.ambari.server.stack.upgrade.Direction.UPGRADE;
    }

    public boolean isDowngrade() {
        return this == org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE;
    }

    public java.lang.String getText(boolean proper) {
        return proper ? org.apache.commons.lang.StringUtils.capitalize(name().toLowerCase()) : name().toLowerCase();
    }

    public java.lang.String getPast(boolean proper) {
        return getText(proper) + "d";
    }

    public java.lang.String getPlural(boolean proper) {
        return getText(proper) + "s";
    }

    public java.lang.String getVerb(boolean proper) {
        java.lang.String verb = (this == org.apache.ambari.server.stack.upgrade.Direction.UPGRADE) ? "upgrading" : "downgrading";
        return proper ? org.apache.commons.lang.StringUtils.capitalize(verb) : verb;
    }

    public java.lang.String getPreposition() {
        return this == org.apache.ambari.server.stack.upgrade.Direction.UPGRADE ? "to" : "from";
    }
}