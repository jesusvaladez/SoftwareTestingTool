package org.apache.ambari.server.view.configuration;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class PersistenceConfig {
    @javax.xml.bind.annotation.XmlElement(name = "entity")
    private java.util.List<org.apache.ambari.server.view.configuration.EntityConfig> entities;

    public java.util.List<org.apache.ambari.server.view.configuration.EntityConfig> getEntities() {
        return entities == null ? java.util.Collections.emptyList() : entities;
    }
}