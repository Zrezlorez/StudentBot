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
    @Getter
    boolean isConv;

    public UserData(long id, String group, String name) {
        this.id = id;
        this.group = group;
        this.name = name;
        isConv = id<0 || (id>2000000004 && id<2000000100);
    }
    public UserData() {}

    public boolean isNull() {
        return id==0;
    }
    public boolean isHaveGroup() {
        return !group.equals("?");
    }
}
