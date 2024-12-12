package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class ServiceOsSpecific {
    private java.lang.String osFamily;

    private org.apache.ambari.server.state.ServiceOsSpecific.Repo repo;

    public ServiceOsSpecific() {
    }

    public ServiceOsSpecific(java.lang.String osFamily) {
        this.osFamily = osFamily;
    }

    @javax.xml.bind.annotation.XmlElementWrapper(name = "packages")
    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "package"))
    private java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> packages = new java.util.ArrayList<>();

    public java.lang.String getOsFamily() {
        return osFamily;
    }

    public org.apache.ambari.server.state.ServiceOsSpecific.Repo getRepo() {
        return repo;
    }

    public java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> getPackages() {
        return packages;
    }

    public void addPackages(java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> packages) {
        this.packages.addAll(packages);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.ServiceOsSpecific that = ((org.apache.ambari.server.state.ServiceOsSpecific) (o));
        if (osFamily != null ? !osFamily.equals(that.osFamily) : that.osFamily != null)
            return false;

        if (packages != null ? !packages.equals(that.packages) : that.packages != null)
            return false;

        if (repo != null ? !repo.equals(that.repo) : that.repo != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (osFamily != null) ? osFamily.hashCode() : 0;
        result = (31 * result) + (repo != null ? repo.hashCode() : 0);
        result = (31 * result) + (packages != null ? packages.hashCode() : 0);
        return result;
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
    public static class Repo {
        @com.google.gson.annotations.SerializedName("baseUrl")
        private java.lang.String baseurl;

        @com.google.gson.annotations.SerializedName("mirrorsList")
        private java.lang.String mirrorslist;

        @com.google.gson.annotations.SerializedName("repoId")
        private java.lang.String repoid;

        @com.google.gson.annotations.SerializedName("repoName")
        private java.lang.String reponame;

        @com.google.gson.annotations.SerializedName("distribution")
        private java.lang.String distribution;

        @com.google.gson.annotations.SerializedName("components")
        private java.lang.String components;

        private Repo() {
        }

        public java.lang.String getBaseUrl() {
            return (null == baseurl) || baseurl.isEmpty() ? null : baseurl;
        }

        public java.lang.String getMirrorsList() {
            return (null == mirrorslist) || mirrorslist.isEmpty() ? null : mirrorslist;
        }

        public java.lang.String getRepoId() {
            return repoid;
        }

        public java.lang.String getRepoName() {
            return reponame;
        }

        public java.lang.String getDistribution() {
            return distribution;
        }

        public java.lang.String getComponents() {
            return components;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.state.ServiceOsSpecific.Repo repo = ((org.apache.ambari.server.state.ServiceOsSpecific.Repo) (o));
            if (baseurl != null ? !baseurl.equals(repo.baseurl) : repo.baseurl != null)
                return false;

            if (mirrorslist != null ? !mirrorslist.equals(repo.mirrorslist) : repo.mirrorslist != null)
                return false;

            if (repoid != null ? !repoid.equals(repo.repoid) : repo.repoid != null)
                return false;

            if (reponame != null ? !reponame.equals(repo.reponame) : repo.reponame != null)
                return false;

            if (distribution != null ? !distribution.equals(repo.distribution) : repo.distribution != null)
                return false;

            if (components != null ? !components.equals(repo.components) : repo.components != null)
                return false;

            return true;
        }

        @java.lang.Override
        public int hashCode() {
            int result = (baseurl != null) ? baseurl.hashCode() : 0;
            result = (31 * result) + (mirrorslist != null ? mirrorslist.hashCode() : 0);
            result = (31 * result) + (repoid != null ? repoid.hashCode() : 0);
            result = (31 * result) + (reponame != null ? reponame.hashCode() : 0);
            result = (31 * result) + (distribution != null ? distribution.hashCode() : 0);
            result = (31 * result) + (components != null ? components.hashCode() : 0);
            return result;
        }
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
    public static class Package {
        private java.lang.String name;

        private java.lang.String condition = "";

        private java.lang.Boolean skipUpgrade = java.lang.Boolean.FALSE;

        public java.lang.String getName() {
            return name;
        }

        public void setName(java.lang.String name) {
            this.name = name;
        }

        public java.lang.String getCondition() {
            return condition;
        }

        public void setCondition(java.lang.String condition) {
            this.condition = condition;
        }

        public java.lang.Boolean getSkipUpgrade() {
            return skipUpgrade;
        }

        public void setSkipUpgrade(java.lang.Boolean skipUpgrade) {
            this.skipUpgrade = skipUpgrade;
        }

        public Package() {
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.state.ServiceOsSpecific.Package aPackage = ((org.apache.ambari.server.state.ServiceOsSpecific.Package) (o));
            if (!name.equals(aPackage.name))
                return false;

            if (!skipUpgrade.equals(aPackage.skipUpgrade))
                return false;

            if (!condition.equals(aPackage.condition))
                return false;

            return true;
        }

        @java.lang.Override
        public int hashCode() {
            int result = name.hashCode();
            result = (31 * result) + skipUpgrade.hashCode();
            result = (31 * result) + condition.hashCode();
            return result;
        }
    }
}