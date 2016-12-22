package com.example.admin.assign2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 11/27/2016.
 */

public class Processing {
    ConnectionClass connectionClass = new ConnectionClass();

    ArrayList<Gender> getGenderList(){
       return connectionClass.getGenderList();
    }

    ArrayList<Role> getRoleList(){
        return connectionClass.getRoleList();
    }

    List<Map<String, String>> getMemList(ArrayList<Role> roleList){
        return connectionClass.getMemList(roleList);
    }

    Member getMemByUsername(String username){
        return connectionClass.getMemByUsername(username);
    }

    int getGenderByID(ArrayList<Gender> genderList, int id){
        for (int i =0; i <genderList.size(); i++){
            if (genderList.get(i).id == id)
                return i;
        }
        return 0;
    }

    int getRoleOrderByID(ArrayList<Role> roleList, int id){
        for (int i =0; i <roleList.size(); i++){
            if (roleList.get(i).id == id)
                return i;
        }
        return 0;
    }

    String getRoleDescByID(ArrayList<Role> roleList, int id){
        for (int i =0; i <roleList.size(); i++){
            if (roleList.get(i).id == id)
                return roleList.get(i).description;
        }
        return "";
    }

    String addNewMember(Member current){
        return connectionClass.addNewMember(current);
    }

    String updateMember(Member curent){
        return connectionClass.updateMember(curent);
    }

    String deleteMember(Member current){
        return  connectionClass.deleteMember(current);
    }
}
