package com.example.admin.assign2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ConnectionClass connectionClass;
    Processing processing;
    EditText usernameEdittext, passwordEdittext, fullnameEdittext;
    DatePicker dobDatePicker;
    Spinner genderSpinner, roleSpinner;
    Button addButton, updateButton, deleteButton;
    ProgressBar pbbar;
    ListView memberList;

    Member currentMember;
    ArrayList<Gender> genderList;
    ArrayList<Role> roleList;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionClass = new ConnectionClass();
        usernameEdittext = (EditText) findViewById(R.id.usernameEdt);
        passwordEdittext = (EditText) findViewById(R.id.passwordEdt);
        fullnameEdittext = (EditText) findViewById(R.id.fullnameEdt);

        dobDatePicker = (DatePicker) findViewById(R.id.dobpicker);

        genderSpinner = (Spinner) findViewById(R.id.genderspinner);
        roleSpinner = (Spinner) findViewById(R.id.rolespinner);

        addButton = (Button) findViewById(R.id.addBtn);
        updateButton = (Button) findViewById(R.id.updateBtn);
        deleteButton = (Button) findViewById(R.id.deleteBtn);

        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        memberList = (ListView) findViewById(R.id.memberlst);

        currentMember = new Member();
        genderList = new ArrayList<>();
        roleList = new ArrayList<>();
        processing = new Processing();

        genderList = processing.getGenderList();
        roleList = processing.getRoleList();

        //Load gender list
        ArrayList<String> genderDesc = new ArrayList<>();
        for(int i = 0; i< genderList.size(); i++) {
            genderDesc.add(genderList.get(i).description);
        }
        ArrayAdapter<String> adapterGender=new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        genderDesc
                );
        adapterGender.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);

        genderSpinner.setAdapter(adapterGender);
        genderSpinner.setOnItemSelectedListener(new GenderLoadingEvent());

        //Load role list
        ArrayList<String> roleDesc = new ArrayList<>();
        for(int i = 0; i< roleList.size(); i++) {
            roleDesc.add(roleList.get(i).description);
        }
        ArrayAdapter<String> adapterRole =new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        roleDesc
                );
        adapterRole.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);

        roleSpinner.setAdapter(adapterRole);
        roleSpinner.setOnItemSelectedListener(new RoleLoadingEvent());

        username = "";
        FillList fillList = new FillList();
        fillList.execute("");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMember addMember = new AddMember();
                addMember.execute("");

            }
        });
        updateButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                UpdateMember updateMember = new UpdateMember();
                updateMember.execute("");

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DeleteMember deleteMember = new DeleteMember();
                deleteMember.execute("");

            }
        });

    }

    public class FillList extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> memberList = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            pbbar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();

            String[] from = { "A", "B", "C" };
            int[] views = { R.id.usernamelbl, R.id.fullnamelbl,R.id.rolelbl };
            final SimpleAdapter ADA = new SimpleAdapter(MainActivity.this,
                    memberList, R.layout.lsttemplate, from,
                    views);
            MainActivity.this.memberList.setAdapter(ADA);


            MainActivity.this.memberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA.getItem(arg2);
                    //Get member by username
                    String username = (String) obj.get("A");
                    currentMember = processing.getMemByUsername(username.trim());

                    //Show details
                    usernameEdittext.setText(currentMember.username);
                    passwordEdittext.setText(currentMember.password);
                    fullnameEdittext.setText(currentMember.fullname);
                    genderSpinner.setSelection(processing.getGenderByID(genderList, currentMember.genderid));
                    roleSpinner.setSelection(processing.getRoleOrderByID(roleList, currentMember.roleid));
                    dobDatePicker.init(currentMember.dob.getYear(),currentMember.dob.getMonth(),currentMember.dob.getDay(),new MyOnDateChangedListener());
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {
            memberList = processing.getMemList(roleList);
            return "";
        }

        private class MyOnDateChangedListener implements DatePicker.OnDateChangedListener {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                currentMember.dob.setDate(dayOfMonth);
                currentMember.dob.setMonth(monthOfYear);
                currentMember.dob.setYear(year);
            }
        }
    }

    public class AddMember extends AsyncTask<String, String, String> {

        String z = "";

        @Override
        protected void onPreExecute() {
            currentMember.username = usernameEdittext.getText().toString().trim();
            currentMember.password = passwordEdittext.getText().toString().trim();
            currentMember.fullname = fullnameEdittext.getText().toString().trim();
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();
            FillList fillList = new FillList();
            fillList.execute("");

        }

        @Override
        protected String doInBackground(String... params) {
            if (currentMember.username.trim().equals("") || currentMember.password.trim().equals(""))
                z = "Please enter username and password";
            else {
                z = processing.addNewMember(currentMember);
            }
            return z;
        }
    }

    public class UpdateMember extends AsyncTask<String, String, String> {
        String z = "";

        @Override
        protected void onPreExecute() {
            currentMember.username = usernameEdittext.getText().toString().trim();
            currentMember.password = passwordEdittext.getText().toString().trim();
            currentMember.fullname = fullnameEdittext.getText().toString().trim();
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();
            FillList fillList = new FillList();
            fillList.execute("");
        }

        @Override
        protected String doInBackground(String... params) {
            if (currentMember.username.trim().equals("") || currentMember.password.trim().equals(""))
                z = "Please enter username and password";
            else {
                z = processing.updateMember(currentMember);
            }
            return z;
        }
    }

    public class DeleteMember extends AsyncTask<String, String, String> {
        String z = "";

        @Override
        protected void onPreExecute() {
            currentMember.username = usernameEdittext.getText().toString().trim();
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();
            FillList fillList = new FillList();
            fillList.execute("");

        }

        @Override
        protected String doInBackground(String... params) {
            z = processing.deleteMember(currentMember);
            return z;
        }
    }

    private class GenderLoadingEvent implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0,
                                   View arg1,
                                   int arg2,
                                   long arg3) {
            //arg2 is selected member
            currentMember.genderid = genderList.get(arg2).id;
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            currentMember.genderid = genderList.get(0).id;
        }
    }

    private class RoleLoadingEvent implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0,
                                   View arg1,
                                   int arg2,
                                   long arg3) {
            //arg2 is selected member
            currentMember.roleid = roleList.get(arg2).id;
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            currentMember.roleid = roleList.get(0).id;
        }
    }
}

