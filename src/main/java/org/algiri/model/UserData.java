package org.algiri.model;

import lombok.Getter;
import lombok.Setter;

public class UserData {
    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    private int groupId;
    @Getter
    @Setter
    private String name;


    public UserData(long id, String group, String name) {}
    public UserData(){}
}
