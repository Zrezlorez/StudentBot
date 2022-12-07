package org.algiri.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lesson {
    int day;
    String timeStart;
    String timeEnd;
    String name;
    String group;
    String teacher = "";



    String cabinet = "";
    boolean isNumerator;

    public Lesson(int day, String timeStart, String timeEnd, String name, String group, boolean isNumerator) {
        this.day = day;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.name = name;
        this.group = group;
        this.isNumerator = isNumerator;
        teacher = Pattern.compile("[А-Я][а-я]+ [А-Я].[А-Я].", Pattern.MULTILINE).matcher(name).group(0);
    }
    public Lesson(){}

    public String getTeacher() {
        return teacher;
    }

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
        Matcher matcher = Pattern.compile("[А-Я][а-я]+ [А-Я].[А-Я].", Pattern.MULTILINE).matcher(name);
        if (matcher.find()) {
            teacher = matcher.group(0);
        }
//        matcher = Pattern.compile("[0-9]+_? ?[a-я]* ?/[0-9А-я]{2,}", Pattern.MULTILINE).matcher(name);
//        if(matcher.find()) {
//            cabinet = matcher.group(0);
//        }
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
