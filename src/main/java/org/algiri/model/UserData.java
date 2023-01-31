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


    public UserData() {
    }

    public UserData(long id, int groupId, String name) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
    }
}
