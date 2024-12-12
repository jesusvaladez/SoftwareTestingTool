package org.apache.ambari.server.state.repository;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class Release {
    @javax.xml.bind.annotation.XmlElement(name = "type")
    public org.apache.ambari.spi.RepositoryType repositoryType;

    @javax.xml.bind.annotation.XmlElement(name = "stack-id")
    public java.lang.String stackId;

    @javax.xml.bind.annotation.XmlElement(name = "version")
    public java.lang.String version;

    @javax.xml.bind.annotation.XmlElement(name = "build")
    public java.lang.String build;

    @javax.xml.bind.annotation.XmlElement(name = "hotfix")
    public java.lang.String hotfix;

    @javax.xml.bind.annotation.XmlElement(name = "compatible-with")
    public java.lang.String compatibleWith;

    @javax.xml.bind.annotation.XmlElement(name = "release-notes")
    public java.lang.String releaseNotes;

    @javax.xml.bind.annotation.XmlElement(name = "display")
    public java.lang.String display;

    public java.lang.String getFullVersion(org.apache.ambari.spi.stack.StackReleaseVersion stackVersion) {
        return stackVersion.getFullVersion(new org.apache.ambari.spi.stack.StackReleaseInfo(version, hotfix, build));
    }

    public org.apache.ambari.spi.stack.StackReleaseInfo getReleaseInfo() {
        return new org.apache.ambari.spi.stack.StackReleaseInfo(version, hotfix, build);
    }
}