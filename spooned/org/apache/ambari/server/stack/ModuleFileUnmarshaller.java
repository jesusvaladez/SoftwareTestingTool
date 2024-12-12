package org.apache.ambari.server.stack;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.lang.exception.ExceptionUtils;
public class ModuleFileUnmarshaller {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.ModuleFileUnmarshaller.class);

    private static final java.util.Map<java.lang.Class<?>, javax.xml.bind.JAXBContext> jaxbContexts = new java.util.HashMap<>();

    private static final java.util.Map<java.lang.String, javax.xml.validation.Schema> jaxbSchemas = new java.util.HashMap<>();

    public <T> T unmarshal(java.lang.Class<T> clz, java.io.File file) throws javax.xml.bind.JAXBException, java.io.IOException, javax.xml.stream.XMLStreamException, org.xml.sax.SAXException {
        return unmarshal(clz, file, false);
    }

    public <T> T unmarshal(java.lang.Class<T> clz, java.io.File file, boolean logXsd) throws javax.xml.bind.JAXBException, java.io.IOException, javax.xml.stream.XMLStreamException, org.xml.sax.SAXException {
        javax.xml.bind.Unmarshaller u = org.apache.ambari.server.stack.ModuleFileUnmarshaller.jaxbContexts.get(clz).createUnmarshaller();
        javax.xml.stream.XMLInputFactory xmlFactory = javax.xml.stream.XMLInputFactory.newInstance();
        java.io.FileReader reader = new java.io.FileReader(file);
        javax.xml.stream.XMLStreamReader xmlReader = xmlFactory.createXMLStreamReader(reader);
        xmlReader.nextTag();
        java.lang.String xsdName = xmlReader.getAttributeValue(javax.xml.XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "noNamespaceSchemaLocation");
        java.io.InputStream xsdStream = null;
        if (null != xsdName) {
            if (logXsd) {
                org.apache.ambari.server.stack.ModuleFileUnmarshaller.LOG.info((("Processing " + file.getAbsolutePath()) + " with ") + xsdName);
            }
            if (org.apache.ambari.server.stack.ModuleFileUnmarshaller.jaxbSchemas.containsKey(xsdName)) {
                u.setSchema(org.apache.ambari.server.stack.ModuleFileUnmarshaller.jaxbSchemas.get(xsdName));
            } else {
                xsdStream = clz.getClassLoader().getResourceAsStream(xsdName);
                if (null != xsdStream) {
                    javax.xml.validation.SchemaFactory factory = javax.xml.validation.SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    javax.xml.validation.Schema schema = factory.newSchema(new javax.xml.transform.stream.StreamSource(xsdStream));
                    u.setSchema(schema);
                    org.apache.ambari.server.stack.ModuleFileUnmarshaller.jaxbSchemas.put(xsdName, schema);
                } else if (logXsd) {
                    org.apache.ambari.server.stack.ModuleFileUnmarshaller.LOG.info(((("Schema '" + xsdName) + "' for ") + file.getAbsolutePath()) + " was not found, ignoring");
                }
            }
        } else if (logXsd) {
            org.apache.ambari.server.stack.ModuleFileUnmarshaller.LOG.info(("NOT processing " + file.getAbsolutePath()) + "; there is no XSD");
        }
        try {
            return clz.cast(u.unmarshal(file));
        } catch (java.lang.Exception unmarshalException) {
            java.lang.Throwable cause = org.apache.commons.lang.exception.ExceptionUtils.getRootCause(unmarshalException);
            org.apache.ambari.server.stack.ModuleFileUnmarshaller.LOG.error("Cannot parse {}", file.getAbsolutePath());
            if (null != cause) {
                org.apache.ambari.server.stack.ModuleFileUnmarshaller.LOG.error(cause.getMessage(), cause);
            }
            throw unmarshalException;
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(xsdStream);
        }
    }

    static {
        try {
            javax.xml.bind.JAXBContext ctx = javax.xml.bind.JAXBContext.newInstance(org.apache.ambari.server.state.stack.StackMetainfoXml.class, org.apache.ambari.server.state.stack.RepositoryXml.class, org.apache.ambari.server.state.stack.ConfigurationXml.class, org.apache.ambari.server.stack.upgrade.UpgradePack.class, org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.class);
            jaxbContexts.put(org.apache.ambari.server.state.stack.StackMetainfoXml.class, ctx);
            jaxbContexts.put(org.apache.ambari.server.state.stack.RepositoryXml.class, ctx);
            jaxbContexts.put(org.apache.ambari.server.state.stack.ConfigurationXml.class, ctx);
            jaxbContexts.put(org.apache.ambari.server.stack.upgrade.UpgradePack.class, ctx);
            jaxbContexts.put(org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.class, ctx);
            jaxbContexts.put(org.apache.ambari.server.state.stack.ServiceMetainfoXml.class, javax.xml.bind.JAXBContext.newInstance(org.apache.ambari.server.state.stack.ServiceMetainfoXml.class));
            jaxbContexts.put(org.apache.ambari.server.state.stack.ExtensionMetainfoXml.class, javax.xml.bind.JAXBContext.newInstance(org.apache.ambari.server.state.stack.ExtensionMetainfoXml.class));
        } catch (javax.xml.bind.JAXBException e) {
            throw new java.lang.RuntimeException(e);
        }
    }
}