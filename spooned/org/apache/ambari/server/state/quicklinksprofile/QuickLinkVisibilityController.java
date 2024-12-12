package org.apache.ambari.server.state.quicklinksprofile;
import javax.annotation.Nonnull;
public interface QuickLinkVisibilityController {
    boolean isVisible(@javax.annotation.Nonnull
    java.lang.String service, @javax.annotation.Nonnull
    org.apache.ambari.server.state.quicklinks.Link quickLink);

    java.util.Optional<java.lang.String> getUrlOverride(@javax.annotation.Nonnull
    java.lang.String service, @javax.annotation.Nonnull
    org.apache.ambari.server.state.quicklinks.Link quickLink);
}