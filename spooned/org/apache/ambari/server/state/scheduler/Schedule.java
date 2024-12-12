package org.apache.ambari.server.state.scheduler;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
public class Schedule {
    private java.lang.String minutes;

    private java.lang.String hours;

    private java.lang.String daysOfMonth;

    private java.lang.String month;

    private java.lang.String dayOfWeek;

    private java.lang.String year;

    private java.lang.String startTime;

    private java.lang.String endTime;

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("minutes")
    public java.lang.String getMinutes() {
        return minutes;
    }

    public void setMinutes(java.lang.String minutes) {
        this.minutes = minutes;
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("hours")
    public java.lang.String getHours() {
        return hours;
    }

    public void setHours(java.lang.String hours) {
        this.hours = hours;
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("days_of_month")
    public java.lang.String getDaysOfMonth() {
        return daysOfMonth;
    }

    public void setDaysOfMonth(java.lang.String daysOfMonth) {
        this.daysOfMonth = daysOfMonth;
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("month")
    public java.lang.String getMonth() {
        return month;
    }

    public void setMonth(java.lang.String month) {
        this.month = month;
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("day_of_week")
    public java.lang.String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(java.lang.String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("year")
    public java.lang.String getYear() {
        return year;
    }

    public void setYear(java.lang.String year) {
        this.year = year;
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("start_time")
    public java.lang.String getStartTime() {
        return startTime;
    }

    public void setStartTime(java.lang.String startTime) {
        this.startTime = startTime;
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("end_time")
    public java.lang.String getEndTime() {
        return endTime;
    }

    public void setEndTime(java.lang.String endTime) {
        this.endTime = endTime;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.scheduler.Schedule schedule = ((org.apache.ambari.server.state.scheduler.Schedule) (o));
        if (dayOfWeek != null ? !dayOfWeek.equals(schedule.dayOfWeek) : schedule.dayOfWeek != null)
            return false;

        if (daysOfMonth != null ? !daysOfMonth.equals(schedule.daysOfMonth) : schedule.daysOfMonth != null)
            return false;

        if (endTime != null ? !endTime.equals(schedule.endTime) : schedule.endTime != null)
            return false;

        if (hours != null ? !hours.equals(schedule.hours) : schedule.hours != null)
            return false;

        if (minutes != null ? !minutes.equals(schedule.minutes) : schedule.minutes != null)
            return false;

        if (month != null ? !month.equals(schedule.month) : schedule.month != null)
            return false;

        if (startTime != null ? !startTime.equals(schedule.startTime) : schedule.startTime != null)
            return false;

        if (year != null ? !year.equals(schedule.year) : schedule.year != null)
            return false;

        return true;
    }

    @org.codehaus.jackson.annotate.JsonIgnore
    public boolean isEmpty() {
        return ((((((((minutes == null) || minutes.isEmpty()) && ((hours == null) || hours.isEmpty())) && ((dayOfWeek == null) || dayOfWeek.isEmpty())) && ((daysOfMonth == null) || daysOfMonth.isEmpty())) && ((month == null) || month.isEmpty())) && ((year == null) || year.isEmpty())) && ((startTime == null) || startTime.isEmpty())) && ((endTime == null) || endTime.isEmpty());
    }

    @org.codehaus.jackson.annotate.JsonIgnore
    public java.lang.String getScheduleExpression() {
        java.lang.StringBuilder expression = new java.lang.StringBuilder();
        expression.append("0");
        expression.append(" ");
        expression.append(minutes);
        expression.append(" ");
        expression.append(hours);
        expression.append(" ");
        expression.append(daysOfMonth);
        expression.append(" ");
        expression.append(month);
        expression.append(" ");
        expression.append(dayOfWeek);
        if ((year != null) && (!year.isEmpty())) {
            expression.append(" ");
            expression.append(year);
        }
        return expression.toString();
    }

    @java.lang.Override
    public int hashCode() {
        int result = (minutes != null) ? minutes.hashCode() : 0;
        result = (31 * result) + (hours != null ? hours.hashCode() : 0);
        result = (31 * result) + (daysOfMonth != null ? daysOfMonth.hashCode() : 0);
        result = (31 * result) + (month != null ? month.hashCode() : 0);
        result = (31 * result) + (dayOfWeek != null ? dayOfWeek.hashCode() : 0);
        result = (31 * result) + (year != null ? year.hashCode() : 0);
        result = (31 * result) + (startTime != null ? startTime.hashCode() : 0);
        result = (31 * result) + (endTime != null ? endTime.hashCode() : 0);
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((((((((((((((("Schedule {" + "minutes='") + minutes) + '\'') + ", hours='") + hours) + '\'') + ", days_of_month='") + daysOfMonth) + '\'') + ", month='") + month) + '\'') + ", day_of_week='") + dayOfWeek) + '\'') + ", year='") + year) + '\'') + ", startTime='") + startTime) + '\'') + ", endTime='") + endTime) + '\'') + '}';
    }
}