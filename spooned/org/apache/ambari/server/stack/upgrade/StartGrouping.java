package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlType;
@javax.xml.bind.annotation.XmlType(name = "start")
public class StartGrouping extends org.apache.ambari.server.stack.upgrade.Grouping implements org.apache.ambari.server.stack.upgrade.UpgradeFunction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.upgrade.StartGrouping.class);

    @java.lang.Override
    public org.apache.ambari.server.stack.upgrade.Task.Type getFunction() {
        return org.apache.ambari.server.stack.upgrade.Task.Type.START;
    }
}