package org.apache.ambari.server.state.action;
public class JobTest {
    private org.apache.ambari.server.state.action.Action createNewJob(long id, java.lang.String jobName, long startTime) {
        org.apache.ambari.server.state.action.ActionId jId = new org.apache.ambari.server.state.action.ActionId(id, new org.apache.ambari.server.state.action.ActionType(jobName));
        org.apache.ambari.server.state.action.Action job = new org.apache.ambari.server.state.action.ActionImpl(jId, startTime);
        return job;
    }

    private org.apache.ambari.server.state.action.Action getRunningJob(long id, java.lang.String jobName, long startTime) throws java.lang.Exception {
        org.apache.ambari.server.state.action.Action job = createNewJob(id, jobName, startTime);
        verifyProgressUpdate(job, ++startTime);
        return job;
    }

    private org.apache.ambari.server.state.action.Action getCompletedJob(long id, java.lang.String jobName, long startTime, boolean failedJob) throws java.lang.Exception {
        org.apache.ambari.server.state.action.Action job = getRunningJob(1, "JobNameFoo", startTime);
        completeJob(job, failedJob, ++startTime);
        return job;
    }

    private void verifyNewJob(org.apache.ambari.server.state.action.Action job, long startTime) {
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.action.ActionState.INIT, job.getState());
        org.junit.Assert.assertEquals(startTime, job.getStartTime());
    }

    @org.junit.Test
    public void testNewJob() {
        long currentTime = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.state.action.Action job = createNewJob(1, "JobNameFoo", currentTime);
        verifyNewJob(job, currentTime);
    }

    private void verifyProgressUpdate(org.apache.ambari.server.state.action.Action job, long updateTime) throws java.lang.Exception {
        org.apache.ambari.server.state.action.ActionProgressUpdateEvent e = new org.apache.ambari.server.state.action.ActionProgressUpdateEvent(job.getId(), updateTime);
        job.handleEvent(e);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.action.ActionState.IN_PROGRESS, job.getState());
        org.junit.Assert.assertEquals(updateTime, job.getLastUpdateTime());
    }

    @org.junit.Test
    public void testJobProgressUpdates() throws java.lang.Exception {
        long currentTime = 1;
        org.apache.ambari.server.state.action.Action job = createNewJob(1, "JobNameFoo", currentTime);
        verifyNewJob(job, currentTime);
        verifyProgressUpdate(job, ++currentTime);
        verifyProgressUpdate(job, ++currentTime);
        verifyProgressUpdate(job, ++currentTime);
    }

    private void completeJob(org.apache.ambari.server.state.action.Action job, boolean failJob, long endTime) throws java.lang.Exception {
        org.apache.ambari.server.state.action.ActionEvent e = null;
        org.apache.ambari.server.state.action.ActionState endState = null;
        if (failJob) {
            e = new org.apache.ambari.server.state.action.ActionFailedEvent(job.getId(), endTime);
            endState = org.apache.ambari.server.state.action.ActionState.FAILED;
        } else {
            e = new org.apache.ambari.server.state.action.ActionCompletedEvent(job.getId(), endTime);
            endState = org.apache.ambari.server.state.action.ActionState.COMPLETED;
        }
        job.handleEvent(e);
        org.junit.Assert.assertEquals(endState, job.getState());
        org.junit.Assert.assertEquals(endTime, job.getLastUpdateTime());
        org.junit.Assert.assertEquals(endTime, job.getCompletionTime());
    }

    @org.junit.Test
    public void testJobSuccessfulCompletion() throws java.lang.Exception {
        long currentTime = 1;
        org.apache.ambari.server.state.action.Action job = getRunningJob(1, "JobNameFoo", currentTime);
        completeJob(job, false, ++currentTime);
    }

    @org.junit.Test
    public void testJobFailedCompletion() throws java.lang.Exception {
        long currentTime = 1;
        org.apache.ambari.server.state.action.Action job = getRunningJob(1, "JobNameFoo", currentTime);
        completeJob(job, true, ++currentTime);
    }

    @org.junit.Test
    public void completeNewJob() throws java.lang.Exception {
        long currentTime = 1;
        org.apache.ambari.server.state.action.Action job = createNewJob(1, "JobNameFoo", currentTime);
        verifyNewJob(job, currentTime);
        completeJob(job, false, ++currentTime);
    }

    @org.junit.Test
    public void failNewJob() throws java.lang.Exception {
        long currentTime = 1;
        org.apache.ambari.server.state.action.Action job = createNewJob(1, "JobNameFoo", currentTime);
        verifyNewJob(job, currentTime);
        completeJob(job, true, ++currentTime);
    }

    @org.junit.Test
    public void reInitCompletedJob() throws java.lang.Exception {
        org.apache.ambari.server.state.action.Action job = getCompletedJob(1, "JobNameFoo", 1, false);
        org.apache.ambari.server.state.action.ActionId jId = new org.apache.ambari.server.state.action.ActionId(2, new org.apache.ambari.server.state.action.ActionType("JobNameFoo"));
        org.apache.ambari.server.state.action.ActionInitEvent e = new org.apache.ambari.server.state.action.ActionInitEvent(jId, 100);
        job.handleEvent(e);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.action.ActionState.INIT, job.getState());
        org.junit.Assert.assertEquals(100, job.getStartTime());
        org.junit.Assert.assertEquals(-1, job.getLastUpdateTime());
        org.junit.Assert.assertEquals(-1, job.getCompletionTime());
        org.junit.Assert.assertEquals(2, job.getId().actionId);
    }
}