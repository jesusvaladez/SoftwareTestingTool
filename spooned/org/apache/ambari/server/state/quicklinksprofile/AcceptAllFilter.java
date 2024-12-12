package org.apache.ambari.server.state.quicklinksprofile;
public class AcceptAllFilter extends org.apache.ambari.server.state.quicklinksprofile.Filter {
    @java.lang.Override
    public boolean accept(org.apache.ambari.server.state.quicklinks.Link link) {
        return true;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.quicklinksprofile.AcceptAllFilter that = ((org.apache.ambari.server.state.quicklinksprofile.AcceptAllFilter) (o));
        return isVisible() == that.isVisible();
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(isVisible());
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((getClass().getSimpleName() + " (visible=") + isVisible()) + ")";
    }
}