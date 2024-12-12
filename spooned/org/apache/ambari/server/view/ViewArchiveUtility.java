package org.apache.ambari.server.view;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
public class ViewArchiveUtility {
    private static final java.lang.String VIEW_XML = "view.xml";

    private static final java.lang.String WEB_INF_VIEW_XML = "WEB-INF/classes/" + org.apache.ambari.server.view.ViewArchiveUtility.VIEW_XML;

    private static final java.lang.String VIEW_XSD = "view.xsd";

    public org.apache.ambari.server.view.configuration.ViewConfig getViewConfigFromArchive(java.io.File archiveFile) throws javax.xml.bind.JAXBException, java.io.IOException {
        java.lang.ClassLoader cl = java.net.URLClassLoader.newInstance(new java.net.URL[]{ archiveFile.toURI().toURL() });
        java.io.InputStream configStream = cl.getResourceAsStream(org.apache.ambari.server.view.ViewArchiveUtility.VIEW_XML);
        if (configStream == null) {
            configStream = cl.getResourceAsStream(org.apache.ambari.server.view.ViewArchiveUtility.WEB_INF_VIEW_XML);
            if (configStream == null) {
                throw new java.lang.IllegalStateException(java.lang.String.format("Archive %s doesn't contain a view descriptor.", archiveFile.getAbsolutePath()));
            }
        }
        try {
            javax.xml.bind.JAXBContext jaxbContext = javax.xml.bind.JAXBContext.newInstance(org.apache.ambari.server.view.configuration.ViewConfig.class);
            javax.xml.bind.Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return ((org.apache.ambari.server.view.configuration.ViewConfig) (jaxbUnmarshaller.unmarshal(configStream)));
        } finally {
            configStream.close();
        }
    }

    public org.apache.ambari.server.view.configuration.ViewConfig getViewConfigFromExtractedArchive(java.lang.String archivePath, boolean validate) throws javax.xml.bind.JAXBException, java.io.IOException, org.xml.sax.SAXException {
        java.io.File configFile = new java.io.File((archivePath + java.io.File.separator) + org.apache.ambari.server.view.ViewArchiveUtility.VIEW_XML);
        if (!configFile.exists()) {
            configFile = new java.io.File((archivePath + java.io.File.separator) + org.apache.ambari.server.view.ViewArchiveUtility.WEB_INF_VIEW_XML);
        }
        if (validate) {
            validateConfig(new java.io.FileInputStream(configFile));
        }
        java.io.InputStream configStream = new java.io.FileInputStream(configFile);
        try {
            javax.xml.bind.JAXBContext jaxbContext = javax.xml.bind.JAXBContext.newInstance(org.apache.ambari.server.view.configuration.ViewConfig.class);
            javax.xml.bind.Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return ((org.apache.ambari.server.view.configuration.ViewConfig) (jaxbUnmarshaller.unmarshal(configStream)));
        } finally {
            configStream.close();
        }
    }

    public java.io.File getFile(java.lang.String path) {
        return new java.io.File(path);
    }

    public java.io.FileOutputStream getFileOutputStream(java.io.File file) throws java.io.FileNotFoundException {
        return new java.io.FileOutputStream(file);
    }

    public java.util.jar.JarInputStream getJarFileStream(java.io.File file) throws java.io.IOException {
        return new java.util.jar.JarInputStream(new java.io.FileInputStream(file));
    }

    protected void validateConfig(java.io.InputStream configStream) throws org.xml.sax.SAXException, java.io.IOException {
        javax.xml.validation.SchemaFactory schemaFactory = javax.xml.validation.SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        java.net.URL schemaUrl = getClass().getClassLoader().getResource(org.apache.ambari.server.view.ViewArchiveUtility.VIEW_XSD);
        javax.xml.validation.Schema schema = schemaFactory.newSchema(schemaUrl);
        schema.newValidator().validate(new javax.xml.transform.stream.StreamSource(configStream));
    }
}