package org.algiri.model;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lesson {
    @Getter
    private final int day;
    @Getter
    private final String timeStart;
    @Getter
    private final String timeEnd;
    @Getter
    private final String name;
    @Getter
    private final String group;
    @Getter
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
    public boolean isNumerator() {
        return isNumerator;
    }

}
