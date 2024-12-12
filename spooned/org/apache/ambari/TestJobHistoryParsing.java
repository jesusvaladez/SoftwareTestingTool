package org.apache.ambari;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.JobHistory;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.tools.rumen.JobSubmittedEvent;
import org.apache.hadoop.util.StringUtils;
public class TestJobHistoryParsing extends junit.framework.TestCase {
    static final char LINE_DELIMITER_CHAR = '.';

    static final char[] charsToEscape = new char[]{ '"', '=', org.apache.ambari.TestJobHistoryParsing.LINE_DELIMITER_CHAR };

    private static final char DELIMITER = ' ';

    private static final java.lang.String ID = "WORKFLOW_ID";

    private static final java.lang.String NAME = "WORKFLOW_NAME";

    private static final java.lang.String NODE = "WORKFLOW_NODE_NAME";

    private static final java.lang.String ADJ = "WORKFLOW_ADJACENCIES";

    private static final java.lang.String ID_PROP = "mapreduce.workflow.id";

    private static final java.lang.String NAME_PROP = "mapreduce.workflow.name";

    private static final java.lang.String NODE_PROP = "mapreduce.workflow.node.name";

    private static final java.lang.String ADJ_PROP = "mapreduce.workflow.adjacency";

    public void test1() {
        java.util.Map<java.lang.String, java.lang.String[]> adj = new java.util.HashMap<java.lang.String, java.lang.String[]>();
        adj.put("10", new java.lang.String[]{ "20", "30" });
        adj.put("20", new java.lang.String[]{ "30" });
        adj.put("30", new java.lang.String[]{  });
        test("id_0-1", "something.name", "10", adj);
    }

    public void test2() {
        java.util.Map<java.lang.String, java.lang.String[]> adj = new java.util.HashMap<java.lang.String, java.lang.String[]>();
        adj.put("1=0", new java.lang.String[]{ "2 0", "3\"0." });
        adj.put("2 0", new java.lang.String[]{ "3\"0." });
        adj.put("3\"0.", new java.lang.String[]{  });
        test("id_= 0-1", "something.name", "1=0", adj);
    }

    public void test3() {
        java.lang.String s = "`~!@#$%^&*()-_=+[]{}|,.<>/?;:\'\"";
        test(s, s, s, new java.util.HashMap<java.lang.String, java.lang.String[]>());
    }

    public void test4() {
        java.util.Map<java.lang.String, java.lang.String[]> adj = new java.util.HashMap<java.lang.String, java.lang.String[]>();
        adj.put("X", new java.lang.String[]{  });
        test("", "jobName", "X", adj);
    }

    public void test(java.lang.String workflowId, java.lang.String workflowName, java.lang.String workflowNodeName, java.util.Map<java.lang.String, java.lang.String[]> adjacencies) {
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        org.apache.ambari.TestJobHistoryParsing.setProperties(conf, workflowId, workflowName, workflowNodeName, adjacencies);
        java.lang.String log = org.apache.ambari.TestJobHistoryParsing.log("JOB", new java.lang.String[]{ org.apache.ambari.TestJobHistoryParsing.ID, org.apache.ambari.TestJobHistoryParsing.NAME, org.apache.ambari.TestJobHistoryParsing.NODE, org.apache.ambari.TestJobHistoryParsing.ADJ }, new java.lang.String[]{ conf.get(org.apache.ambari.TestJobHistoryParsing.ID_PROP), conf.get(org.apache.ambari.TestJobHistoryParsing.NAME_PROP), conf.get(org.apache.ambari.TestJobHistoryParsing.NODE_PROP), JobHistory.JobInfo.getWorkflowAdjacencies(conf) });
        org.apache.ambari.TestJobHistoryParsing.ParsedLine line = new org.apache.ambari.TestJobHistoryParsing.ParsedLine(log);
        org.apache.hadoop.mapreduce.JobID jobid = new org.apache.hadoop.mapreduce.JobID("id", 1);
        org.apache.hadoop.tools.rumen.JobSubmittedEvent event = new org.apache.hadoop.tools.rumen.JobSubmittedEvent(jobid, workflowName, "", 0L, "", null, "", line.get(org.apache.ambari.TestJobHistoryParsing.ID), line.get(org.apache.ambari.TestJobHistoryParsing.NAME), line.get(org.apache.ambari.TestJobHistoryParsing.NODE), line.get(org.apache.ambari.TestJobHistoryParsing.ADJ));
        org.apache.ambari.eventdb.model.WorkflowContext context = org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.buildWorkflowContext(event);
        java.lang.String resultingWorkflowId = workflowId;
        if (workflowId.isEmpty())
            resultingWorkflowId = jobid.toString().replace("job_", "mr_");

        junit.framework.Assert.assertEquals("Didn't recover workflowId", resultingWorkflowId, context.getWorkflowId());
        junit.framework.Assert.assertEquals("Didn't recover workflowName", workflowName, context.getWorkflowName());
        junit.framework.Assert.assertEquals("Didn't recover workflowNodeName", workflowNodeName, context.getWorkflowEntityName());
        java.util.Map<java.lang.String, java.lang.String[]> resultingAdjacencies = adjacencies;
        if (resultingAdjacencies.size() == 0) {
            resultingAdjacencies = new java.util.HashMap<java.lang.String, java.lang.String[]>();
            resultingAdjacencies.put(workflowNodeName, new java.lang.String[]{  });
        }
        junit.framework.Assert.assertEquals("Got incorrect number of adjacencies", resultingAdjacencies.size(), context.getWorkflowDag().getEntries().size());
        for (org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry entry : context.getWorkflowDag().getEntries()) {
            java.lang.String[] sTargets = resultingAdjacencies.get(entry.getSource());
            junit.framework.Assert.assertNotNull("No original targets for " + entry.getSource(), sTargets);
            java.util.List<java.lang.String> dTargets = entry.getTargets();
            junit.framework.Assert.assertEquals("Got incorrect number of targets for " + entry.getSource(), sTargets.length, dTargets.size());
            for (int i = 0; i < sTargets.length; i++) {
                junit.framework.Assert.assertEquals("Got incorrect target for " + entry.getSource(), sTargets[i], dTargets.get(i));
            }
        }
    }

    private static void setProperties(org.apache.hadoop.conf.Configuration conf, java.lang.String workflowId, java.lang.String workflowName, java.lang.String workflowNodeName, java.util.Map<java.lang.String, java.lang.String[]> adj) {
        conf.set(org.apache.ambari.TestJobHistoryParsing.ID_PROP, workflowId);
        conf.set(org.apache.ambari.TestJobHistoryParsing.NAME_PROP, workflowName);
        conf.set(org.apache.ambari.TestJobHistoryParsing.NODE_PROP, workflowNodeName);
        for (java.util.Map.Entry<java.lang.String, java.lang.String[]> entry : adj.entrySet()) {
            conf.setStrings((org.apache.ambari.TestJobHistoryParsing.ADJ_PROP + ".") + entry.getKey(), entry.getValue());
        }
    }

    private static java.lang.String log(java.lang.String recordType, java.lang.String[] keys, java.lang.String[] values) {
        int length = (recordType.length() + (keys.length * 4)) + 2;
        for (int i = 0; i < keys.length; i++) {
            values[i] = org.apache.hadoop.util.StringUtils.escapeString(values[i], StringUtils.ESCAPE_CHAR, org.apache.ambari.TestJobHistoryParsing.charsToEscape);
            length += values[i].length() + keys[i].toString().length();
        }
        java.lang.StringBuilder builder = new java.lang.StringBuilder(length);
        builder.append(recordType);
        builder.append(org.apache.ambari.TestJobHistoryParsing.DELIMITER);
        for (int i = 0; i < keys.length; i++) {
            builder.append(keys[i]);
            builder.append("=\"");
            builder.append(values[i]);
            builder.append("\"");
            builder.append(org.apache.ambari.TestJobHistoryParsing.DELIMITER);
        }
        builder.append(org.apache.ambari.TestJobHistoryParsing.LINE_DELIMITER_CHAR);
        return builder.toString();
    }

    private static class ParsedLine {
        static final java.lang.String KEY = "(\\w+)";

        static final java.lang.String VALUE = "([^\"\\\\]*+(?:\\\\.[^\"\\\\]*+)*+)";

        static final java.util.regex.Pattern keyValPair = java.util.regex.Pattern.compile((((org.apache.ambari.TestJobHistoryParsing.ParsedLine.KEY + "=") + "\"") + org.apache.ambari.TestJobHistoryParsing.ParsedLine.VALUE) + "\"");

        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<java.lang.String, java.lang.String>();

        private java.lang.String type;

        ParsedLine(java.lang.String fullLine) {
            int firstSpace = fullLine.indexOf(" ");
            if (firstSpace < 0) {
                firstSpace = fullLine.length();
            }
            if (firstSpace == 0) {
                return;
            }
            type = fullLine.substring(0, firstSpace);
            java.lang.String propValPairs = fullLine.substring(firstSpace + 1);
            java.util.regex.Matcher matcher = org.apache.ambari.TestJobHistoryParsing.ParsedLine.keyValPair.matcher(propValPairs);
            while (matcher.find()) {
                java.lang.String key = matcher.group(1);
                java.lang.String value = matcher.group(2);
                props.put(key, value);
            } 
        }

        protected java.lang.String getType() {
            return type;
        }

        protected java.lang.String get(java.lang.String key) {
            return props.get(key);
        }
    }
}