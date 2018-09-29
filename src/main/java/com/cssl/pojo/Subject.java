package com.cssl.pojo;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Subject {
    private int sid;
    private String title;
    private String type;
    private List<Options> ops;
    private int optionCount;//选项数
    private int poll;//票数
}
