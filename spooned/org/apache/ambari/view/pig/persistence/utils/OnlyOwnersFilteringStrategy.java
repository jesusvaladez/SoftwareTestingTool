package org.apache.ambari.view.pig.persistence.utils;
public class OnlyOwnersFilteringStrategy implements org.apache.ambari.view.pig.persistence.utils.FilteringStrategy {
    private final java.lang.String username;

    public OnlyOwnersFilteringStrategy(java.lang.String username) {
        this.username = username;
    }

    @java.lang.Override
    public boolean isConform(org.apache.ambari.view.pig.persistence.utils.Indexed item) {
        org.apache.ambari.view.pig.persistence.utils.Owned object = ((org.apache.ambari.view.pig.persistence.utils.Owned) (item));
        return object.getOwner().compareTo(username) == 0;
    }
}