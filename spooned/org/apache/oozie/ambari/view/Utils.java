package org.apache.oozie.ambari.view;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.StreamingOutput;
public class Utils {
    private static final java.lang.String XML_INDENT_SPACES = "4";

    private static final java.lang.String XML_INDENT_AMT_PROP_NAME = "{http://xml.apache.org/xslt}indent-amount";

    private final java.lang.String FEATURES_DISALLOW_DOCTYPE = "http://apache.org/xml/features/disallow-doctype-decl";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.oozie.ambari.view.Utils.class);

    private final javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();

    Utils() {
        try {
            dbf.setFeature(FEATURES_DISALLOW_DOCTYPE, true);
            org.apache.oozie.ambari.view.Utils.LOGGER.info("Setting feature disallow doctype to true");
        } catch (javax.xml.parsers.ParserConfigurationException | javax.xml.transform.TransformerFactoryConfigurationError e) {
            org.apache.oozie.ambari.view.Utils.LOGGER.error("Error in formatting xml", e);
            throw new java.lang.RuntimeException(e);
        }
    }

    public java.lang.String formatXml(java.lang.String xml) {
        try {
            javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(new java.io.StringWriter());
            org.w3c.dom.Document document = db.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));
            javax.xml.transform.Transformer transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(org.apache.oozie.ambari.view.Utils.XML_INDENT_AMT_PROP_NAME, org.apache.oozie.ambari.view.Utils.XML_INDENT_SPACES);
            javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(document);
            transformer.transform(source, result);
            return result.getWriter().toString();
        } catch (javax.xml.parsers.ParserConfigurationException | org.xml.sax.SAXException | java.io.IOException | javax.xml.transform.TransformerFactoryConfigurationError | javax.xml.transform.TransformerException e) {
            org.apache.oozie.ambari.view.Utils.LOGGER.error("Error in formatting xml", e);
            throw new java.lang.RuntimeException(e);
        }
    }

    public java.lang.String generateXml(org.w3c.dom.Document doc) {
        javax.xml.transform.dom.DOMSource domSource = new javax.xml.transform.dom.DOMSource(doc);
        java.io.StringWriter writer = new java.io.StringWriter();
        javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);
        javax.xml.transform.TransformerFactory tf = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer;
        try {
            transformer = tf.newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(org.apache.oozie.ambari.view.Utils.XML_INDENT_AMT_PROP_NAME, org.apache.oozie.ambari.view.Utils.XML_INDENT_SPACES);
            try {
                transformer.transform(domSource, result);
            } catch (javax.xml.transform.TransformerException e) {
                throw new java.lang.RuntimeException(e);
            }
            return writer.toString();
        } catch (javax.xml.transform.TransformerConfigurationException tce) {
            throw new java.lang.RuntimeException(tce);
        }
    }

    public java.util.Map<java.lang.String, java.lang.String> getHeaders(javax.ws.rs.core.HttpHeaders headers) {
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> requestHeaders = headers.getRequestHeaders();
        java.util.Set<java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>>> headerEntrySet = requestHeaders.entrySet();
        java.util.HashMap<java.lang.String, java.lang.String> headersMap = new java.util.HashMap<java.lang.String, java.lang.String>();
        for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> headerEntry : headerEntrySet) {
            java.lang.String key = headerEntry.getKey();
            java.util.List<java.lang.String> values = headerEntry.getValue();
            headersMap.put(key, strJoin(values, ","));
        }
        return headersMap;
    }

    public java.lang.String strJoin(java.util.List<java.lang.String> strings, java.lang.String separator) {
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
        for (int i = 0, il = strings.size(); i < il; i++) {
            if (i > 0) {
                stringBuilder.append(separator);
            }
            stringBuilder.append(strings.get(i));
        }
        return stringBuilder.toString();
    }

    public javax.ws.rs.core.MediaType deduceType(java.lang.String stringResponse) {
        if (stringResponse.startsWith("{")) {
            return javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
        } else if (stringResponse.startsWith("<")) {
            return javax.ws.rs.core.MediaType.TEXT_XML_TYPE;
        } else {
            return javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
        }
    }

    public java.lang.String convertParamsToUrl(javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> parameters) {
        java.lang.StringBuilder urlBuilder = new java.lang.StringBuilder();
        boolean firstEntry = true;
        for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> entry : parameters.entrySet()) {
            if (firstEntry) {
                urlBuilder.append("?");
            } else {
                urlBuilder.append("&");
            }
            boolean firstVal = true;
            for (java.lang.String val : entry.getValue()) {
                try {
                    val = java.net.URLEncoder.encode(val, "UTF-8");
                } catch (java.io.UnsupportedEncodingException e) {
                    org.apache.oozie.ambari.view.Utils.LOGGER.error(e.getMessage(), e);
                }
                urlBuilder.append(firstVal ? "" : "&").append(entry.getKey()).append("=").append(val);
                firstVal = false;
            }
            firstEntry = false;
        }
        return urlBuilder.toString();
    }

    public boolean isXml(java.lang.String postBody) {
        try {
            javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
            org.xml.sax.InputSource is = new org.xml.sax.InputSource();
            is.setCharacterStream(new java.io.StringReader(postBody));
            org.w3c.dom.Document doc = db.parse(is);
            return true;
        } catch (java.lang.Exception e) {
            return false;
        }
    }

    public javax.ws.rs.core.StreamingOutput streamResponse(final java.io.InputStream is) {
        return new javax.ws.rs.core.StreamingOutput() {
            @java.lang.Override
            public void write(java.io.OutputStream os) throws java.io.IOException, javax.ws.rs.WebApplicationException {
                java.io.BufferedInputStream bis = new java.io.BufferedInputStream(is);
                java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(os);
                try {
                    int data;
                    while ((data = bis.read()) != (-1)) {
                        bos.write(data);
                    } 
                    bos.flush();
                    is.close();
                } catch (java.io.IOException e) {
                    org.apache.oozie.ambari.view.Utils.LOGGER.error(e.getMessage(), e);
                    throw e;
                } catch (java.lang.Exception e) {
                    org.apache.oozie.ambari.view.Utils.LOGGER.error(e.getMessage(), e);
                    throw new java.lang.RuntimeException(e);
                } finally {
                    bis.close();
                }
            }
        };
    }
}