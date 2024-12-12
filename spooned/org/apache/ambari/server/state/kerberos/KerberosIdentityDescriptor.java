package org.apache.ambari.server.state.kerberos;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
public class KerberosIdentityDescriptor extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor {
    static final java.lang.String KEY_REFERENCE = "reference";

    static final java.lang.String KEY_PRINCIPAL = org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.PRINCIPAL.getDescriptorName();

    static final java.lang.String KEY_KEYTAB = org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.Type.KEYTAB.getDescriptorName();

    static final java.lang.String KEY_WHEN = "when";

    private java.lang.String reference = null;

    private org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principal = null;

    private org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytab = null;

    private org.apache.ambari.server.collections.Predicate when = null;

    private java.lang.String path = null;

    public KerberosIdentityDescriptor(java.lang.String name, java.lang.String reference, org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principal, org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytab, org.apache.ambari.server.collections.Predicate when) {
        setName(name);
        setReference(reference);
        setPrincipalDescriptor(principal);
        setKeytabDescriptor(keytab);
        setWhen(when);
    }

    public KerberosIdentityDescriptor(java.util.Map<?, ?> data) {
        setName(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getStringValue(data, "name"));
        setReference(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.getStringValue(data, org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_REFERENCE));
        if (data != null) {
            java.lang.Object item;
            item = data.get(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_PRINCIPAL);
            if (item instanceof java.util.Map) {
                setPrincipalDescriptor(new org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor(((java.util.Map<?, ?>) (item))));
            }
            item = data.get(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_KEYTAB);
            if (item instanceof java.util.Map) {
                setKeytabDescriptor(new org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor(((java.util.Map<?, ?>) (item))));
            }
            item = data.get(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_WHEN);
            if (item instanceof java.util.Map) {
                setWhen(org.apache.ambari.server.collections.PredicateUtils.fromMap(((java.util.Map<?, ?>) (item))));
            }
        }
    }

    public java.lang.String getReference() {
        return reference;
    }

    public java.lang.String getReferenceAbsolutePath() {
        java.lang.String absolutePath;
        if (org.apache.commons.lang.StringUtils.isEmpty(reference)) {
            absolutePath = getName();
        } else {
            absolutePath = reference;
        }
        if ((!org.apache.commons.lang.StringUtils.isEmpty(absolutePath)) && (!absolutePath.startsWith("/"))) {
            java.lang.String path = getPath();
            if (path == null) {
                path = "";
            }
            if (absolutePath.startsWith("..")) {
                org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor parent = getParent();
                if (parent != null) {
                    parent = parent.getParent();
                    if (parent != null) {
                        absolutePath = absolutePath.replace("..", parent.getPath());
                    }
                }
            } else if (absolutePath.startsWith(".")) {
                org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor parent = getParent();
                if (parent != null) {
                    absolutePath = absolutePath.replace(".", parent.getPath());
                }
            }
        }
        return absolutePath;
    }

    public void setReference(java.lang.String reference) {
        this.reference = reference;
    }

    public org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor getPrincipalDescriptor() {
        return principal;
    }

    public void setPrincipalDescriptor(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principal) {
        this.principal = principal;
        if (this.principal != null) {
            this.principal.setParent(this);
        }
    }

    public org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor getKeytabDescriptor() {
        return keytab;
    }

    public void setKeytabDescriptor(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytab) {
        this.keytab = keytab;
        if (this.keytab != null) {
            this.keytab.setParent(this);
        }
    }

    public org.apache.ambari.server.collections.Predicate getWhen() {
        return when;
    }

    public void setWhen(org.apache.ambari.server.collections.Predicate when) {
        this.when = when;
    }

    public boolean shouldInclude(java.util.Map<java.lang.String, java.lang.Object> context) {
        return (this.when == null) || this.when.evaluate(context);
    }

    public void update(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor updates) {
        if (updates != null) {
            setName(updates.getName());
            setReference(updates.getReference());
            org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor existingPrincipal = getPrincipalDescriptor();
            if (existingPrincipal == null) {
                setPrincipalDescriptor(updates.getPrincipalDescriptor());
            } else {
                existingPrincipal.update(updates.getPrincipalDescriptor());
            }
            org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor existingKeytabDescriptor = getKeytabDescriptor();
            if (existingKeytabDescriptor == null) {
                setKeytabDescriptor(updates.getKeytabDescriptor());
            } else {
                existingKeytabDescriptor.update(updates.getKeytabDescriptor());
            }
            org.apache.ambari.server.collections.Predicate updatedWhen = updates.getWhen();
            if (updatedWhen != null) {
                setWhen(updatedWhen);
            }
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.Object> toMap() {
        java.util.Map<java.lang.String, java.lang.Object> dataMap = super.toMap();
        if (reference != null) {
            dataMap.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_REFERENCE, reference);
        }
        if (principal != null) {
            dataMap.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_PRINCIPAL, principal.toMap());
        }
        if (keytab != null) {
            dataMap.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_KEYTAB, keytab.toMap());
        }
        if (when != null) {
            dataMap.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_WHEN, org.apache.ambari.server.collections.PredicateUtils.toMap(when));
        }
        return dataMap;
    }

    public com.google.common.base.Optional<java.lang.String> getReferencedServiceName() {
        return parseServiceName(reference).or(parseServiceName(getName()));
    }

    private com.google.common.base.Optional<java.lang.String> parseServiceName(java.lang.String name) {
        if (((name != null) && name.startsWith("/")) && (name.split("/").length > 2)) {
            return com.google.common.base.Optional.of(name.split("/")[1]);
        } else {
            return com.google.common.base.Optional.absent();
        }
    }

    public boolean isShared(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor that) {
        return hasSamePrincipal(that) || hasSameKeytab(that);
    }

    private boolean hasSameKeytab(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor that) {
        try {
            return this.getKeytabDescriptor().getFile().equals(that.getKeytabDescriptor().getFile());
        } catch (java.lang.NullPointerException e) {
            return false;
        }
    }

    private boolean hasSamePrincipal(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor that) {
        try {
            return this.getPrincipalDescriptor().getValue().equals(that.getPrincipalDescriptor().getValue());
        } catch (java.lang.NullPointerException e) {
            return false;
        }
    }

    public boolean isReference() {
        java.lang.String name = getName();
        return (!org.apache.commons.lang.StringUtils.isEmpty(reference)) || ((!org.apache.commons.lang.StringUtils.isEmpty(name)) && (name.startsWith("/") || name.startsWith("./")));
    }

    @java.lang.Override
    public java.lang.String getPath() {
        if (path == null) {
            path = super.getPath();
        }
        return path;
    }

    void setPath(java.lang.String path) {
        this.path = path;
    }

    @java.lang.Override
    public int hashCode() {
        return (((super.hashCode() + (getReference() == null ? 0 : getReference().hashCode())) + (getPrincipalDescriptor() == null ? 0 : getPrincipalDescriptor().hashCode())) + (getKeytabDescriptor() == null ? 0 : getKeytabDescriptor().hashCode())) + (getWhen() == null ? 0 : getWhen().hashCode());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (object == null) {
            return false;
        } else if (object == this) {
            return true;
        } else if (object.getClass() == org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class) {
            org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor descriptor = ((org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor) (object));
            return (((super.equals(object) && (getReference() == null ? descriptor.getReference() == null : getReference().equals(descriptor.getReference()))) && (getPrincipalDescriptor() == null ? descriptor.getPrincipalDescriptor() == null : getPrincipalDescriptor().equals(descriptor.getPrincipalDescriptor()))) && (getKeytabDescriptor() == null ? descriptor.getKeytabDescriptor() == null : getKeytabDescriptor().equals(descriptor.getKeytabDescriptor()))) && (getWhen() == null ? descriptor.getWhen() == null : getWhen().equals(descriptor.getWhen()));
        } else {
            return false;
        }
    }

    public java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> findReferences() {
        org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor root = getRoot();
        if (root instanceof org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer) {
            return findIdentityReferences(((org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer) (root)), getPath());
        } else {
            return null;
        }
    }

    private java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> findIdentityReferences(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer root, java.lang.String path) {
        if (root == null) {
            return null;
        }
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> references = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identityDescriptors = root.getIdentities();
        if (identityDescriptors != null) {
            for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor : identityDescriptors) {
                if (identityDescriptor.isReference()) {
                    java.lang.String reference = identityDescriptor.getReferenceAbsolutePath();
                    if ((!org.apache.commons.lang.StringUtils.isEmpty(reference)) && path.equals(reference)) {
                        references.add(identityDescriptor);
                    }
                }
            }
        }
        java.util.Collection<? extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer> children = root.getChildContainers();
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(children)) {
            for (org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer child : children) {
                java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> childReferences = findIdentityReferences(child, path);
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(childReferences)) {
                    references.addAll(childReferences);
                }
            }
        }
        return references;
    }
}