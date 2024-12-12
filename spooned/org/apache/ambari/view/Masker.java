package org.apache.ambari.view;
public interface Masker {
    public java.lang.String mask(java.lang.String value) throws org.apache.ambari.view.MaskException;

    public java.lang.String unmask(java.lang.String value) throws org.apache.ambari.view.MaskException;
}