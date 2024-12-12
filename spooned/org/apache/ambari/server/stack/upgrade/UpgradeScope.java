package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
@javax.xml.bind.annotation.XmlEnum
public enum UpgradeScope {

    @javax.xml.bind.annotation.XmlEnumValue("COMPLETE")
    @com.google.gson.annotations.SerializedName("rolling_upgrade")
    COMPLETE,
    @javax.xml.bind.annotation.XmlEnumValue("PARTIAL")
    @com.google.gson.annotations.SerializedName("partial")
    PARTIAL,
    @javax.xml.bind.annotation.XmlEnumValue("ANY")
    @com.google.gson.annotations.SerializedName("any")
    ANY;}