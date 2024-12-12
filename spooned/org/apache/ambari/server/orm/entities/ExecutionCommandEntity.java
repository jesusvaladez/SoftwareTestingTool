package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@javax.persistence.Table(name = "execution_command")
@javax.persistence.Entity
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "ExecutionCommandEntity.removeByTaskIds", query = "DELETE FROM ExecutionCommandEntity command WHERE command.taskId IN :taskIds") })
public class ExecutionCommandEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "task_id")
    private java.lang.Long taskId;

    @javax.persistence.Basic
    @javax.persistence.Lob
    @javax.persistence.Column(name = "command")
    private byte[] command;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "task_id", referencedColumnName = "task_id", nullable = false, insertable = false, updatable = false)
    private org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommand;

    public java.lang.Long getTaskId() {
        return taskId;
    }

    public void setTaskId(java.lang.Long taskId) {
        this.taskId = taskId;
    }

    public byte[] getCommand() {
        return command;
    }

    public void setCommand(byte[] command) {
        this.command = command;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.ExecutionCommandEntity that = ((org.apache.ambari.server.orm.entities.ExecutionCommandEntity) (o));
        if (command != null ? !java.util.Arrays.equals(command, that.command) : that.command != null)
            return false;

        if (taskId != null ? !taskId.equals(that.taskId) : that.taskId != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (taskId != null) ? taskId.hashCode() : 0;
        result = (31 * result) + (command != null ? java.util.Arrays.hashCode(command) : 0);
        return result;
    }

    public org.apache.ambari.server.orm.entities.HostRoleCommandEntity getHostRoleCommand() {
        return hostRoleCommand;
    }

    public void setHostRoleCommand(org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandByTaskId) {
        this.hostRoleCommand = hostRoleCommandByTaskId;
    }
}