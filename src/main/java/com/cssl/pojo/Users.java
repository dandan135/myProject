package com.cssl.pojo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Setter
@Getter
public class Users {
    private int uid;
    private String username;
    private String password;
    private String isadmin;
    private String logdate;
    private String islog;
    public Users() {
    }

    public Users(int uid, String username, String password, String isadmin, String logdate, String islog) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.isadmin = isadmin;
        this.logdate = logdate;
        this.islog = islog;
    }

    public static void main(String[] args) throws ParseException {
        /*java.util.Date date=new java.util.Date();
        String time="2018-08-16 15:11:56";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.sql.Date d2 = new java.sql.Date(System.currentTimeMillis());
        java.util.Date da;
        da = sdf.parse(time);
        System.out.println(date);
        System.out.println(da);
        long tt=date.getTime()-da.getTime();
        long minutes = (tt % (1000 * 60 * 60)) / (1000 * 60);
        System.out.println("minutes:"+minutes);
        //System.out.println(d2);
    */
        double i=1%2;
        System.out.println("i"+i);

    }
}
