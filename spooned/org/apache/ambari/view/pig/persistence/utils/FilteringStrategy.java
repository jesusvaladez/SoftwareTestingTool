package org.apache.ambari.view.pig.persistence.utils;
public interface FilteringStrategy {
    boolean isConform(org.apache.ambari.view.pig.persistence.utils.Indexed item);
}