package org.apache.ambari.server.utils;
public class TestDateUtils {
    @org.junit.Test
    public void testConvertToReadableTime() throws java.lang.Exception {
        java.lang.Long timestamp = 1389125737000L;
        java.lang.String readableTime = org.apache.ambari.server.utils.DateUtils.convertToReadableTime(timestamp);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        junit.framework.Assert.assertEquals("2014-01-07 20:15:37", sdf.format(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(readableTime)));
    }

    @org.junit.Test
    public void testConvertToDate() throws java.lang.Exception {
        java.lang.String time = "2013-11-18T14:29:29-0000";
        java.util.Date date = org.apache.ambari.server.utils.DateUtils.convertToDate(time);
        junit.framework.Assert.assertNotNull(date);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        calendar.set(java.util.Calendar.YEAR, 2013);
        calendar.set(java.util.Calendar.MONTH, java.util.Calendar.NOVEMBER);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 18);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 14);
        calendar.set(java.util.Calendar.MINUTE, 29);
        calendar.set(java.util.Calendar.SECOND, 29);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        junit.framework.Assert.assertEquals(0, date.compareTo(calendar.getTime()));
    }

    @org.junit.Test
    public void testGetDateDifferenceInMinutes() throws java.lang.Exception {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        calendar.set(java.util.Calendar.YEAR, 2013);
        calendar.set(java.util.Calendar.MONTH, java.util.Calendar.NOVEMBER);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 18);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 14);
        calendar.set(java.util.Calendar.MINUTE, 49);
        calendar.set(java.util.Calendar.SECOND, 29);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        java.lang.String time = "2013-11-18T14:29:29-0000";
        java.util.Date date = org.apache.ambari.server.utils.DateUtils.convertToDate(time);
        java.lang.Long diff = (java.lang.Math.abs(date.getTime() - calendar.getTimeInMillis()) / (60 * 1000)) % 60;
        junit.framework.Assert.assertEquals(java.lang.Long.valueOf(20L).longValue(), diff.longValue());
    }
}