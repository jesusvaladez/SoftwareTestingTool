package org.apache.ambari.server.state.quicklinksprofile;
import javax.annotation.Nonnull;
public class ShowAllLinksVisibilityController implements org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityController {
    @java.lang.Override
    public boolean isVisible(@javax.annotation.Nonnull
    java.lang.String service, @javax.annotation.Nonnull
    org.apache.ambari.server.state.quicklinks.Link quickLink) {
        return true;
    }

    @java.lang.Override
    public java.util.Optional<java.lang.String> getUrlOverride(@javax.annotation.Nonnull
    java.lang.String service, @javax.annotation.Nonnull
    org.apache.ambari.server.state.quicklinks.Link quickLink) {
        return java.util.Optional.empty();
    }
}