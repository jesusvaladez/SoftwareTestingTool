package org.apache.ambari.server.state;
public class ServicePropertiesTest {
    @org.junit.Test
    public void validatePropertySchemaOfServiceXMLs() throws org.xml.sax.SAXException, java.io.IOException, java.net.URISyntaxException {
        javax.xml.validation.Validator validator = org.apache.ambari.server.stack.StackManager.getPropertySchemaValidator();
        org.apache.ambari.server.stack.StackManager.validateAllPropertyXmlsInFolderRecursively(getDirectoryFromMainResources("common-services"), validator);
        org.apache.ambari.server.stack.StackManager.validateAllPropertyXmlsInFolderRecursively(getDirectoryFromMainResources("stacks"), validator);
    }

    private java.io.File getDirectoryFromMainResources(java.lang.String dir) throws java.net.URISyntaxException, java.io.IOException {
        java.io.File resourcesFolder = new java.io.File(resolveAbsolutePathToResourcesFolder(), "../../../src/main/resources");
        java.io.File resultDir = new java.io.File(resourcesFolder, dir);
        if (resultDir.exists()) {
            return resultDir;
        } else {
            java.lang.String msg = java.lang.String.format("Directory %s does not exist", resultDir.getAbsolutePath());
            throw new java.io.IOException(msg);
        }
    }

    private java.io.File resolveAbsolutePathToResourcesFolder() throws java.net.URISyntaxException {
        java.net.URL dirURL = this.getClass().getClassLoader().getResource("TestAmbaryServer.samples");
        if ((dirURL != null) && dirURL.getProtocol().equals("file")) {
            return new java.io.File(dirURL.toURI());
        } else {
            throw new java.lang.UnsupportedOperationException(java.lang.String.format("Dir uri %s does not seem to point to file. Maybe a jar?", dirURL.toURI()));
        }
    }
}