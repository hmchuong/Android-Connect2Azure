package com.example.admin.assign2;

import java.util.Date;

/**
 * Created by Admin on 11/27/2016.
 */

public class Member {
    String username;
    String password;
    String fullname;
    Date dob;
    int roleid;
    String role;
    int genderid;
    public Member(){
        username = new String();
        password = new String();
        fullname = new String();
        dob = new Date();
        roleid = 0;
        role = new String();
        genderid = 0;
    }
}
