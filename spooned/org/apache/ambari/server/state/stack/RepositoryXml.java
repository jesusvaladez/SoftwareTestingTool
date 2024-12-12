package org.apache.ambari.server.state.stack;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
@javax.xml.bind.annotation.XmlRootElement(name = "reposinfo")
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class RepositoryXml implements org.apache.ambari.server.stack.Validable {
    private static final java.util.regex.Pattern HTTP_URL_PROTOCOL_PATTERN = java.util.regex.Pattern.compile("((http(s)*:\\/\\/))");

    @javax.xml.bind.annotation.XmlElement(name = "latest")
    private java.lang.String latestUri;

    @javax.xml.bind.annotation.XmlElement(name = "os")
    private java.util.List<org.apache.ambari.server.state.stack.RepositoryXml.Os> oses = new java.util.ArrayList<>();

    @javax.xml.bind.annotation.XmlTransient
    private boolean valid = true;

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

    public java.lang.String getLatestURI() {
        return latestUri;
    }

    public java.util.List<org.apache.ambari.server.state.stack.RepositoryXml.Os> getOses() {
        return oses;
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
    public static class Os {
        @javax.xml.bind.annotation.XmlAttribute(name = "family")
        private java.lang.String family;

        @javax.xml.bind.annotation.XmlElement(name = "package-version")
        private java.lang.String packageVersion;

        @javax.xml.bind.annotation.XmlElement(name = "repo")
        private java.util.List<org.apache.ambari.server.state.stack.RepositoryXml.Repo> repos;

        private Os() {
        }

        public java.lang.String getFamily() {
            return family;
        }

        public java.util.List<org.apache.ambari.server.state.stack.RepositoryXml.Repo> getRepos() {
            return repos;
        }

        public java.lang.String getPackageVersion() {
            return packageVersion;
        }
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
    public static class Repo {
        private java.lang.String baseurl = null;

        private java.lang.String mirrorslist = null;

        private java.lang.String repoid = null;

        private java.lang.String reponame = null;

        private java.lang.String distribution = null;

        private java.lang.String components = null;

        private boolean unique = false;

        @javax.xml.bind.annotation.XmlElementWrapper(name = "tags")
        @javax.xml.bind.annotation.XmlElement(name = "tag")
        private java.util.Set<org.apache.ambari.server.state.stack.RepoTag> tags = new java.util.HashSet<>();

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

        public boolean isUnique() {
            return unique;
        }

        public void setUnique(boolean unique) {
            this.unique = unique;
        }

        public java.util.Set<org.apache.ambari.server.state.stack.RepoTag> getTags() {
            return tags;
        }
    }

    public java.util.List<org.apache.ambari.server.state.RepositoryInfo> getRepositories() {
        return getRepositories(null);
    }

    public java.util.List<org.apache.ambari.server.state.RepositoryInfo> getRepositories(java.lang.String credentials) {
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> repos = new java.util.ArrayList<>();
        for (org.apache.ambari.server.state.stack.RepositoryXml.Os o : getOses()) {
            java.lang.String osFamily = o.getFamily();
            for (java.lang.String os : osFamily.split(",")) {
                for (org.apache.ambari.server.state.stack.RepositoryXml.Repo r : o.getRepos()) {
                    org.apache.ambari.server.state.RepositoryInfo ri = new org.apache.ambari.server.state.RepositoryInfo();
                    java.lang.String baseUrl = r.getBaseUrl();
                    if (!com.google.common.base.Strings.isNullOrEmpty(credentials)) {
                        java.util.regex.Matcher matcher = org.apache.ambari.server.state.stack.RepositoryXml.HTTP_URL_PROTOCOL_PATTERN.matcher(baseUrl);
                        baseUrl = matcher.replaceAll(("$1" + credentials) + "@");
                    }
                    ri.setBaseUrl(baseUrl);
                    ri.setDefaultBaseUrl(r.getBaseUrl());
                    ri.setMirrorsList(r.getMirrorsList());
                    ri.setOsType(os.trim());
                    ri.setRepoId(r.getRepoId());
                    ri.setRepoName(r.getRepoName());
                    ri.setDistribution(r.getDistribution());
                    ri.setComponents(r.getComponents());
                    ri.setUnique(r.isUnique());
                    ri.setTags(r.tags);
                    repos.add(ri);
                }
            }
        }
        return repos;
    }
}