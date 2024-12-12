package org.apache.oozie.ambari.view;
public interface Constants {
    java.lang.String STATUS_FAILED = "failed";

    java.lang.String STATUS_OK = "ok";

    java.lang.String STATUS_KEY = "status";

    java.lang.String MESSAGE_KEY = "message";

    java.lang.String WF_DRAFT_EXTENSION = ".wfdraft";

    java.lang.String WF_EXTENSION = ".xml";

    java.lang.String DEFAULT_WORKFLOW_FILENAME = "workflow";

    java.lang.String DEFAULT_COORDINATOR_FILENAME = "coordinator";

    java.lang.String DEFAULT_BUNDLE_FILENAME = "bundle";

    java.lang.String DEFAULT_DRAFT_FILENAME = "workflow" + org.apache.oozie.ambari.view.Constants.WF_DRAFT_EXTENSION;

    java.lang.String WF_ASSET_EXTENSION = ".wfasset";

    java.lang.String DEFAULT_WORKFLOW_ASSET_FILENAME = "asset" + org.apache.oozie.ambari.view.Constants.WF_ASSET_EXTENSION;
}