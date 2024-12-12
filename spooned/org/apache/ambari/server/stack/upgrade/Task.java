package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
@javax.xml.bind.annotation.XmlSeeAlso({ org.apache.ambari.server.stack.upgrade.ExecuteTask.class, org.apache.ambari.server.stack.upgrade.CreateAndConfigureTask.class, org.apache.ambari.server.stack.upgrade.ConfigureTask.class, org.apache.ambari.server.stack.upgrade.ManualTask.class, org.apache.ambari.server.stack.upgrade.RestartTask.class, org.apache.ambari.server.stack.upgrade.StartTask.class, org.apache.ambari.server.stack.upgrade.StopTask.class, org.apache.ambari.server.stack.upgrade.ServerActionTask.class, org.apache.ambari.server.stack.upgrade.ConfigureFunction.class, org.apache.ambari.server.stack.upgrade.AddComponentTask.class, org.apache.ambari.server.stack.upgrade.RegenerateKeytabsTask.class })
public abstract class Task {
    protected static final com.google.gson.Gson GSON = new com.google.gson.GsonBuilder().serializeNulls().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

    @com.google.gson.annotations.Expose
    @javax.xml.bind.annotation.XmlElement(name = "summary")
    public java.lang.String summary;

    @com.google.gson.annotations.Expose
    @javax.xml.bind.annotation.XmlAttribute(name = "sequential")
    public boolean isSequential = false;

    @com.google.gson.annotations.Expose
    @javax.xml.bind.annotation.XmlAttribute(name = "timeout-config")
    public java.lang.String timeoutConfig = null;

    @javax.xml.bind.annotation.XmlElement(name = "condition")
    public org.apache.ambari.server.stack.upgrade.Condition condition;

    public abstract org.apache.ambari.server.stack.upgrade.Task.Type getType();

    public abstract org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type getStageWrapperType();

    public abstract java.lang.String getActionVerb();

    @com.google.gson.annotations.Expose
    @javax.xml.bind.annotation.XmlElement(name = "scope")
    public org.apache.ambari.server.stack.upgrade.UpgradeScope scope = org.apache.ambari.server.stack.upgrade.UpgradeScope.ANY;

    @java.lang.Override
    public java.lang.String toString() {
        return getType().toString();
    }

    public java.lang.String getSummary() {
        return summary;
    }

    public enum Type {

        EXECUTE,
        CONFIGURE,
        CREATE_AND_CONFIGURE,
        CONFIGURE_FUNCTION,
        MANUAL,
        RESTART,
        START,
        STOP,
        SERVICE_CHECK,
        SERVER_ACTION,
        ADD_COMPONENT,
        REGENERATE_KEYTABS;
        public static final java.util.EnumSet<org.apache.ambari.server.stack.upgrade.Task.Type> SERVER_ACTIONS = java.util.EnumSet.of(org.apache.ambari.server.stack.upgrade.Task.Type.MANUAL, org.apache.ambari.server.stack.upgrade.Task.Type.CONFIGURE, org.apache.ambari.server.stack.upgrade.Task.Type.SERVER_ACTION, org.apache.ambari.server.stack.upgrade.Task.Type.ADD_COMPONENT);

        public static final java.util.EnumSet<org.apache.ambari.server.stack.upgrade.Task.Type> COMMANDS = java.util.EnumSet.of(org.apache.ambari.server.stack.upgrade.Task.Type.RESTART, org.apache.ambari.server.stack.upgrade.Task.Type.START, org.apache.ambari.server.stack.upgrade.Task.Type.CONFIGURE_FUNCTION, org.apache.ambari.server.stack.upgrade.Task.Type.STOP, org.apache.ambari.server.stack.upgrade.Task.Type.SERVICE_CHECK, org.apache.ambari.server.stack.upgrade.Task.Type.REGENERATE_KEYTABS);

        public boolean isServerAction() {
            return org.apache.ambari.server.stack.upgrade.Task.Type.SERVER_ACTIONS.contains(this);
        }

        public boolean isCommand() {
            return org.apache.ambari.server.stack.upgrade.Task.Type.COMMANDS.contains(this);
        }
    }
}