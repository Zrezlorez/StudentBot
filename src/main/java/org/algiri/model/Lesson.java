package org.algiri.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lesson {
    private int day;
    private String timeStart;
    private String timeEnd;
    private String name;
    private String group;
    private String teacher = "";


    boolean isNumerator;

    public Lesson(int day, String timeStart, String timeEnd, String name, String group, boolean isNumerator) {
        this.day = day;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.name = name;
        this.group = group;
        this.isNumerator = isNumerator;
        Matcher matcher = Pattern.compile("[А-Я][а-я]+ [А-Я].[А-Я].", Pattern.MULTILINE).matcher(name);
        if (matcher.find()) {
            teacher = matcher.group(0);
        }
    }

    public String getTeacher() {
        return teacher;
    }

    public int getDay() {
        return day;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }
    public boolean isNumerator() {
        return isNumerator;
    }

}
