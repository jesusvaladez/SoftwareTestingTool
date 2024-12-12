package org.apache.ambari.view.pig.templeton.client;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.MultivaluedMapImpl;
public class TempletonApi {
    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.templeton.client.TempletonApi.class);

    protected com.sun.jersey.api.client.WebResource service;

    private java.lang.String doAs;

    private org.apache.ambari.view.ViewContext context;

    public TempletonApi(java.lang.String api, java.lang.String doAs, org.apache.ambari.view.ViewContext context) {
        this.doAs = doAs;
        this.context = context;
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, java.lang.Boolean.TRUE);
        com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create(config);
        this.service = client.resource(api);
    }

    public TempletonApi(java.lang.String api, org.apache.ambari.view.ViewContext context) {
        this(api, null, context);
    }

    public org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData runPigQuery(java.lang.String execute, java.io.File pigFile, java.lang.String statusDir, java.lang.String arg) throws java.io.IOException {
        com.sun.jersey.core.util.MultivaluedMapImpl data = new com.sun.jersey.core.util.MultivaluedMapImpl();
        if (execute != null)
            data.add("execute", execute);

        if (pigFile != null)
            data.add("file", pigFile.toString());

        if (statusDir != null)
            data.add("statusdir", statusDir);

        if ((arg != null) && (!arg.isEmpty())) {
            for (java.lang.String arg1 : arg.split("\t")) {
                data.add("arg", arg1);
            }
        }
        org.apache.ambari.view.pig.templeton.client.JSONRequest<org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData> request = new org.apache.ambari.view.pig.templeton.client.JSONRequest<org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData>(service.path("pig"), org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData.class, doAs, doAs, context);
        return request.post(data);
    }

    public org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData runPigQuery(java.io.File pigFile, java.lang.String statusDir, java.lang.String arg) throws java.io.IOException {
        return runPigQuery(null, pigFile, statusDir, arg);
    }

    public org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData runPigQuery(java.lang.String execute, java.lang.String statusDir, java.lang.String arg) throws java.io.IOException {
        return runPigQuery(execute, null, statusDir, arg);
    }

    public org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData runPigQuery(java.lang.String execute) throws java.io.IOException {
        return runPigQuery(execute, null, null, null);
    }

    public org.apache.ambari.view.pig.templeton.client.TempletonApi.JobInfo checkJob(java.lang.String jobId) throws java.io.IOException {
        org.apache.ambari.view.pig.templeton.client.JSONRequest<org.apache.ambari.view.pig.templeton.client.TempletonApi.JobInfo> request = new org.apache.ambari.view.pig.templeton.client.JSONRequest<org.apache.ambari.view.pig.templeton.client.TempletonApi.JobInfo>(service.path("jobs").path(jobId), org.apache.ambari.view.pig.templeton.client.TempletonApi.JobInfo.class, doAs, doAs, context);
        return request.get();
    }

    public void killJob(java.lang.String jobId) throws java.io.IOException {
        org.apache.ambari.view.pig.templeton.client.JSONRequest<org.apache.ambari.view.pig.templeton.client.TempletonApi.JobInfo> request = new org.apache.ambari.view.pig.templeton.client.JSONRequest<org.apache.ambari.view.pig.templeton.client.TempletonApi.JobInfo>(service.path("jobs").path(jobId), org.apache.ambari.view.pig.templeton.client.TempletonApi.JobInfo.class, doAs, doAs, context);
        try {
            request.delete();
        } catch (java.io.IOException e) {
            org.apache.ambari.view.pig.templeton.client.TempletonApi.LOG.error("Ignoring 500 response from webhcat (see HIVE-5835)");
        }
    }

    public org.apache.ambari.view.pig.templeton.client.TempletonApi.Status status() throws java.io.IOException {
        org.apache.ambari.view.pig.templeton.client.JSONRequest<org.apache.ambari.view.pig.templeton.client.TempletonApi.Status> request = new org.apache.ambari.view.pig.templeton.client.JSONRequest<org.apache.ambari.view.pig.templeton.client.TempletonApi.Status>(service.path("status"), org.apache.ambari.view.pig.templeton.client.TempletonApi.Status.class, doAs, doAs, context);
        return request.get();
    }

    public class Status {
        public java.lang.String status;

        public java.lang.String version;
    }

    public class JobData {
        public java.lang.String id;
    }

    public class JobInfo {
        public java.util.Map<java.lang.String, java.lang.Object> status;

        public java.util.Map<java.lang.String, java.lang.Object> profile;

        public java.util.Map<java.lang.String, java.lang.Object> userargs;

        public java.lang.String id;

        public java.lang.String parentId;

        public java.lang.String percentComplete;

        public java.lang.Integer exitValue;

        public java.lang.String user;

        public java.lang.String callback;

        public java.lang.String completed;
    }
}