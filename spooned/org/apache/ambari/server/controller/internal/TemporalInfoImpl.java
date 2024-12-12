package org.apache.ambari.server.controller.internal;
public class TemporalInfoImpl implements org.apache.ambari.server.controller.spi.TemporalInfo {
    private final long m_startTime;

    private final long m_endTime;

    private final long m_step;

    private final long startTimeMillis;

    private final long endTimeMillis;

    public TemporalInfoImpl(long startTime, long endTime, long step) {
        m_startTime = startTime;
        m_endTime = endTime;
        m_step = step;
        if (startTime < 9999999999L) {
            startTimeMillis = startTime * 1000;
        } else {
            startTimeMillis = startTime;
        }
        if (endTime < 9999999999L) {
            endTimeMillis = endTime * 1000;
        } else {
            endTimeMillis = endTime;
        }
    }

    @java.lang.Override
    public java.lang.Long getStartTime() {
        return m_startTime;
    }

    @java.lang.Override
    public java.lang.Long getEndTime() {
        return m_endTime;
    }

    @java.lang.Override
    public java.lang.Long getStep() {
        return m_step;
    }

    @java.lang.Override
    public java.lang.Long getStartTimeMillis() {
        return startTimeMillis;
    }

    @java.lang.Override
    public java.lang.Long getEndTimeMillis() {
        return endTimeMillis;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((("TemporalInfoImpl{" + "m_startTime = ") + new java.util.Date(getStartTimeMillis())) + ", m_endTime = ") + new java.util.Date(getEndTimeMillis())) + ", m_step = ") + m_step) + '}';
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.controller.internal.TemporalInfoImpl that = ((org.apache.ambari.server.controller.internal.TemporalInfoImpl) (o));
        return ((m_endTime == that.m_endTime) && (m_startTime == that.m_startTime)) && (m_step == that.m_step);
    }

    @java.lang.Override
    public int hashCode() {
        int result = ((int) (m_startTime ^ (m_startTime >>> 32)));
        result = (31 * result) + ((int) (m_endTime ^ (m_endTime >>> 32)));
        result = (31 * result) + ((int) (m_step ^ (m_step >>> 32)));
        return result;
    }
}