package org.apache.ambari.server.view;
public class DefaultMasker implements org.apache.ambari.view.Masker {
    @java.lang.Override
    public java.lang.String mask(java.lang.String value) throws org.apache.ambari.view.MaskException {
        try {
            return org.apache.commons.codec.binary.Base64.encodeBase64String(value.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            throw new org.apache.ambari.view.MaskException("UTF-8 is not supported", e);
        }
    }

    @java.lang.Override
    public java.lang.String unmask(java.lang.String value) throws org.apache.ambari.view.MaskException {
        try {
            return new java.lang.String(org.apache.commons.codec.binary.Base64.decodeBase64(value), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            throw new org.apache.ambari.view.MaskException("UTF-8 is not supported", e);
        }
    }
}