package org.algiri.model;

public class Lesson {
    int day;
    String timeStart;
    String timeEnd;
    String name;
    String group;
    boolean isNumerator;

    public Lesson(int day, String timeStart, String timeEnd, String name, String group, boolean isNumerator) {
        this.day = day;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.name = name;
        this.group = group;
        this.isNumerator = isNumerator;
    }
    public Lesson(){}

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isNumerator() {
        return isNumerator;
    }

    public void setNumerator(boolean numerator) {
        isNumerator = numerator;
    }
}
