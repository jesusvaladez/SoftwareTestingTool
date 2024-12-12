package org.apache.ambari.view;
public class ViewXmlTest {
    @org.junit.Test
    public void testValidateViewXmls() throws java.lang.Exception {
        java.util.List<java.io.File> viewXmlFiles = new java.util.LinkedList<java.io.File>();
        java.io.File ambariViewsDir = new java.io.File(".");
        java.io.File xsdFile = new java.io.File("./target/classes/view.xsd");
        for (java.io.File file : getViewXmlFiles(viewXmlFiles, ambariViewsDir.listFiles())) {
            validateViewXml(file, xsdFile);
        }
    }

    private java.util.List<java.io.File> getViewXmlFiles(java.util.List<java.io.File> viewXmlFiles, java.io.File[] files) throws java.lang.Exception {
        if (files != null) {
            for (java.io.File file : files) {
                if (file.isDirectory()) {
                    getViewXmlFiles(viewXmlFiles, file.listFiles());
                } else {
                    java.lang.String absolutePath = file.getAbsolutePath();
                    if (absolutePath.endsWith("/src/main/resources/view.xml")) {
                        viewXmlFiles.add(file);
                    }
                }
            }
        }
        return viewXmlFiles;
    }

    private void validateViewXml(java.io.File xmlFile, java.io.File xsdFile) throws java.lang.Exception {
        javax.xml.validation.SchemaFactory schemaFactory = javax.xml.validation.SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        javax.xml.validation.Schema schema = schemaFactory.newSchema(xsdFile);
        schema.newValidator().validate(new javax.xml.transform.stream.StreamSource(xmlFile));
    }
}