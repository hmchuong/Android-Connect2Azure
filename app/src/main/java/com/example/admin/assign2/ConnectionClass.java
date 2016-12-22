package com.example.admin.assign2;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 10/1/2016.
 */

public class ConnectionClass {
    String ip = "hmchuong1108.database.windows.net:1433";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "ForumMembersManagement";
    String un = "hmchuong";
    String password = "03052013wp5s3b#";

    String getGenderListQuery = "select * from gender";
    String getRoleListQuery = "select * from role";
    String getMemListQuery = "select * from member";
    String getMemByUsername = "select * from member where username = '";

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {

            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }

    protected ArrayList<Gender> getGenderList() {
        try {
            Connection con = CONN();
            if (con == null) {
                return null;
            } else {

                PreparedStatement ps = con.prepareStatement(getGenderListQuery);
                ResultSet rs = ps.executeQuery();
                ArrayList<Gender> genderList = new ArrayList<>();
                while(rs.next()){
                    Gender gender = new Gender();
                    gender.id = rs.getInt("id");
                    gender.description = rs.getString("description");
                    genderList.add(gender);
                }
                return genderList;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    protected ArrayList<Role> getRoleList() {
        try {
            Connection con = CONN();
            if (con == null) {
                return null;
            } else {


                PreparedStatement ps = con.prepareStatement(getRoleListQuery);
                ResultSet rs = ps.executeQuery();
                ArrayList<Role> roleList = new ArrayList<>();
                while(rs.next()){
                    Role role = new Role();
                    role.id = rs.getInt("id");
                    role.description = rs.getString("description");
                    roleList.add(role);
                }
                return roleList;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    protected List<Map<String, String>> getMemList(ArrayList<Role> roleList) {
        try {
            Processing processing = new Processing();
            Connection con = CONN();
            if (con == null) {
                return null;
            } else {
                List<Map<String, String>> memberList = new ArrayList<Map<String, String>>();
                PreparedStatement ps = con.prepareStatement(getMemListQuery);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Map<String, String> datanum = new HashMap<String, String>();
                    datanum.put("A", rs.getString("username").trim());
                    datanum.put("B", rs.getString("fullname").trim());
                    datanum.put("C", processing.getRoleDescByID(roleList,rs.getInt("roleid")));
                    memberList.add(datanum);
                }
                return memberList;
            }
        } catch (Exception ex) {
            return null;

        }
    }

    protected Member getMemByUsername(String username){
        try {
            Connection con = CONN();
            if (con == null) {
                return null;
            } else {
                String query = getMemByUsername + username.trim() + "'";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                ArrayList<Role> roleList = new ArrayList<>();
                Member mem = new Member();
                while (rs.next()){
                    mem.username = rs.getString("username").trim();
                    mem.password = rs.getString("password").trim();
                    mem.fullname = rs.getString("fullname").trim();
                    mem.dob = rs.getDate("dob");
                    mem.genderid = rs.getInt("genderid");
                    mem.roleid = rs.getInt("roleid");
                }
                return mem;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    protected String addNewMember(Member member){
        String z ="";
        try {
            Connection con = CONN();
            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat.format(member.dob);
                String query = "insert into member (username, password, fullname, dob, genderid, roleid) " +
                        "values ('" + member.username + "','" + member.password + "','" + member.fullname + "','"
                        + formattedDate + "'," + String.valueOf(member.genderid) + "," + String.valueOf(member.roleid) + ")";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.executeUpdate();
                z = "Added Successfully";
            }
        } catch (Exception ex) {
            z = "Exceptions";
        }
        return z;
    }

    protected String updateMember(Member member){
        String z ="";
        try {
            Connection con = CONN();
            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat.format(member.dob);
                String query = "update member set username = '" + member.username +
                "', password = '" + member.password +
                "', fullname = '" + member.fullname +
                "', dob = '" + formattedDate + "', genderid = " + String.valueOf(member.genderid) +
                ", roleid = " + String.valueOf(member.roleid) + "where username = '" + member.username + "'";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.executeUpdate();
                z = "Updated Successfully";
            }
        } catch (Exception ex) {
            z = "Exceptions";
        }
        return z;
    }

    protected String deleteMember(Member member){
        String z = "";
        try {
            Connection con = CONN();
            if (con == null) {
                z = "Error in connection with SQL server";
            } else {

                String query = "delete from member where username='"+member.username+"'";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.executeUpdate();
                z = "Deleted Successfully";
            }
        } catch (Exception ex) {
            z = "Exceptions";
        }
        return z;
    }
}
