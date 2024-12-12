package org.apache.ambari.server.state.stack;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
@javax.xml.bind.annotation.XmlRootElement(name = "metainfo")
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class ExtensionMetainfoXml implements org.apache.ambari.server.stack.Validable {
    @javax.xml.bind.annotation.XmlElement(name = "extends")
    private java.lang.String extendsVersion = null;

    @javax.xml.bind.annotation.XmlElement(name = "versions")
    private org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Version version = new org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Version();

    @javax.xml.bind.annotation.XmlElement(name = "prerequisites")
    private org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Prerequisites prerequisites = new org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Prerequisites();

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
    public static class Prerequisites {
        private Prerequisites() {
        }

        @javax.xml.bind.annotation.XmlElementWrapper(name = "min-stack-versions")
        @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "stack"))
        private java.util.List<org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Stack> stacks = new java.util.ArrayList<>();

        @javax.xml.bind.annotation.XmlElementWrapper(name = "min-extension-versions")
        @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "extension"))
        private java.util.List<org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Extension> extensions = new java.util.ArrayList<>();

        public java.util.List<org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Stack> getStacks() {
            return stacks;
        }

        public java.util.List<org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Extension> getExtensions() {
            return extensions;
        }
    }

    @javax.xml.bind.annotation.XmlTransient
    private boolean valid = true;

    @javax.xml.bind.annotation.XmlElement(name = "auto-link")
    private boolean autoLink = false;

    @java.lang.Override
    public boolean isValid() {
        return valid;
    }

    @java.lang.Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @javax.xml.bind.annotation.XmlTransient
    private java.util.Set<java.lang.String> errorSet = new java.util.HashSet<>();

    @java.lang.Override
    public void addError(java.lang.String error) {
        errorSet.add(error);
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getErrors() {
        return errorSet;
    }

    @java.lang.Override
    public void addErrors(java.util.Collection<java.lang.String> errors) {
        this.errorSet.addAll(errors);
    }

    public java.lang.String getExtends() {
        return extendsVersion;
    }

    public org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Version getVersion() {
        return version;
    }

    public java.util.List<org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Stack> getStacks() {
        return prerequisites.getStacks();
    }

    public java.util.List<org.apache.ambari.server.state.stack.ExtensionMetainfoXml.Extension> getExtensions() {
        return prerequisites.getExtensions();
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
    public static class Version {
        private Version() {
        }

        private boolean active = false;

        private java.lang.String upgrade = null;

        public boolean isActive() {
            return active;
        }

        public java.lang.String getUpgrade() {
            return upgrade;
        }
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
    public static class Stack {
        private Stack() {
        }

        private java.lang.String name = null;

        private java.lang.String version = null;

        public java.lang.String getName() {
            return name;
        }

        public java.lang.String getVersion() {
            return version;
        }
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
    public static class Extension {
        private Extension() {
        }

        private java.lang.String name = null;

        private java.lang.String version = null;

        public java.lang.String getName() {
            return name;
        }

        public java.lang.String getVersion() {
            return version;
        }
    }

    public boolean isAutoLink() {
        return autoLink;
    }

    public void setAutoLink(boolean autoLink) {
        this.autoLink = autoLink;
    }
}