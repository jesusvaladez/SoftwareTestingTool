package org.apache.ambari.server.state.stack;
@org.junit.experimental.categories.Category({ category.StackUpgradeTest.class })
public class UpgradePackParsingTest {
    @org.junit.Test
    @java.lang.SuppressWarnings("unchecked")
    public void findAndValidateUpgradePacks() throws java.lang.Exception {
        org.apache.commons.io.filefilter.IOFileFilter filter = new org.apache.commons.io.filefilter.IOFileFilter() {
            @java.lang.Override
            public boolean accept(java.io.File dir, java.lang.String name) {
                return false;
            }

            @java.lang.Override
            public boolean accept(java.io.File file) {
                if ((file.getAbsolutePath().contains("upgrades") && file.getAbsolutePath().endsWith(".xml")) && (!file.getAbsolutePath().contains("config-upgrade.xml"))) {
                    return true;
                }
                return false;
            }
        };
        java.util.List<java.io.File> files = new java.util.ArrayList<>();
        files.addAll(org.apache.commons.io.FileUtils.listFiles(new java.io.File("src/main/resources/stacks"), filter, org.apache.commons.io.filefilter.FileFilterUtils.directoryFileFilter()));
        files.addAll(org.apache.commons.io.FileUtils.listFiles(new java.io.File("src/test/resources/stacks"), filter, org.apache.commons.io.filefilter.FileFilterUtils.directoryFileFilter()));
        files.addAll(org.apache.commons.io.FileUtils.listFiles(new java.io.File("src/test/resources/stacks_with_upgrade_cycle"), filter, org.apache.commons.io.filefilter.FileFilterUtils.directoryFileFilter()));
        org.apache.ambari.server.stack.ModuleFileUnmarshaller unmarshaller = new org.apache.ambari.server.stack.ModuleFileUnmarshaller();
        for (java.io.File file : files) {
            java.lang.String fileContent = org.apache.commons.io.FileUtils.readFileToString(file, "UTF-8");
            if (fileContent.contains("<upgrade") && fileContent.contains("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"")) {
                if (!fileContent.contains("xsi:noNamespaceSchemaLocation=\"upgrade-pack.xsd\"")) {
                    java.lang.String msg = java.lang.String.format("File %s appears to be an upgrade pack, but does not define 'upgrade-pack.xsd' as its schema", file.getAbsolutePath());
                    org.junit.Assert.fail(msg);
                } else {
                    unmarshaller.unmarshal(org.apache.ambari.server.stack.upgrade.UpgradePack.class, file, true);
                }
            }
        }
    }
}