package org.apache.ambari.server.state.stack;
class JsonOsFamilyEntry {
    @com.google.gson.annotations.SerializedName("extends")
    private java.lang.String extendsFamily;

    private java.util.Set<java.lang.String> distro;

    private java.util.Set<java.lang.String> versions;

    public java.lang.String getExtendsFamily() {
        return extendsFamily;
    }

    public void setExtendsFamily(java.lang.String extendsFamily) {
        this.extendsFamily = extendsFamily;
    }

    public java.util.Set<java.lang.String> getDistro() {
        return distro;
    }

    public void setDistro(java.util.Set<java.lang.String> distro) {
        this.distro = distro;
    }

    public java.util.Set<java.lang.String> getVersions() {
        return versions;
    }

    public void setVersions(java.util.Set<java.lang.String> versions) {
        this.versions = versions;
    }
}