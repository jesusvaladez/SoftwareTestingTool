package org.apache.ambari.server.state.stack;
@com.google.inject.Singleton
public class OsFamily {
    private static final java.lang.String OS_FAMILY_UBUNTU = "ubuntu";

    private static final java.lang.String OS_FAMILY_SUSE = "suse";

    private static final java.lang.String OS_FAMILY_REDHAT = "redhat";

    private final java.lang.String os_pattern = "([\\D]+|(?:[\\D]+[\\d]+[\\D]+))([\\d]*)";

    private final java.lang.String OS_DISTRO = "distro";

    private final java.lang.String OS_VERSION = "versions";

    private final java.lang.String LOAD_CONFIG_MSG = "Could not load OS family definition from %s file";

    private final java.lang.String FILE_NAME = "os_family.json";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.stack.OsFamily.class);

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.JsonOsFamilyEntry> osMap = null;

    private org.apache.ambari.server.state.stack.JsonOsFamilyRoot jsonOsFamily = null;

    public OsFamily(org.apache.ambari.server.configuration.Configuration conf) {
        init(conf.getSharedResourcesDirPath());
    }

    public OsFamily(java.util.Properties properties) {
        init(properties.getProperty(org.apache.ambari.server.configuration.Configuration.SHARED_RESOURCES_DIR.getKey()));
    }

    private void init(java.lang.String SharedResourcesPath) {
        java.io.FileInputStream inputStream = null;
        try {
            java.io.File f = new java.io.File(SharedResourcesPath, FILE_NAME);
            if (!f.exists()) {
                throw new java.lang.Exception();
            }
            inputStream = new java.io.FileInputStream(f);
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<org.apache.ambari.server.state.stack.JsonOsFamilyRoot>() {}.getType();
            com.google.gson.Gson gson = new com.google.gson.Gson();
            jsonOsFamily = gson.fromJson(new java.io.InputStreamReader(inputStream), type);
            osMap = jsonOsFamily.getMapping();
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.state.stack.OsFamily.LOG.error(java.lang.String.format(LOAD_CONFIG_MSG, new java.io.File(SharedResourcesPath, FILE_NAME).toString()));
            throw new java.lang.RuntimeException(e);
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(inputStream);
        }
    }

    private java.util.Map<java.lang.String, java.lang.String> parse_os(java.lang.String os) {
        java.util.Map<java.lang.String, java.lang.String> pos = new java.util.HashMap<>();
        java.util.regex.Pattern r = java.util.regex.Pattern.compile(os_pattern);
        java.util.regex.Matcher m = r.matcher(os);
        if (m.matches()) {
            pos.put(OS_DISTRO, m.group(1));
            pos.put(OS_VERSION, m.group(2));
        } else {
            pos.put(OS_DISTRO, os);
            pos.put(OS_VERSION, "");
        }
        return pos;
    }

    public java.util.Set<java.lang.String> findTypes(java.lang.String os) {
        java.util.Map<java.lang.String, java.lang.String> pos = parse_os(os);
        for (java.lang.String family : osMap.keySet()) {
            org.apache.ambari.server.state.stack.JsonOsFamilyEntry fam = osMap.get(family);
            if (fam.getDistro().contains(pos.get(OS_DISTRO)) && fam.getVersions().contains(pos.get(OS_VERSION))) {
                java.util.Set<java.lang.String> data = new java.util.HashSet<>();
                for (java.lang.String item : fam.getDistro()) {
                    data.add(item + pos.get(OS_VERSION));
                }
                return java.util.Collections.unmodifiableSet(data);
            }
        }
        return java.util.Collections.emptySet();
    }

    public java.lang.String find(java.lang.String os) {
        java.util.Map<java.lang.String, java.lang.String> pos = parse_os(os);
        for (java.lang.String family : osMap.keySet()) {
            org.apache.ambari.server.state.stack.JsonOsFamilyEntry fam = osMap.get(family);
            if (fam.getDistro().contains(pos.get(OS_DISTRO)) && fam.getVersions().contains(pos.get(OS_VERSION))) {
                return family + pos.get(OS_VERSION);
            }
        }
        return null;
    }

    public java.lang.String find_family(java.lang.String os) {
        java.util.Map<java.lang.String, java.lang.String> pos = parse_os(os);
        for (java.lang.String family : osMap.keySet()) {
            org.apache.ambari.server.state.stack.JsonOsFamilyEntry fam = osMap.get(family);
            if (fam.getDistro().contains(pos.get(OS_DISTRO)) && fam.getVersions().contains(pos.get(OS_VERSION))) {
                return family;
            }
        }
        return null;
    }

    public java.util.Set<java.lang.String> os_list() {
        java.util.Set<java.lang.String> r = new java.util.HashSet<>();
        for (java.lang.String family : osMap.keySet()) {
            org.apache.ambari.server.state.stack.JsonOsFamilyEntry fam = osMap.get(family);
            for (java.lang.String version : fam.getVersions()) {
                java.util.Set<java.lang.String> data = new java.util.HashSet<>();
                for (java.lang.String item : fam.getDistro()) {
                    data.add(item + version);
                }
                r.addAll(data);
            }
        }
        return r;
    }

    public boolean isUbuntuFamily(java.lang.String osType) {
        return isOsInFamily(osType, org.apache.ambari.server.state.stack.OsFamily.OS_FAMILY_UBUNTU);
    }

    public boolean isSuseFamily(java.lang.String osType) {
        return isOsInFamily(osType, org.apache.ambari.server.state.stack.OsFamily.OS_FAMILY_SUSE);
    }

    public boolean isRedhatFamily(java.lang.String osType) {
        return isOsInFamily(osType, org.apache.ambari.server.state.stack.OsFamily.OS_FAMILY_REDHAT);
    }

    public boolean isOsInFamily(java.lang.String osType, java.lang.String osFamily) {
        java.lang.String familyOfOsType = find_family(osType);
        return (familyOfOsType != null) && isFamilyExtendedByFamily(familyOfOsType, osFamily);
    }

    private boolean isFamilyExtendedByFamily(java.lang.String currentFamily, java.lang.String family) {
        return currentFamily.equals(family) || ((getOsFamilyParent(currentFamily) != null) && isFamilyExtendedByFamily(getOsFamilyParent(currentFamily), family));
    }

    public boolean isVersionedOsFamilyExtendedByVersionedFamily(java.lang.String currentVersionedFamily, java.lang.String versionedFamily) {
        java.util.Map<java.lang.String, java.lang.String> pos = this.parse_os(currentVersionedFamily);
        java.lang.String currentFamily = pos.get(OS_DISTRO);
        java.lang.String currentFamilyVersion = pos.get(OS_VERSION);
        pos = this.parse_os(versionedFamily);
        java.lang.String family = pos.get(OS_DISTRO);
        java.lang.String familyVersion = pos.get(OS_VERSION);
        return currentFamilyVersion.equals(familyVersion) && isFamilyExtendedByFamily(currentFamily, family);
    }

    private java.lang.String getOsFamilyParent(java.lang.String osFamily) {
        return osMap.get(osFamily).getExtendsFamily();
    }

    public java.util.Map<java.lang.String, java.lang.String> getAliases() {
        return (null == jsonOsFamily) || (null == jsonOsFamily.getAliases()) ? java.util.Collections.emptyMap() : jsonOsFamily.getAliases();
    }
}