package com.phoenix.archimedes.Models;

public class ScheduleList {

    private String activity_name, timeSlot, location,activityType;
    private long scheduleId;
    long duration;
    boolean isSchedule;



    public ScheduleList() {
    }

    public ScheduleList(String activity_name, String timeSlot, long duration,boolean isSchedule,String location, long scheduleId,String activityType) {
        this.activity_name = activity_name;
        this.timeSlot = timeSlot;
        this.duration = duration;
        this.location = location;
        this.isSchedule = isSchedule;
        this.scheduleId = scheduleId;
        this.activityType = activityType;

    }

    public String getActivityName() {
        return activity_name;
    }

    public void setActivityName(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isSchedule() {
        return isSchedule;
    }

    public void setSchedule(boolean schedule) {
        isSchedule = schedule;
    }

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
}
