package org.apache.oozie.ambari.view;
public class OozieUtils {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.oozie.ambari.view.OozieUtils.class);

    private org.apache.oozie.ambari.view.Utils utils = new org.apache.oozie.ambari.view.Utils();

    public java.lang.String generateConfigXml(java.util.Map<java.lang.String, java.lang.String> map) {
        javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        javax.xml.parsers.DocumentBuilder db;
        try {
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.newDocument();
            org.w3c.dom.Element configElement = doc.createElement("configuration");
            doc.appendChild(configElement);
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : map.entrySet()) {
                org.w3c.dom.Element propElement = doc.createElement("property");
                configElement.appendChild(propElement);
                org.w3c.dom.Element nameElem = doc.createElement("name");
                nameElem.setTextContent(entry.getKey());
                org.w3c.dom.Element valueElem = doc.createElement("value");
                valueElem.setTextContent(entry.getValue());
                propElement.appendChild(nameElem);
                propElement.appendChild(valueElem);
            }
            return utils.generateXml(doc);
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            org.apache.oozie.ambari.view.OozieUtils.LOGGER.error("error in generating config xml", e);
            throw new java.lang.RuntimeException(e);
        }
    }

    public java.lang.String getJobPathPropertyKey(org.apache.oozie.ambari.view.JobType jobType) {
        switch (jobType) {
            case WORKFLOW :
                return "oozie.wf.application.path";
            case COORDINATOR :
                return "oozie.coord.application.path";
            case BUNDLE :
                return "oozie.bundle.application.path";
        }
        throw new java.lang.RuntimeException("Unknown Job Type");
    }

    public org.apache.oozie.ambari.view.JobType deduceJobType(java.lang.String xml) {
        try {
            javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            javax.xml.parsers.DocumentBuilder db = null;
            db = dbf.newDocumentBuilder();
            org.xml.sax.InputSource is = new org.xml.sax.InputSource();
            is.setCharacterStream(new java.io.StringReader(xml));
            org.w3c.dom.Document doc = db.parse(is);
            java.lang.String rootNode = doc.getDocumentElement().getNodeName();
            if ("workflow-app".equals(rootNode)) {
                return org.apache.oozie.ambari.view.JobType.WORKFLOW;
            } else if ("coordinator-app".equals(rootNode)) {
                return org.apache.oozie.ambari.view.JobType.COORDINATOR;
            } else if ("bundle-app".equals(rootNode)) {
                return org.apache.oozie.ambari.view.JobType.BUNDLE;
            }
            throw new java.lang.RuntimeException("invalid xml submitted");
        } catch (javax.xml.parsers.ParserConfigurationException | java.lang.Exception e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public java.lang.String deduceWorkflowNameFromJson(java.lang.String json) {
        com.google.gson.JsonElement jsonElement = new com.google.gson.JsonParser().parse(json);
        java.lang.String name = jsonElement.getAsJsonObject().get("name").getAsString();
        return name;
    }

    public java.lang.String deduceWorkflowSchemaVersionFromJson(java.lang.String json) {
        com.google.gson.JsonElement jsonElement = new com.google.gson.JsonParser().parse(json);
        return jsonElement.getAsJsonObject().get("xmlns").getAsString();
    }

    public java.lang.String deduceWorkflowNameFromXml(java.lang.String xml) {
        try {
            javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
            org.xml.sax.InputSource is = new org.xml.sax.InputSource();
            is.setCharacterStream(new java.io.StringReader(xml));
            org.w3c.dom.Document doc = db.parse(is);
            java.lang.String name = doc.getDocumentElement().getAttributeNode("name").getValue();
            return name;
        } catch (javax.xml.parsers.ParserConfigurationException | java.lang.Exception e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public java.lang.String generateWorkflowXml(java.lang.String actionNodeXml) {
        javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        javax.xml.parsers.DocumentBuilder db;
        try {
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.newDocument();
            org.w3c.dom.Element workflowElement = doc.createElement("workflow-app");
            workflowElement.setAttribute("name", "testWorkflow");
            workflowElement.setAttribute("xmlns", "uri:oozie:workflow:0.5");
            doc.appendChild(workflowElement);
            org.w3c.dom.Element startElement = doc.createElement("start");
            startElement.setAttribute("to", "testAction");
            workflowElement.appendChild(startElement);
            org.w3c.dom.Element actionElement = doc.createElement("action");
            actionElement.setAttribute("name", "testAction");
            org.w3c.dom.Element actionSettingsElement = db.parse(new org.xml.sax.InputSource(new java.io.StringReader(actionNodeXml))).getDocumentElement();
            actionElement.appendChild(doc.importNode(actionSettingsElement, true));
            workflowElement.appendChild(actionElement);
            org.w3c.dom.Element actionOkTransitionElement = doc.createElement("ok");
            actionOkTransitionElement.setAttribute("to", "end");
            actionElement.appendChild(actionOkTransitionElement);
            org.w3c.dom.Element actionErrorTransitionElement = doc.createElement("error");
            actionErrorTransitionElement.setAttribute("to", "kill");
            actionElement.appendChild(actionErrorTransitionElement);
            org.w3c.dom.Element killElement = doc.createElement("kill");
            killElement.setAttribute("name", "kill");
            org.w3c.dom.Element killMessageElement = doc.createElement("message");
            killMessageElement.setTextContent("Kill node message");
            killElement.appendChild(killMessageElement);
            workflowElement.appendChild(killElement);
            org.w3c.dom.Element endElement = doc.createElement("end");
            endElement.setAttribute("name", "end");
            workflowElement.appendChild(endElement);
            return utils.generateXml(doc);
        } catch (javax.xml.parsers.ParserConfigurationException | org.xml.sax.SAXException | java.io.IOException e) {
            org.apache.oozie.ambari.view.OozieUtils.LOGGER.error("error in generating workflow xml", e);
            throw new java.lang.RuntimeException(e);
        }
    }

    public java.lang.String getNoOpWorkflowXml(java.lang.String json, org.apache.oozie.ambari.view.JobType jobType) {
        java.lang.String schema = deduceWorkflowSchemaVersionFromJson(json);
        java.lang.String name = deduceWorkflowNameFromJson(json);
        switch (jobType) {
            case WORKFLOW :
                return java.lang.String.format("<workflow-app xmlns=\"%s\" name=\"%s\"><start to=\"end\"/><end name=\"end\"/></workflow-app>", schema, name);
            case COORDINATOR :
                return java.lang.String.format("<coordinator-app xmlns=\"%s\" name=\"%s\"></coordinator-app>", schema, name);
            case BUNDLE :
                return java.lang.String.format("<bundle-app xmlns=\"%s\" name=\"%s\"></bundle-app>", schema, name);
        }
        return null;
    }
}