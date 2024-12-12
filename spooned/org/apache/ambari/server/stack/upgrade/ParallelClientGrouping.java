package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@javax.xml.bind.annotation.XmlType(name = "parallel-client")
public class ParallelClientGrouping extends org.apache.ambari.server.stack.upgrade.Grouping {
    @java.lang.Override
    public org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapperBuilder getBuilder() {
        return new org.apache.ambari.server.stack.upgrade.orchestrate.ParallelClientGroupingBuilder(this);
    }
}