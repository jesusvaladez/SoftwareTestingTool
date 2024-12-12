package org.apache.ambari.server.utils;
public class DateUtils {
    public static final java.lang.String ALLOWED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssz";

    public static java.lang.String convertToReadableTime(java.lang.Long timestamp) {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new java.util.Date(timestamp));
    }

    public static java.lang.Long convertToTimestamp(java.lang.String time, java.lang.String format) {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(format);
        try {
            java.util.Date date = dateFormat.parse(time);
            return date.getTime();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static java.util.Date convertToDate(java.lang.String date) throws java.text.ParseException {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(org.apache.ambari.server.utils.DateUtils.ALLOWED_DATE_FORMAT);
        return sdf.parse(date);
    }

    public static java.lang.String convertDateToString(java.util.Date date) throws java.text.ParseException {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(org.apache.ambari.server.utils.DateUtils.ALLOWED_DATE_FORMAT);
        return sdf.format(date);
    }

    public static java.lang.Long getDateDifferenceInMinutes(java.util.Date oldTime) {
        long diff = java.lang.Math.abs(oldTime.getTime() - new java.util.Date().getTime());
        return (diff / (60 * 1000)) % 60;
    }

    public static boolean isFutureTime(java.util.Date time) {
        java.util.Date now = new java.util.Date();
        return time.after(now);
    }

    public static java.util.Date getDateSpecifiedTimeAgo(java.lang.String periodString) {
        java.lang.String pattern = "((\\d+)([hdwmy]))";
        java.util.regex.Pattern findPattern = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Pattern matchPattern = java.util.regex.Pattern.compile(pattern + "+");
        java.util.Map<java.lang.String, java.lang.Integer> qualifierToConstant = new java.util.HashMap<java.lang.String, java.lang.Integer>() {
            {
                put("h", java.util.Calendar.HOUR);
                put("d", java.util.Calendar.DATE);
                put("w", java.util.Calendar.WEEK_OF_YEAR);
                put("m", java.util.Calendar.MONTH);
                put("y", java.util.Calendar.YEAR);
            }
        };
        if (!matchPattern.matcher(periodString).matches()) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Invalid string for indicating period %s", periodString));
        }
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        java.util.regex.Matcher m = findPattern.matcher(periodString);
        while (m.find()) {
            int amount = java.lang.Integer.parseInt(m.group(2));
            int unit = qualifierToConstant.get(m.group(3));
            calendar.add(unit, -amount);
        } 
        return calendar.getTime();
    }
}