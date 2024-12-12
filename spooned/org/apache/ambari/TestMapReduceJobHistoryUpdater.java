package org.apache.ambari;
public class TestMapReduceJobHistoryUpdater extends junit.framework.TestCase {
    public void testDagMerging() {
        org.apache.ambari.eventdb.model.WorkflowDag dag1 = new org.apache.ambari.eventdb.model.WorkflowDag();
        dag1.addEntry(org.apache.ambari.TestMapReduceJobHistoryUpdater.getEntry("a", "b", "c"));
        dag1.addEntry(org.apache.ambari.TestMapReduceJobHistoryUpdater.getEntry("b", "d"));
        org.apache.ambari.eventdb.model.WorkflowContext one = new org.apache.ambari.eventdb.model.WorkflowContext();
        one.setWorkflowDag(dag1);
        org.apache.ambari.eventdb.model.WorkflowDag dag2 = new org.apache.ambari.eventdb.model.WorkflowDag();
        dag2.addEntry(org.apache.ambari.TestMapReduceJobHistoryUpdater.getEntry("a", "d"));
        dag2.addEntry(org.apache.ambari.TestMapReduceJobHistoryUpdater.getEntry("c", "e"));
        org.apache.ambari.eventdb.model.WorkflowContext two = new org.apache.ambari.eventdb.model.WorkflowContext();
        two.setWorkflowDag(dag2);
        org.apache.ambari.eventdb.model.WorkflowDag emptyDag = new org.apache.ambari.eventdb.model.WorkflowDag();
        org.apache.ambari.eventdb.model.WorkflowContext three = new org.apache.ambari.eventdb.model.WorkflowContext();
        three.setWorkflowDag(emptyDag);
        org.apache.ambari.eventdb.model.WorkflowDag mergedDag = new org.apache.ambari.eventdb.model.WorkflowDag();
        mergedDag.addEntry(org.apache.ambari.TestMapReduceJobHistoryUpdater.getEntry("a", "b", "c", "d"));
        mergedDag.addEntry(org.apache.ambari.TestMapReduceJobHistoryUpdater.getEntry("b", "d"));
        mergedDag.addEntry(org.apache.ambari.TestMapReduceJobHistoryUpdater.getEntry("c", "e"));
        org.apache.ambari.TestMapReduceJobHistoryUpdater.assertEquals(mergedDag, org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.constructMergedDag(one, two));
        org.apache.ambari.TestMapReduceJobHistoryUpdater.assertEquals(mergedDag, org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.constructMergedDag(two, one));
        org.apache.ambari.TestMapReduceJobHistoryUpdater.assertEquals(dag1, org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.constructMergedDag(three, one));
        org.apache.ambari.TestMapReduceJobHistoryUpdater.assertEquals(dag1, org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.constructMergedDag(one, three));
        org.apache.ambari.TestMapReduceJobHistoryUpdater.assertEquals(dag2, org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.constructMergedDag(three, two));
        org.apache.ambari.TestMapReduceJobHistoryUpdater.assertEquals(dag2, org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.constructMergedDag(two, three));
        org.apache.ambari.TestMapReduceJobHistoryUpdater.assertEquals(dag1, org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.constructMergedDag(new org.apache.ambari.eventdb.model.WorkflowContext(), one));
        org.apache.ambari.TestMapReduceJobHistoryUpdater.assertEquals(dag1, org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.constructMergedDag(one, new org.apache.ambari.eventdb.model.WorkflowContext()));
        org.apache.ambari.TestMapReduceJobHistoryUpdater.assertEquals(dag2, org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.constructMergedDag(new org.apache.ambari.eventdb.model.WorkflowContext(), two));
        org.apache.ambari.TestMapReduceJobHistoryUpdater.assertEquals(dag2, org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.constructMergedDag(two, new org.apache.ambari.eventdb.model.WorkflowContext()));
        org.apache.ambari.TestMapReduceJobHistoryUpdater.assertEquals(dag1, org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.constructMergedDag(one, one));
        org.apache.ambari.TestMapReduceJobHistoryUpdater.assertEquals(dag2, org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.constructMergedDag(two, two));
        org.apache.ambari.TestMapReduceJobHistoryUpdater.assertEquals(emptyDag, org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.constructMergedDag(three, three));
    }

    private static org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry getEntry(java.lang.String source, java.lang.String... targets) {
        org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry entry = new org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry();
        entry.setSource(source);
        for (java.lang.String target : targets) {
            entry.addTarget(target);
        }
        return entry;
    }

    private static void assertEquals(org.apache.ambari.eventdb.model.WorkflowDag dag1, org.apache.ambari.eventdb.model.WorkflowDag dag2) {
        junit.framework.Assert.assertEquals(dag1.size(), dag2.size());
        java.util.List<org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry> entries1 = dag1.getEntries();
        java.util.List<org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry> entries2 = dag2.getEntries();
        junit.framework.Assert.assertEquals(entries1.size(), entries2.size());
        for (int i = 0; i < entries1.size(); i++) {
            org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry e1 = entries1.get(i);
            org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry e2 = entries2.get(i);
            junit.framework.Assert.assertEquals(e1.getSource(), e2.getSource());
            java.util.List<java.lang.String> t1 = e1.getTargets();
            java.util.List<java.lang.String> t2 = e2.getTargets();
            junit.framework.Assert.assertEquals(t1.size(), t2.size());
            for (int j = 0; j < t1.size(); j++) {
                junit.framework.Assert.assertEquals(t1.get(j), t2.get(j));
            }
        }
    }
}