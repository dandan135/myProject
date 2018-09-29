package com.cssl.pojo;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
public class Options {
    private int oid;
    private String context;
    private int osid;
    private double width;//显示的宽度
    private int pollCount;//单个选项票数

    public Options(String context, int osid) {
        this.context = context;
        this.osid = osid;
    }
    public Options(){

    }
}
