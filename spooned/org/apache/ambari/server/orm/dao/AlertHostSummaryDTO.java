package org.apache.ambari.server.orm.dao;
public class AlertHostSummaryDTO {
    private final int m_warningCount;

    private final int m_criticalCount;

    private final int m_unknownCount;

    private final int m_okCount;

    public AlertHostSummaryDTO(int okCount, int unknownCount, int warningCount, int criticalCount) {
        m_okCount = okCount;
        m_unknownCount = unknownCount;
        m_warningCount = warningCount;
        m_criticalCount = criticalCount;
    }

    public int getWarningCount() {
        return m_warningCount;
    }

    public int getCriticalCount() {
        return m_criticalCount;
    }

    public int getUnknownCount() {
        return m_unknownCount;
    }

    public int getOkCount() {
        return m_okCount;
    }
}