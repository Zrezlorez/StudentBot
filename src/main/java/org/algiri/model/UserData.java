package org.algiri.model;

import lombok.Getter;
import lombok.Setter;

public class UserData {
    @Getter
    @Setter
    long id;
    @Getter
    @Setter
    String group;
    @Getter
    @Setter
    String name;


    public UserData(long id, String group, String name) {
        this.id = id;
        this.group = group;
        this.name = name;

    }
    public UserData() {}

    public boolean isNull() {
        return id==0;
    }
    public boolean isHaveGroup() {
        return !group.equals("?");
    }
}
