package org.apache.ambari.eventdb.model;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class WorkflowDag {
    public static class WorkflowDagEntry {
        private java.lang.String source;

        private java.util.List<java.lang.String> targets = new java.util.ArrayList<java.lang.String>();

        public WorkflowDagEntry() {
        }

        public java.lang.String getSource() {
            return this.source;
        }

        public java.util.List<java.lang.String> getTargets() {
            return this.targets;
        }

        public void setSource(java.lang.String source) {
            this.source = source;
        }

        public void setTargets(java.util.List<java.lang.String> targets) {
            this.targets = targets;
        }

        public void addTarget(java.lang.String target) {
            this.targets.add(target);
        }
    }

    java.util.List<org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry> entries = new java.util.ArrayList<org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry>();

    public WorkflowDag() {
    }

    public java.util.List<org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry> getEntries() {
        return this.entries;
    }

    public void setEntries(java.util.List<org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry> entries) {
        this.entries = entries;
    }

    public void addEntry(org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry entry) {
        this.entries.add(entry);
    }

    public int size() {
        java.util.Set<java.lang.String> nodes = new java.util.HashSet<java.lang.String>();
        for (org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry entry : entries) {
            nodes.add(entry.getSource());
            nodes.addAll(entry.getTargets());
        }
        return nodes.size();
    }
}