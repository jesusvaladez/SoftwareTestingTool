package org.apache.ambari.server.serveraction.kerberos;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
@javax.xml.bind.annotation.XmlEnum
public enum KDCType {

    @javax.xml.bind.annotation.XmlEnumValue("none")
    NONE,
    @javax.xml.bind.annotation.XmlEnumValue("mit-kdc")
    MIT_KDC,
    @javax.xml.bind.annotation.XmlEnumValue("active-directory")
    ACTIVE_DIRECTORY,
    @javax.xml.bind.annotation.XmlEnumValue("ipa")
    IPA;
    public static org.apache.ambari.server.serveraction.kerberos.KDCType translate(java.lang.String value) {
        if ((value == null) || value.isEmpty()) {
            return org.apache.ambari.server.serveraction.kerberos.KDCType.NONE;
        } else {
            return org.apache.ambari.server.serveraction.kerberos.KDCType.valueOf(value.replace("-", "_").toUpperCase());
        }
    }
}