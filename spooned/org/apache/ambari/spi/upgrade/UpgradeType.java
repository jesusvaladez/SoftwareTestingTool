package org.apache.ambari.spi.upgrade;
import javax.xml.bind.annotation.XmlEnumValue;
public enum UpgradeType {

    @javax.xml.bind.annotation.XmlEnumValue("ROLLING")
    @com.google.gson.annotations.SerializedName("rolling_upgrade")
    ROLLING,
    @javax.xml.bind.annotation.XmlEnumValue("NON_ROLLING")
    @com.google.gson.annotations.SerializedName("nonrolling_upgrade")
    NON_ROLLING,
    @javax.xml.bind.annotation.XmlEnumValue("HOST_ORDERED")
    @com.google.gson.annotations.SerializedName("host_ordered_upgrade")
    HOST_ORDERED;}