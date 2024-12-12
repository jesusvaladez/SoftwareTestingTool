package org.apache.ambari.log4j.hadoop.mapreduce.jobhistory;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.CounterGroup;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.TaskType;
import org.apache.hadoop.tools.rumen.HistoryEvent;
import org.apache.hadoop.tools.rumen.JhCounter;
import org.apache.hadoop.tools.rumen.JhCounterGroup;
import org.apache.hadoop.tools.rumen.JhCounters;
import org.apache.hadoop.tools.rumen.JobFinishedEvent;
import org.apache.hadoop.tools.rumen.JobInfoChangeEvent;
import org.apache.hadoop.tools.rumen.JobInitedEvent;
import org.apache.hadoop.tools.rumen.JobStatusChangedEvent;
import org.apache.hadoop.tools.rumen.JobSubmittedEvent;
import org.apache.hadoop.tools.rumen.JobUnsuccessfulCompletionEvent;
import org.apache.hadoop.tools.rumen.MapAttemptFinishedEvent;
import org.apache.hadoop.tools.rumen.ReduceAttemptFinishedEvent;
import org.apache.hadoop.tools.rumen.TaskAttemptFinishedEvent;
import org.apache.hadoop.tools.rumen.TaskAttemptStartedEvent;
import org.apache.hadoop.tools.rumen.TaskAttemptUnsuccessfulCompletionEvent;
import org.apache.hadoop.tools.rumen.TaskFailedEvent;
import org.apache.hadoop.tools.rumen.TaskFinishedEvent;
import org.apache.hadoop.tools.rumen.TaskStartedEvent;
import org.apache.hadoop.util.StringUtils;
import org.apache.log4j.spi.LoggingEvent;
import org.codehaus.jackson.map.ObjectMapper;
public class MapReduceJobHistoryUpdater implements org.apache.ambari.log4j.common.LogStoreUpdateProvider {
    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.class);

    private java.sql.Connection connection;

    private static final java.lang.String WORKFLOW_TABLE = "workflow";

    private static final java.lang.String JOB_TABLE = "job";

    private static final java.lang.String TASK_TABLE = "task";

    private static final java.lang.String TASKATTEMPT_TABLE = "taskAttempt";

    private java.sql.PreparedStatement workflowPS = null;

    private java.sql.PreparedStatement workflowSelectPS = null;

    private java.sql.PreparedStatement workflowUpdateTimePS = null;

    private java.sql.PreparedStatement workflowUpdateNumCompletedPS = null;

    private java.util.Map<java.lang.Class<? extends org.apache.hadoop.tools.rumen.HistoryEvent>, java.sql.PreparedStatement> entitySqlMap = new java.util.HashMap<java.lang.Class<? extends org.apache.hadoop.tools.rumen.HistoryEvent>, java.sql.PreparedStatement>();

    @java.lang.Override
    public void init(java.sql.Connection connection) throws java.io.IOException {
        this.connection = connection;
        try {
            initializePreparedStatements();
        } catch (java.sql.SQLException sqle) {
            throw new java.io.IOException(sqle);
        }
    }

    private void initializePreparedStatements() throws java.sql.SQLException {
        initializeJobPreparedStatements();
        initializeTaskPreparedStatements();
        initializeTaskAttemptPreparedStatements();
    }

    private java.sql.PreparedStatement jobEndUpdate;

    private void initializeJobPreparedStatements() throws java.sql.SQLException {
        java.sql.PreparedStatement jobSubmittedPrepStmnt = connection.prepareStatement((((((((((((("INSERT INTO " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE) + " (") + "jobId, ") + "jobName, ") + "userName, ") + "confPath, ") + "queue, ") + "submitTime, ") + "workflowId, ") + "workflowEntityName ") + ") ") + "VALUES") + " (?, ?, ?, ?, ?, ?, ?, ?)");
        entitySqlMap.put(org.apache.hadoop.tools.rumen.JobSubmittedEvent.class, jobSubmittedPrepStmnt);
        workflowSelectPS = connection.prepareStatement(("SELECT workflowContext FROM " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.WORKFLOW_TABLE) + " where workflowId = ?");
        workflowPS = connection.prepareStatement(((((((((((((("INSERT INTO " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.WORKFLOW_TABLE) + " (") + "workflowId, ") + "workflowName, ") + "workflowContext, ") + "userName, ") + "startTime, ") + "lastUpdateTime, ") + "duration, ") + "numJobsTotal, ") + "numJobsCompleted") + ") ") + "VALUES") + " (?, ?, ?, ?, ?, ?, 0, ?, 0)");
        workflowUpdateTimePS = connection.prepareStatement((((((("UPDATE " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.WORKFLOW_TABLE) + " SET ") + "workflowContext = ?, ") + "numJobsTotal = ?, ") + "lastUpdateTime = ?, ") + "duration = ? - startTime ") + "WHERE workflowId = ?");
        workflowUpdateNumCompletedPS = connection.prepareStatement(((((((((((((((((((((((((((((((((("UPDATE " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.WORKFLOW_TABLE) + " SET ") + "lastUpdateTime = ?, ") + "duration = ? - startTime, ") + "numJobsCompleted = (") + "SELECT count(*)") + " FROM ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE) + " WHERE ") + "workflowId = ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.WORKFLOW_TABLE) + ".workflowId") + " AND status = 'SUCCESS'), ") + "inputBytes = (") + "SELECT sum(inputBytes)") + " FROM ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE) + " WHERE ") + "workflowId = ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.WORKFLOW_TABLE) + ".workflowId") + " AND status = 'SUCCESS'), ") + "outputBytes = (") + "SELECT sum(outputBytes)") + " FROM ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE) + " WHERE ") + "workflowId = ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.WORKFLOW_TABLE) + ".workflowId") + " AND status = 'SUCCESS') ") + " WHERE workflowId = (SELECT workflowId FROM ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE) + " WHERE jobId = ?)");
        java.sql.PreparedStatement jobFinishedPrepStmnt = connection.prepareStatement((((((((((("UPDATE " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE) + " SET ") + "finishTime = ?, ") + "finishedMaps = ?, ") + "finishedReduces= ?, ") + "failedMaps = ?, ") + "failedReduces = ?, ") + "inputBytes = ?, ") + "outputBytes = ? ") + "WHERE ") + "jobId = ?");
        entitySqlMap.put(org.apache.hadoop.tools.rumen.JobFinishedEvent.class, jobFinishedPrepStmnt);
        java.sql.PreparedStatement jobInitedPrepStmnt = connection.prepareStatement(((((((("UPDATE " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE) + " SET ") + "launchTime = ?, ") + "maps = ?, ") + "reduces = ?, ") + "status = ? ") + "WHERE ") + "jobId = ?");
        entitySqlMap.put(org.apache.hadoop.tools.rumen.JobInitedEvent.class, jobInitedPrepStmnt);
        java.sql.PreparedStatement jobStatusChangedPrepStmnt = connection.prepareStatement((((("UPDATE " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE) + " SET ") + "status = ? ") + "WHERE ") + "jobId = ?");
        entitySqlMap.put(org.apache.hadoop.tools.rumen.JobStatusChangedEvent.class, jobStatusChangedPrepStmnt);
        java.sql.PreparedStatement jobInfoChangedPrepStmnt = connection.prepareStatement(((((("UPDATE " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE) + " SET ") + "submitTime = ?, ") + "launchTime = ? ") + "WHERE ") + "jobId = ?");
        entitySqlMap.put(org.apache.hadoop.tools.rumen.JobInfoChangeEvent.class, jobInfoChangedPrepStmnt);
        java.sql.PreparedStatement jobUnsuccessfulPrepStmnt = connection.prepareStatement(((((((("UPDATE " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE) + " SET ") + "finishTime = ?, ") + "finishedMaps = ?, ") + "finishedReduces = ?, ") + "status = ? ") + "WHERE ") + "jobId = ?");
        entitySqlMap.put(org.apache.hadoop.tools.rumen.JobUnsuccessfulCompletionEvent.class, jobUnsuccessfulPrepStmnt);
        jobEndUpdate = connection.prepareStatement(((((((((((((((((((((((((((((((((((((((((("UPDATE " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE) + " SET ") + " mapsRuntime = (") + "SELECT ") + "SUM(") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE) + ".finishTime") + " - ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE) + ".startTime") + ")") + " FROM ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE) + " WHERE ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE) + ".jobId = ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE) + ".jobId ") + " AND ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE) + ".taskType = ?)") + ", ") + " reducesRuntime = (") + "SELECT SUM(") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE) + ".finishTime") + " - ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE) + ".startTime") + ")") + " FROM ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE) + " WHERE ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE) + ".jobId = ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE) + ".jobId ") + " AND ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE) + ".taskType = ?) ") + " WHERE ") + "jobId = ?");
    }

    private void initializeTaskPreparedStatements() throws java.sql.SQLException {
        java.sql.PreparedStatement taskStartedPrepStmnt = connection.prepareStatement((((((((("INSERT INTO " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASK_TABLE) + " (") + "jobId, ") + "taskType, ") + "splits, ") + "startTime, ") + "taskId") + ") ") + "VALUES (?, ?, ?, ?, ?)");
        entitySqlMap.put(org.apache.hadoop.tools.rumen.TaskStartedEvent.class, taskStartedPrepStmnt);
        java.sql.PreparedStatement taskFinishedPrepStmnt = connection.prepareStatement(((((((("UPDATE " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASK_TABLE) + " SET ") + "jobId = ?, ") + "taskType = ?, ") + "status = ?, ") + "finishTime = ? ") + " WHERE ") + "taskId = ?");
        entitySqlMap.put(org.apache.hadoop.tools.rumen.TaskFinishedEvent.class, taskFinishedPrepStmnt);
        java.sql.PreparedStatement taskFailedPrepStmnt = connection.prepareStatement(((((((((("UPDATE " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASK_TABLE) + " SET ") + "jobId = ?, ") + "taskType = ?, ") + "status = ?, ") + "finishTime = ?, ") + "error = ?, ") + "failedAttempt = ? ") + "WHERE ") + "taskId = ?");
        entitySqlMap.put(org.apache.hadoop.tools.rumen.TaskFailedEvent.class, taskFailedPrepStmnt);
    }

    private void initializeTaskAttemptPreparedStatements() throws java.sql.SQLException {
        java.sql.PreparedStatement taskAttemptStartedPrepStmnt = connection.prepareStatement(((((((((((("INSERT INTO " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE) + " (") + "jobId, ") + "taskId, ") + "taskType, ") + "startTime, ") + "taskTracker, ") + "locality, ") + "avataar, ") + "taskAttemptId") + ") ") + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        entitySqlMap.put(org.apache.hadoop.tools.rumen.TaskAttemptStartedEvent.class, taskAttemptStartedPrepStmnt);
        java.sql.PreparedStatement taskAttemptFinishedPrepStmnt = connection.prepareStatement(((((((((("UPDATE " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE) + " SET ") + "jobId = ?, ") + "taskId = ?, ") + "taskType = ?, ") + "finishTime = ?, ") + "status = ?, ") + "taskTracker = ? ") + " WHERE ") + "taskAttemptId = ?");
        entitySqlMap.put(org.apache.hadoop.tools.rumen.TaskAttemptFinishedEvent.class, taskAttemptFinishedPrepStmnt);
        java.sql.PreparedStatement taskAttemptUnsuccessfulPrepStmnt = connection.prepareStatement((((((((((("UPDATE " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE) + " SET ") + "jobId = ?, ") + "taskId = ?, ") + "taskType = ?, ") + "finishTime = ?, ") + "status = ?, ") + "taskTracker = ?, ") + "error = ? ") + " WHERE ") + "taskAttemptId = ?");
        entitySqlMap.put(org.apache.hadoop.tools.rumen.TaskAttemptUnsuccessfulCompletionEvent.class, taskAttemptUnsuccessfulPrepStmnt);
        java.sql.PreparedStatement mapAttemptFinishedPrepStmnt = connection.prepareStatement((((((((((((("UPDATE " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE) + " SET ") + "jobId = ?, ") + "taskId = ?, ") + "taskType = ?, ") + "mapFinishTime = ?, ") + "finishTime = ?, ") + "inputBytes = ?, ") + "outputBytes = ?, ") + "status = ?, ") + "taskTracker = ? ") + " WHERE ") + "taskAttemptId = ?");
        entitySqlMap.put(org.apache.hadoop.tools.rumen.MapAttemptFinishedEvent.class, mapAttemptFinishedPrepStmnt);
        java.sql.PreparedStatement reduceAttemptFinishedPrepStmnt = connection.prepareStatement(((((((((((((("UPDATE " + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE) + " SET ") + "jobId = ?, ") + "taskId = ?, ") + "taskType = ?, ") + "shuffleFinishTime = ?, ") + "sortFinishTime = ?, ") + "finishTime = ?, ") + "inputBytes = ?, ") + "outputBytes = ?, ") + "status = ?, ") + "taskTracker = ? ") + " WHERE ") + "taskAttemptId = ?");
        entitySqlMap.put(org.apache.hadoop.tools.rumen.ReduceAttemptFinishedEvent.class, reduceAttemptFinishedPrepStmnt);
    }

    private void doUpdates(org.apache.log4j.spi.LoggingEvent originalEvent, java.lang.Object parsedEvent) throws java.sql.SQLException {
        java.lang.Class<?> eventClass = parsedEvent.getClass();
        java.sql.PreparedStatement entityPS = entitySqlMap.get(eventClass);
        if (entityPS == null) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.debug("No prepared statement for " + eventClass);
            return;
        }
        if (eventClass == org.apache.hadoop.tools.rumen.JobSubmittedEvent.class) {
            processJobSubmittedEvent(entityPS, workflowSelectPS, workflowPS, workflowUpdateTimePS, originalEvent, ((org.apache.hadoop.tools.rumen.JobSubmittedEvent) (parsedEvent)));
        } else if (eventClass == org.apache.hadoop.tools.rumen.JobFinishedEvent.class) {
            processJobFinishedEvent(entityPS, workflowUpdateNumCompletedPS, originalEvent, ((org.apache.hadoop.tools.rumen.JobFinishedEvent) (parsedEvent)));
        } else if (eventClass == org.apache.hadoop.tools.rumen.JobInitedEvent.class) {
            processJobInitedEvent(entityPS, originalEvent, ((org.apache.hadoop.tools.rumen.JobInitedEvent) (parsedEvent)));
        } else if (eventClass == org.apache.hadoop.tools.rumen.JobStatusChangedEvent.class) {
            processJobStatusChangedEvent(entityPS, originalEvent, ((org.apache.hadoop.tools.rumen.JobStatusChangedEvent) (parsedEvent)));
        } else if (eventClass == org.apache.hadoop.tools.rumen.JobInfoChangeEvent.class) {
            processJobInfoChangeEvent(entityPS, originalEvent, ((org.apache.hadoop.tools.rumen.JobInfoChangeEvent) (parsedEvent)));
        } else if (eventClass == org.apache.hadoop.tools.rumen.JobUnsuccessfulCompletionEvent.class) {
            processJobUnsuccessfulEvent(entityPS, originalEvent, ((org.apache.hadoop.tools.rumen.JobUnsuccessfulCompletionEvent) (parsedEvent)));
        } else if (eventClass == org.apache.hadoop.tools.rumen.TaskStartedEvent.class) {
            processTaskStartedEvent(entityPS, originalEvent, ((org.apache.hadoop.tools.rumen.TaskStartedEvent) (parsedEvent)));
        } else if (eventClass == org.apache.hadoop.tools.rumen.TaskFinishedEvent.class) {
            processTaskFinishedEvent(entityPS, originalEvent, ((org.apache.hadoop.tools.rumen.TaskFinishedEvent) (parsedEvent)));
        } else if (eventClass == org.apache.hadoop.tools.rumen.TaskFailedEvent.class) {
            processTaskFailedEvent(entityPS, originalEvent, ((org.apache.hadoop.tools.rumen.TaskFailedEvent) (parsedEvent)));
        } else if (eventClass == org.apache.hadoop.tools.rumen.TaskAttemptStartedEvent.class) {
            processTaskAttemptStartedEvent(entityPS, originalEvent, ((org.apache.hadoop.tools.rumen.TaskAttemptStartedEvent) (parsedEvent)));
        } else if (eventClass == org.apache.hadoop.tools.rumen.TaskAttemptFinishedEvent.class) {
            processTaskAttemptFinishedEvent(entityPS, originalEvent, ((org.apache.hadoop.tools.rumen.TaskAttemptFinishedEvent) (parsedEvent)));
        } else if (eventClass == org.apache.hadoop.tools.rumen.TaskAttemptUnsuccessfulCompletionEvent.class) {
            processTaskAttemptUnsuccessfulEvent(entityPS, originalEvent, ((org.apache.hadoop.tools.rumen.TaskAttemptUnsuccessfulCompletionEvent) (parsedEvent)));
        } else if (eventClass == org.apache.hadoop.tools.rumen.MapAttemptFinishedEvent.class) {
            processMapAttemptFinishedEvent(entityPS, originalEvent, ((org.apache.hadoop.tools.rumen.MapAttemptFinishedEvent) (parsedEvent)));
        } else if (eventClass == org.apache.hadoop.tools.rumen.ReduceAttemptFinishedEvent.class) {
            processReduceAttemptFinishedEvent(entityPS, originalEvent, ((org.apache.hadoop.tools.rumen.ReduceAttemptFinishedEvent) (parsedEvent)));
        }
    }

    private void updateJobStatsAtFinish(java.lang.String jobId) {
        try {
            jobEndUpdate.setString(1, "MAP");
            jobEndUpdate.setString(2, "REDUCE");
            jobEndUpdate.setString(3, jobId);
            jobEndUpdate.executeUpdate();
        } catch (java.sql.SQLException sqle) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.info("Failed to update mapsRuntime/reducesRuntime for " + jobId, sqle);
        }
    }

    private static org.apache.ambari.eventdb.model.WorkflowContext generateWorkflowContext(org.apache.hadoop.tools.rumen.JobSubmittedEvent historyEvent) {
        org.apache.ambari.eventdb.model.WorkflowDag wfDag = new org.apache.ambari.eventdb.model.WorkflowDag();
        org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry wfDagEntry = new org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry();
        wfDagEntry.setSource("X");
        wfDag.addEntry(wfDagEntry);
        org.apache.ambari.eventdb.model.WorkflowContext wc = new org.apache.ambari.eventdb.model.WorkflowContext();
        wc.setWorkflowId(historyEvent.getJobId().toString().replace("job_", "mr_"));
        wc.setWorkflowName(historyEvent.getJobName());
        wc.setWorkflowEntityName("X");
        wc.setWorkflowDag(wfDag);
        return wc;
    }

    private static final java.util.regex.Pattern adjPattern = java.util.regex.Pattern.compile("\"([^\"\\\\]*+(?:\\\\.[^\"\\\\]*+)*+)\"" + ("=" + "\"([^\"\\\\]*+(?:\\\\.[^\"\\\\]*+)*+)\" "));

    public static org.apache.ambari.eventdb.model.WorkflowContext buildWorkflowContext(org.apache.hadoop.tools.rumen.JobSubmittedEvent historyEvent) {
        java.lang.String workflowId = historyEvent.getWorkflowId().replace("\\", "");
        if (workflowId.isEmpty())
            return org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.generateWorkflowContext(historyEvent);

        java.lang.String workflowName = historyEvent.getWorkflowName().replace("\\", "");
        java.lang.String workflowNodeName = historyEvent.getWorkflowNodeName().replace("\\", "");
        java.lang.String workflowAdjacencies = org.apache.hadoop.util.StringUtils.unEscapeString(historyEvent.getWorkflowAdjacencies(), StringUtils.ESCAPE_CHAR, new char[]{ '"', '=', '.' });
        org.apache.ambari.eventdb.model.WorkflowContext context = new org.apache.ambari.eventdb.model.WorkflowContext();
        context.setWorkflowId(workflowId);
        context.setWorkflowName(workflowName);
        context.setWorkflowEntityName(workflowNodeName);
        org.apache.ambari.eventdb.model.WorkflowDag dag = new org.apache.ambari.eventdb.model.WorkflowDag();
        java.util.regex.Matcher matcher = org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.adjPattern.matcher(workflowAdjacencies);
        while (matcher.find()) {
            org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry dagEntry = new org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry();
            dagEntry.setSource(matcher.group(1).replace("\\", ""));
            java.lang.String[] values = org.apache.hadoop.util.StringUtils.getStrings(matcher.group(2).replace("\\", ""));
            if (values != null) {
                for (java.lang.String target : values) {
                    dagEntry.addTarget(target);
                }
            }
            dag.addEntry(dagEntry);
        } 
        if (dag.getEntries().isEmpty()) {
            org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry wfDagEntry = new org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry();
            wfDagEntry.setSource(workflowNodeName);
            dag.addEntry(wfDagEntry);
        }
        context.setWorkflowDag(dag);
        return context;
    }

    public static void mergeEntries(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> edges, java.util.List<org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry> entries) {
        if (entries == null)
            return;

        for (org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry entry : entries) {
            if (!edges.containsKey(entry.getSource()))
                edges.put(entry.getSource(), new java.util.TreeSet<java.lang.String>());

            java.util.Set<java.lang.String> targets = edges.get(entry.getSource());
            targets.addAll(entry.getTargets());
        }
    }

    public static org.apache.ambari.eventdb.model.WorkflowDag constructMergedDag(org.apache.ambari.eventdb.model.WorkflowContext workflowContext, org.apache.ambari.eventdb.model.WorkflowContext existingWorkflowContext) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> edges = new java.util.TreeMap<java.lang.String, java.util.Set<java.lang.String>>();
        if (existingWorkflowContext.getWorkflowDag() != null)
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.mergeEntries(edges, existingWorkflowContext.getWorkflowDag().getEntries());

        if (workflowContext.getWorkflowDag() != null)
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.mergeEntries(edges, workflowContext.getWorkflowDag().getEntries());

        org.apache.ambari.eventdb.model.WorkflowDag mergedDag = new org.apache.ambari.eventdb.model.WorkflowDag();
        for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> edge : edges.entrySet()) {
            org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry entry = new org.apache.ambari.eventdb.model.WorkflowDag.WorkflowDagEntry();
            entry.setSource(edge.getKey());
            entry.getTargets().addAll(edge.getValue());
            mergedDag.addEntry(entry);
        }
        return mergedDag;
    }

    private static org.apache.ambari.eventdb.model.WorkflowContext getSanitizedWorkflow(org.apache.ambari.eventdb.model.WorkflowContext workflowContext, org.apache.ambari.eventdb.model.WorkflowContext existingWorkflowContext) {
        org.apache.ambari.eventdb.model.WorkflowContext sanitizedWC = new org.apache.ambari.eventdb.model.WorkflowContext();
        if (existingWorkflowContext == null) {
            sanitizedWC.setWorkflowDag(workflowContext.getWorkflowDag());
            sanitizedWC.setParentWorkflowContext(workflowContext.getParentWorkflowContext());
        } else {
            sanitizedWC.setWorkflowDag(org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.constructMergedDag(existingWorkflowContext, workflowContext));
            sanitizedWC.setParentWorkflowContext(existingWorkflowContext.getParentWorkflowContext());
        }
        return sanitizedWC;
    }

    private static java.lang.String getWorkflowString(org.apache.ambari.eventdb.model.WorkflowContext sanitizedWC) {
        java.lang.String sanitizedWCString = null;
        try {
            org.codehaus.jackson.map.ObjectMapper om = new org.codehaus.jackson.map.ObjectMapper();
            sanitizedWCString = om.writeValueAsString(sanitizedWC);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            sanitizedWCString = "";
        }
        return sanitizedWCString;
    }

    private void processJobSubmittedEvent(java.sql.PreparedStatement jobPS, java.sql.PreparedStatement workflowSelectPS, java.sql.PreparedStatement workflowPS, java.sql.PreparedStatement workflowUpdateTimePS, org.apache.log4j.spi.LoggingEvent logEvent, org.apache.hadoop.tools.rumen.JobSubmittedEvent historyEvent) {
        try {
            java.lang.String jobId = historyEvent.getJobId().toString();
            jobPS.setString(1, jobId);
            jobPS.setString(2, historyEvent.getJobName());
            jobPS.setString(3, historyEvent.getUserName());
            jobPS.setString(4, historyEvent.getJobConfPath());
            jobPS.setString(5, historyEvent.getJobQueueName());
            jobPS.setLong(6, historyEvent.getSubmitTime());
            org.apache.ambari.eventdb.model.WorkflowContext workflowContext = org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.buildWorkflowContext(historyEvent);
            boolean insertWorkflow = false;
            java.lang.String existingContextString = null;
            java.sql.ResultSet rs = null;
            try {
                workflowSelectPS.setString(1, workflowContext.getWorkflowId());
                workflowSelectPS.execute();
                rs = workflowSelectPS.getResultSet();
                if (rs.next()) {
                    existingContextString = rs.getString(1);
                } else {
                    insertWorkflow = true;
                }
            } catch (java.sql.SQLException sqle) {
                org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.warn("workflow select failed with: ", sqle);
                insertWorkflow = false;
            } finally {
                try {
                    if (rs != null)
                        rs.close();

                } catch (java.sql.SQLException e) {
                    org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.error("Exception while closing ResultSet", e);
                }
            }
            if (insertWorkflow) {
                workflowPS.setString(1, workflowContext.getWorkflowId());
                workflowPS.setString(2, workflowContext.getWorkflowName());
                workflowPS.setString(3, org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.getWorkflowString(org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.getSanitizedWorkflow(workflowContext, null)));
                workflowPS.setString(4, historyEvent.getUserName());
                workflowPS.setLong(5, historyEvent.getSubmitTime());
                workflowPS.setLong(6, historyEvent.getSubmitTime());
                workflowPS.setLong(7, workflowContext.getWorkflowDag().size());
                workflowPS.executeUpdate();
                org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.debug("Successfully inserted workflowId = " + workflowContext.getWorkflowId());
            } else {
                org.codehaus.jackson.map.ObjectMapper om = new org.codehaus.jackson.map.ObjectMapper();
                org.apache.ambari.eventdb.model.WorkflowContext existingWorkflowContext = null;
                try {
                    if (existingContextString != null)
                        existingWorkflowContext = om.readValue(existingContextString.getBytes(), org.apache.ambari.eventdb.model.WorkflowContext.class);

                } catch (java.io.IOException e) {
                    org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.warn("Couldn't read existing workflow context for " + workflowContext.getWorkflowId(), e);
                }
                org.apache.ambari.eventdb.model.WorkflowContext sanitizedWC = org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.getSanitizedWorkflow(workflowContext, existingWorkflowContext);
                workflowUpdateTimePS.setString(1, org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.getWorkflowString(sanitizedWC));
                workflowUpdateTimePS.setLong(2, sanitizedWC.getWorkflowDag().size());
                workflowUpdateTimePS.setLong(3, historyEvent.getSubmitTime());
                workflowUpdateTimePS.setLong(4, historyEvent.getSubmitTime());
                workflowUpdateTimePS.setString(5, workflowContext.getWorkflowId());
                workflowUpdateTimePS.executeUpdate();
                org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.debug("Successfully updated workflowId = " + workflowContext.getWorkflowId());
            }
            jobPS.setString(7, workflowContext.getWorkflowId());
            jobPS.setString(8, workflowContext.getWorkflowEntityName());
            jobPS.executeUpdate();
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.debug((("Successfully inserted job = " + jobId) + " and workflowId = ") + workflowContext.getWorkflowId());
        } catch (java.sql.SQLException sqle) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.info((((("Failed to store " + historyEvent.getEventType()) + " for job ") + historyEvent.getJobId()) + " into ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE, sqle);
        } catch (java.lang.Exception e) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.info((((("Failed to store " + historyEvent.getEventType()) + " for job ") + historyEvent.getJobId()) + " into ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE, e);
        }
    }

    private void processJobFinishedEvent(java.sql.PreparedStatement entityPS, java.sql.PreparedStatement workflowUpdateNumCompletedPS, org.apache.log4j.spi.LoggingEvent logEvent, org.apache.hadoop.tools.rumen.JobFinishedEvent historyEvent) {
        org.apache.hadoop.mapreduce.Counters counters = historyEvent.getMapCounters();
        long inputBytes = 0;
        if (counters != null) {
            for (org.apache.hadoop.mapreduce.CounterGroup group : counters) {
                for (org.apache.hadoop.mapreduce.Counter counter : group) {
                    if (counter.getName().equals("HDFS_BYTES_READ"))
                        inputBytes += counter.getValue();

                }
            }
        }
        if (historyEvent.getFinishedReduces() != 0)
            counters = historyEvent.getReduceCounters();

        long outputBytes = 0;
        if (counters != null) {
            for (org.apache.hadoop.mapreduce.CounterGroup group : counters) {
                for (org.apache.hadoop.mapreduce.Counter counter : group) {
                    if (counter.getName().equals("HDFS_BYTES_WRITTEN"))
                        outputBytes += counter.getValue();

                }
            }
        }
        try {
            entityPS.setLong(1, historyEvent.getFinishTime());
            entityPS.setInt(2, historyEvent.getFinishedMaps());
            entityPS.setInt(3, historyEvent.getFinishedReduces());
            entityPS.setInt(4, historyEvent.getFailedMaps());
            entityPS.setInt(5, historyEvent.getFailedReduces());
            entityPS.setLong(6, inputBytes);
            entityPS.setLong(7, outputBytes);
            entityPS.setString(8, historyEvent.getJobid().toString());
            entityPS.executeUpdate();
            workflowUpdateNumCompletedPS.setLong(1, historyEvent.getFinishTime());
            workflowUpdateNumCompletedPS.setLong(2, historyEvent.getFinishTime());
            workflowUpdateNumCompletedPS.setString(3, historyEvent.getJobid().toString());
            workflowUpdateNumCompletedPS.executeUpdate();
        } catch (java.sql.SQLException sqle) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.info((((("Failed to store " + historyEvent.getEventType()) + " for job ") + historyEvent.getJobid()) + " into ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE, sqle);
        }
        updateJobStatsAtFinish(historyEvent.getJobid().toString());
    }

    private void processJobInitedEvent(java.sql.PreparedStatement entityPS, org.apache.log4j.spi.LoggingEvent logEvent, org.apache.hadoop.tools.rumen.JobInitedEvent historyEvent) {
        try {
            entityPS.setLong(1, historyEvent.getLaunchTime());
            entityPS.setInt(2, historyEvent.getTotalMaps());
            entityPS.setInt(3, historyEvent.getTotalReduces());
            entityPS.setString(4, historyEvent.getStatus());
            entityPS.setString(5, historyEvent.getJobId().toString());
            entityPS.executeUpdate();
        } catch (java.sql.SQLException sqle) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.info((((("Failed to store " + historyEvent.getEventType()) + " for job ") + historyEvent.getJobId()) + " into ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE, sqle);
        }
    }

    private void processJobStatusChangedEvent(java.sql.PreparedStatement entityPS, org.apache.log4j.spi.LoggingEvent logEvent, org.apache.hadoop.tools.rumen.JobStatusChangedEvent historyEvent) {
        try {
            entityPS.setString(1, historyEvent.getStatus());
            entityPS.setString(2, historyEvent.getJobId().toString());
            entityPS.executeUpdate();
        } catch (java.sql.SQLException sqle) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.info((((("Failed to store " + historyEvent.getEventType()) + " for job ") + historyEvent.getJobId()) + " into ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE, sqle);
        }
    }

    private void processJobInfoChangeEvent(java.sql.PreparedStatement entityPS, org.apache.log4j.spi.LoggingEvent logEvent, org.apache.hadoop.tools.rumen.JobInfoChangeEvent historyEvent) {
        try {
            entityPS.setLong(1, historyEvent.getSubmitTime());
            entityPS.setLong(2, historyEvent.getLaunchTime());
            entityPS.setString(3, historyEvent.getJobId().toString());
            entityPS.executeUpdate();
        } catch (java.sql.SQLException sqle) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.info((((("Failed to store " + historyEvent.getEventType()) + " for job ") + historyEvent.getJobId()) + " into ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE, sqle);
        }
    }

    private void processJobUnsuccessfulEvent(java.sql.PreparedStatement entityPS, org.apache.log4j.spi.LoggingEvent logEvent, org.apache.hadoop.tools.rumen.JobUnsuccessfulCompletionEvent historyEvent) {
        try {
            entityPS.setLong(1, historyEvent.getFinishTime());
            entityPS.setLong(2, historyEvent.getFinishedMaps());
            entityPS.setLong(3, historyEvent.getFinishedReduces());
            entityPS.setString(4, historyEvent.getStatus());
            entityPS.setString(5, historyEvent.getJobId().toString());
            entityPS.executeUpdate();
        } catch (java.sql.SQLException sqle) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.info((((("Failed to store " + historyEvent.getEventType()) + " for job ") + historyEvent.getJobId()) + " into ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.JOB_TABLE, sqle);
        }
        updateJobStatsAtFinish(historyEvent.getJobId().toString());
    }

    private void processTaskStartedEvent(java.sql.PreparedStatement entityPS, org.apache.log4j.spi.LoggingEvent logEvent, org.apache.hadoop.tools.rumen.TaskStartedEvent historyEvent) {
        try {
            entityPS.setString(1, historyEvent.getTaskId().getJobID().toString());
            entityPS.setString(2, historyEvent.getTaskType().toString());
            entityPS.setString(3, historyEvent.getSplitLocations());
            entityPS.setLong(4, historyEvent.getStartTime());
            entityPS.setString(5, historyEvent.getTaskId().toString());
            entityPS.executeUpdate();
        } catch (java.sql.SQLException sqle) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.info((((("Failed to store " + historyEvent.getEventType()) + " for task ") + historyEvent.getTaskId()) + " into ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASK_TABLE, sqle);
        }
    }

    private void processTaskFinishedEvent(java.sql.PreparedStatement entityPS, org.apache.log4j.spi.LoggingEvent logEvent, org.apache.hadoop.tools.rumen.TaskFinishedEvent historyEvent) {
        try {
            entityPS.setString(1, historyEvent.getTaskId().getJobID().toString());
            entityPS.setString(2, historyEvent.getTaskType().toString());
            entityPS.setString(3, historyEvent.getTaskStatus());
            entityPS.setLong(4, historyEvent.getFinishTime());
            entityPS.setString(5, historyEvent.getTaskId().toString());
            entityPS.executeUpdate();
        } catch (java.sql.SQLException sqle) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.info((((("Failed to store " + historyEvent.getEventType()) + " for task ") + historyEvent.getTaskId()) + " into ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASK_TABLE, sqle);
        }
    }

    private void processTaskFailedEvent(java.sql.PreparedStatement entityPS, org.apache.log4j.spi.LoggingEvent logEvent, org.apache.hadoop.tools.rumen.TaskFailedEvent historyEvent) {
        try {
            entityPS.setString(1, historyEvent.getTaskId().getJobID().toString());
            entityPS.setString(2, historyEvent.getTaskType().toString());
            entityPS.setString(3, historyEvent.getTaskStatus());
            entityPS.setLong(4, historyEvent.getFinishTime());
            entityPS.setString(5, historyEvent.getError());
            if (historyEvent.getFailedAttemptID() != null) {
                entityPS.setString(6, historyEvent.getFailedAttemptID().toString());
            } else {
                entityPS.setString(6, "task_na");
            }
            entityPS.setString(7, historyEvent.getTaskId().toString());
            entityPS.executeUpdate();
        } catch (java.sql.SQLException sqle) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.info((((("Failed to store " + historyEvent.getEventType()) + " for task ") + historyEvent.getTaskId()) + " into ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASK_TABLE, sqle);
        }
    }

    private void processTaskAttemptStartedEvent(java.sql.PreparedStatement entityPS, org.apache.log4j.spi.LoggingEvent logEvent, org.apache.hadoop.tools.rumen.TaskAttemptStartedEvent historyEvent) {
        try {
            entityPS.setString(1, historyEvent.getTaskId().getJobID().toString());
            entityPS.setString(2, historyEvent.getTaskId().toString());
            entityPS.setString(3, historyEvent.getTaskType().toString());
            entityPS.setLong(4, historyEvent.getStartTime());
            entityPS.setString(5, historyEvent.getTrackerName());
            entityPS.setString(6, historyEvent.getLocality().toString());
            entityPS.setString(7, historyEvent.getAvataar().toString());
            entityPS.setString(8, historyEvent.getTaskAttemptId().toString());
            entityPS.executeUpdate();
        } catch (java.sql.SQLException sqle) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.info((((("Failed to store " + historyEvent.getEventType()) + " for taskAttempt ") + historyEvent.getTaskAttemptId()) + " into ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE, sqle);
        }
    }

    private void processTaskAttemptFinishedEvent(java.sql.PreparedStatement entityPS, org.apache.log4j.spi.LoggingEvent logEvent, org.apache.hadoop.tools.rumen.TaskAttemptFinishedEvent historyEvent) {
        if ((historyEvent.getTaskType() == org.apache.hadoop.mapreduce.TaskType.MAP) || (historyEvent.getTaskType() == org.apache.hadoop.mapreduce.TaskType.REDUCE)) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.debug("Ignoring TaskAttemptFinishedEvent for " + historyEvent.getTaskType());
            return;
        }
        try {
            entityPS.setString(1, historyEvent.getTaskId().getJobID().toString());
            entityPS.setString(2, historyEvent.getTaskId().toString());
            entityPS.setString(3, historyEvent.getTaskType().toString());
            entityPS.setLong(4, historyEvent.getFinishTime());
            entityPS.setString(5, historyEvent.getTaskStatus());
            entityPS.setString(6, historyEvent.getHostname());
            entityPS.setString(7, historyEvent.getAttemptId().toString());
            entityPS.executeUpdate();
        } catch (java.sql.SQLException sqle) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.info((((("Failed to store " + historyEvent.getEventType()) + " for taskAttempt ") + historyEvent.getAttemptId()) + " into ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE, sqle);
        }
    }

    private void processTaskAttemptUnsuccessfulEvent(java.sql.PreparedStatement entityPS, org.apache.log4j.spi.LoggingEvent logEvent, org.apache.hadoop.tools.rumen.TaskAttemptUnsuccessfulCompletionEvent historyEvent) {
        try {
            entityPS.setString(1, historyEvent.getTaskId().getJobID().toString());
            entityPS.setString(2, historyEvent.getTaskId().toString());
            entityPS.setString(3, historyEvent.getTaskType().toString());
            entityPS.setLong(4, historyEvent.getFinishTime());
            entityPS.setString(5, historyEvent.getTaskStatus());
            entityPS.setString(6, historyEvent.getHostname());
            entityPS.setString(7, historyEvent.getError());
            entityPS.setString(8, historyEvent.getTaskAttemptId().toString());
            entityPS.executeUpdate();
        } catch (java.sql.SQLException sqle) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.info((((("Failed to store " + historyEvent.getEventType()) + " for taskAttempt ") + historyEvent.getTaskAttemptId()) + " into ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE, sqle);
        }
    }

    private void processMapAttemptFinishedEvent(java.sql.PreparedStatement entityPS, org.apache.log4j.spi.LoggingEvent logEvent, org.apache.hadoop.tools.rumen.MapAttemptFinishedEvent historyEvent) {
        if (historyEvent.getTaskType() != org.apache.hadoop.mapreduce.TaskType.MAP) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.debug("Ignoring MapAttemptFinishedEvent for " + historyEvent.getTaskType());
            return;
        }
        long[] ioBytes = org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.getInputOutputBytes(historyEvent.getCounters());
        try {
            entityPS.setString(1, historyEvent.getTaskId().getJobID().toString());
            entityPS.setString(2, historyEvent.getTaskId().toString());
            entityPS.setString(3, historyEvent.getTaskType().toString());
            entityPS.setLong(4, historyEvent.getMapFinishTime());
            entityPS.setLong(5, historyEvent.getFinishTime());
            entityPS.setLong(6, ioBytes[0]);
            entityPS.setLong(7, ioBytes[1]);
            entityPS.setString(8, historyEvent.getTaskStatus());
            entityPS.setString(9, historyEvent.getHostname());
            entityPS.setString(10, historyEvent.getAttemptId().toString());
            entityPS.executeUpdate();
        } catch (java.sql.SQLException sqle) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.info((((("Failed to store " + historyEvent.getEventType()) + " for taskAttempt ") + historyEvent.getAttemptId()) + " into ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE, sqle);
        }
    }

    private void processReduceAttemptFinishedEvent(java.sql.PreparedStatement entityPS, org.apache.log4j.spi.LoggingEvent logEvent, org.apache.hadoop.tools.rumen.ReduceAttemptFinishedEvent historyEvent) {
        if (historyEvent.getTaskType() != org.apache.hadoop.mapreduce.TaskType.REDUCE) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.debug("Ignoring ReduceAttemptFinishedEvent for " + historyEvent.getTaskType());
            return;
        }
        long[] ioBytes = org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.getInputOutputBytes(historyEvent.getCounters());
        try {
            entityPS.setString(1, historyEvent.getTaskId().getJobID().toString());
            entityPS.setString(2, historyEvent.getTaskId().toString());
            entityPS.setString(3, historyEvent.getTaskType().toString());
            entityPS.setLong(4, historyEvent.getShuffleFinishTime());
            entityPS.setLong(5, historyEvent.getSortFinishTime());
            entityPS.setLong(6, historyEvent.getFinishTime());
            entityPS.setLong(7, ioBytes[0]);
            entityPS.setLong(8, ioBytes[1]);
            entityPS.setString(9, historyEvent.getTaskStatus());
            entityPS.setString(10, historyEvent.getHostname());
            entityPS.setString(11, historyEvent.getAttemptId().toString());
            entityPS.executeUpdate();
        } catch (java.sql.SQLException sqle) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.LOG.info((((("Failed to store " + historyEvent.getEventType()) + " for taskAttempt ") + historyEvent.getAttemptId()) + " into ") + org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater.TASKATTEMPT_TABLE, sqle);
        }
    }

    public static long[] getInputOutputBytes(org.apache.hadoop.tools.rumen.JhCounters counters) {
        long inputBytes = 0;
        long outputBytes = 0;
        if (counters != null) {
            for (org.apache.hadoop.tools.rumen.JhCounterGroup counterGroup : counters.groups) {
                if (counterGroup.name.equals("FileSystemCounters")) {
                    for (org.apache.hadoop.tools.rumen.JhCounter counter : counterGroup.counts) {
                        if (counter.name.equals("HDFS_BYTES_READ") || counter.name.equals("FILE_BYTES_READ"))
                            inputBytes += counter.value;
                        else if (counter.name.equals("HDFS_BYTES_WRITTEN") || counter.name.equals("FILE_BYTES_WRITTEN"))
                            outputBytes += counter.value;

                    }
                }
            }
        }
        return new long[]{ inputBytes, outputBytes };
    }

    @java.lang.Override
    public void update(org.apache.log4j.spi.LoggingEvent originalEvent, java.lang.Object parsedEvent) throws java.io.IOException {
        try {
            doUpdates(originalEvent, parsedEvent);
        } catch (java.sql.SQLException sqle) {
            throw new java.io.IOException(sqle);
        }
    }
}