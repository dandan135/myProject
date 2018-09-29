package com.cssl.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Items {
    private int id;
    private int userid;
    private int subid;
    private int opid;

    public Items(int userid, int subid, int opid) {
        this.userid = userid;
        this.subid = subid;
        this.opid = opid;
    }
}
