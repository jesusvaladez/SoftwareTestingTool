package org.apache.ambari.server.state.repository;
import javax.xml.bind.annotation.XmlAttribute;
public class ManifestService {
    @javax.xml.bind.annotation.XmlAttribute(name = "id")
    public java.lang.String serviceId;

    @javax.xml.bind.annotation.XmlAttribute(name = "name")
    public java.lang.String serviceName;

    @javax.xml.bind.annotation.XmlAttribute(name = "version")
    public java.lang.String version;

    @javax.xml.bind.annotation.XmlAttribute(name = "version-id")
    public java.lang.String versionId;

    @javax.xml.bind.annotation.XmlAttribute(name = "release-version")
    public java.lang.String releaseVersion;
}