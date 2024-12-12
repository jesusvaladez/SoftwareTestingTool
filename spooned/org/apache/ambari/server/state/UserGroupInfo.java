package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserGroupInfo {
    private java.lang.String type;

    private java.lang.String name;

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }
}