package org.apache.ambari.view.pig.persistence.utils;
import org.apache.commons.configuration.Configuration;
@java.lang.Deprecated
public class ContextConfigurationAdapter implements org.apache.commons.configuration.Configuration {
    private org.apache.ambari.view.ViewContext context;

    public ContextConfigurationAdapter(org.apache.ambari.view.ViewContext context) {
        this.context = context;
    }

    @java.lang.Override
    public org.apache.commons.configuration.Configuration subset(java.lang.String prefix) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean isEmpty() {
        return context.getInstanceData().isEmpty();
    }

    @java.lang.Override
    public boolean containsKey(java.lang.String s) {
        java.util.Map<java.lang.String, java.lang.String> data = context.getInstanceData();
        return data.containsKey(s);
    }

    @java.lang.Override
    public void addProperty(java.lang.String s, java.lang.Object o) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void setProperty(java.lang.String s, java.lang.Object o) {
        context.putInstanceData(s, o.toString());
    }

    @java.lang.Override
    public void clearProperty(java.lang.String key) {
        context.removeInstanceData(key);
    }

    @java.lang.Override
    public void clear() {
        for (java.lang.String key : context.getInstanceData().keySet())
            context.removeInstanceData(key);

    }

    @java.lang.Override
    public java.lang.Object getProperty(java.lang.String key) {
        return context.getInstanceData(key);
    }

    @java.lang.Override
    public java.util.Iterator getKeys(java.lang.String s) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.Iterator getKeys() {
        return context.getInstanceData().keySet().iterator();
    }

    @java.lang.Override
    public java.util.Properties getProperties(java.lang.String s) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean getBoolean(java.lang.String s) {
        return getBoolean(s, null);
    }

    @java.lang.Override
    public boolean getBoolean(java.lang.String s, boolean b) {
        return getBoolean(s, ((java.lang.Boolean) (b)));
    }

    @java.lang.Override
    public java.lang.Boolean getBoolean(java.lang.String s, java.lang.Boolean aBoolean) {
        java.lang.String data = context.getInstanceData(s);
        return data != null ? java.lang.Boolean.parseBoolean(data) : aBoolean;
    }

    @java.lang.Override
    public byte getByte(java.lang.String s) {
        return getByte(s, null);
    }

    @java.lang.Override
    public byte getByte(java.lang.String s, byte b) {
        return getByte(s, ((java.lang.Byte) (b)));
    }

    @java.lang.Override
    public java.lang.Byte getByte(java.lang.String s, java.lang.Byte aByte) {
        java.lang.String data = context.getInstanceData(s);
        return data != null ? java.lang.Byte.parseByte(data) : aByte;
    }

    @java.lang.Override
    public double getDouble(java.lang.String s) {
        return getDouble(s, null);
    }

    @java.lang.Override
    public double getDouble(java.lang.String s, double v) {
        return getDouble(s, ((java.lang.Double) (v)));
    }

    @java.lang.Override
    public java.lang.Double getDouble(java.lang.String s, java.lang.Double aDouble) {
        java.lang.String data = context.getInstanceData(s);
        return data != null ? java.lang.Double.parseDouble(data) : aDouble;
    }

    @java.lang.Override
    public float getFloat(java.lang.String s) {
        return getFloat(s, null);
    }

    @java.lang.Override
    public float getFloat(java.lang.String s, float v) {
        return getFloat(s, ((java.lang.Float) (v)));
    }

    @java.lang.Override
    public java.lang.Float getFloat(java.lang.String s, java.lang.Float aFloat) {
        java.lang.String data = context.getInstanceData(s);
        return data != null ? java.lang.Float.parseFloat(data) : aFloat;
    }

    @java.lang.Override
    public int getInt(java.lang.String s) {
        return getInteger(s, null);
    }

    @java.lang.Override
    public int getInt(java.lang.String s, int i) {
        return getInteger(s, i);
    }

    @java.lang.Override
    public java.lang.Integer getInteger(java.lang.String s, java.lang.Integer integer) {
        java.lang.String data = context.getInstanceData(s);
        return data != null ? java.lang.Integer.parseInt(data) : integer;
    }

    @java.lang.Override
    public long getLong(java.lang.String s) {
        return getLong(s, null);
    }

    @java.lang.Override
    public long getLong(java.lang.String s, long l) {
        return getLong(s, ((java.lang.Long) (l)));
    }

    @java.lang.Override
    public java.lang.Long getLong(java.lang.String s, java.lang.Long aLong) {
        java.lang.String data = context.getInstanceData(s);
        return data != null ? java.lang.Long.parseLong(data) : aLong;
    }

    @java.lang.Override
    public short getShort(java.lang.String s) {
        return getShort(s, null);
    }

    @java.lang.Override
    public short getShort(java.lang.String s, short i) {
        return getShort(s, ((java.lang.Short) (i)));
    }

    @java.lang.Override
    public java.lang.Short getShort(java.lang.String s, java.lang.Short aShort) {
        java.lang.String data = context.getInstanceData(s);
        return data != null ? java.lang.Short.parseShort(data) : aShort;
    }

    @java.lang.Override
    public java.math.BigDecimal getBigDecimal(java.lang.String s) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.math.BigDecimal getBigDecimal(java.lang.String s, java.math.BigDecimal bigDecimal) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.math.BigInteger getBigInteger(java.lang.String s) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.math.BigInteger getBigInteger(java.lang.String s, java.math.BigInteger bigInteger) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String getString(java.lang.String s) {
        return context.getInstanceData(s);
    }

    @java.lang.Override
    public java.lang.String getString(java.lang.String s, java.lang.String s2) {
        java.lang.String data = getString(s);
        return data != null ? data : s2;
    }

    @java.lang.Override
    public java.lang.String[] getStringArray(java.lang.String s) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.List getList(java.lang.String s) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.List getList(java.lang.String s, java.util.List list) {
        throw new java.lang.UnsupportedOperationException();
    }
}