package org.apache.ambari.server.state.stack;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.apache.commons.lang.StringUtils;
public class TrimmingAdapter extends javax.xml.bind.annotation.adapters.XmlAdapter<java.lang.String, java.lang.String> {
    @java.lang.Override
    public java.lang.String marshal(java.lang.String v) throws java.lang.Exception {
        return org.apache.commons.lang.StringUtils.trim(v);
    }

    @java.lang.Override
    public java.lang.String unmarshal(java.lang.String v) throws java.lang.Exception {
        return org.apache.commons.lang.StringUtils.trim(v);
    }
}