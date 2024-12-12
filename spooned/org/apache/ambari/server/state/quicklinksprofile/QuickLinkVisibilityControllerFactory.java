package org.apache.ambari.server.state.quicklinksprofile;
import javax.annotation.Nullable;
public class QuickLinkVisibilityControllerFactory {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerFactory.class);

    public static org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityController get(@javax.annotation.Nullable
    java.lang.String quickLinkProfileJson) {
        if (null == quickLinkProfileJson) {
            org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerFactory.LOG.info("No quick link profile is set, will display all quicklinks.");
            return new org.apache.ambari.server.state.quicklinksprofile.ShowAllLinksVisibilityController();
        }
        try {
            org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile = new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileParser().parse(quickLinkProfileJson.getBytes());
            return new org.apache.ambari.server.state.quicklinksprofile.DefaultQuickLinkVisibilityController(profile);
        } catch (java.io.IOException | org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException ex) {
            org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityControllerFactory.LOG.error("Unable to parse quick link profile json: " + quickLinkProfileJson, ex);
            return new org.apache.ambari.server.state.quicklinksprofile.ShowAllLinksVisibilityController();
        }
    }
}