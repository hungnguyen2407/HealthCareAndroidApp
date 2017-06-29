package com.healthcareandroidapp;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public JSONObject doctorJSON, workScheduleListJSON = null;
    public JSONArray workScheduleListJSONArray = null;
    public String fileName = "doctorInfo";
    public TextView workScheduleTV;
    public Button createWorkSchedul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        Intent intent = getIntent();
        String doctorInfo = intent.getStringExtra(fileName);
        if (doctorInfo == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        try {
            doctorJSON = new JSONObject(doctorInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        createWorkSchedul = (Button) findViewById(R.id.create_work_shedule);

        createWorkSchedul.setVisibility(View.GONE);
        TextView header = (TextView) findViewById(R.id.welcome);
        homeHanle();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        workScheduleTV = (TextView) findViewById(R.id.workScheduleTV);
//        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        TextView header = (TextView) findViewById(R.id.welcome);
        ScrollView homeContent = (ScrollView) findViewById(R.id.homeContent);
        FrameLayout contentFrame = (FrameLayout) findViewById(R.id.contentFrame);
        header.setVisibility(View.GONE);
        createWorkSchedul.setVisibility(View.GONE);
        ScrollView workScheduleView = (ScrollView) findViewById(R.id.scrollViewWorkSchedule);
        workScheduleView.setVisibility(View.GONE);
        //Xu ly su kien nav lich lam viec
        if (id == R.id.nav_work_schedule) {
            homeContent.setVisibility(View.GONE);
            contentFrame.setVisibility(View.GONE);
            workScheduleView.setVisibility(View.VISIBLE);
            createWorkSchedul.setVisibility(View.VISIBLE);
            createWorkSchedul.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findViewById(R.id.homeContent).setVisibility(View.GONE);
                    findViewById(R.id.contentFrame).setVisibility(View.VISIBLE);
                    findViewById(R.id.scrollViewWorkSchedule).setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, new RegisterWorkScheduleFragment()).commit();
                }
            });
            String doctorID = null;
            try {
                doctorID = doctorJSON.getString("idDoctor");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SharedPreferences sp = getSharedPreferences("workSchedule", MODE_PRIVATE);
            try {
                if (sp.getString("workScheduleList", "").equals("")) {
                    WorkScheduleTask workScheduleTask = new WorkScheduleTask(doctorID);
                    workScheduleTask.execute((Void) null);
                } else
                    workScheduleListJSON = new JSONObject(sp.getString("workScheduleList", ""));

                workScheduleListJSONArray = workScheduleListJSON.getJSONArray("scheduleList");
                String text = "";
                for (int i = 0; i < workScheduleListJSONArray.length(); i++) {
                    JSONObject workScheduleJSON = (JSONObject) workScheduleListJSONArray.get(i);
                    text += "Thứ " + workScheduleJSON.getString("dates") + "\nGiờ làm việc từ " + getTime(Integer.parseInt(workScheduleJSON.getString("startTime"))) + " đến " + getTime(Integer.parseInt(workScheduleJSON.getString("stopTime"))) + "\nPhòng làm việc " + workScheduleJSON.getString("workspace") + "\n\n";
                }
                workScheduleTV.setText(text);
                Log.v("TV", workScheduleTV.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        //Xu ly su kien nav danh sach benh nhan
        else if (id == R.id.nav_patient_list) {
            homeContent.setVisibility(View.GONE);
            contentFrame.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().replace(R.id.contentFrame, new PatientListFragment()).commit();
        }


        //Xu ly su kien nav lien he
        else if (id == R.id.nav_communicate) {
            homeContent.setVisibility(View.GONE);
            contentFrame.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().replace(R.id.contentFrame, new CommunicationFragment()).commit();
        }

        //Xu ly su kien nav dang xuat
        else if (id == R.id.nav_logout) {
            //Xoa du lieu dang nhap trong may
            logout();
        }

        //Xu ly su kien nav trang chu
        else if (id == R.id.nav_home) {
            homeHanle();
            homeContent.setVisibility(View.VISIBLE);
            contentFrame.setVisibility(View.GONE);
        }

        //Xy ly su kien nav cai dat
        else if (id == R.id.nav_setting) {
            header.setVisibility(View.GONE);
            homeContent.setVisibility(View.GONE);
            contentFrame.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().replace(R.id.contentFrame, new SettingFragment()).commit();

        }
//        else if (id == R.id.nav_messages) {
//            homeContent.setVisibility(View.GONE);
//            contentFrame.setVisibility(View.VISIBLE);
//            fragmentManager.beginTransaction().replace(R.id.contentFrame, new MessagesFragment()).commit();
//        }
        else if (id == R.id.nav_info) {
            homeContent.setVisibility(View.GONE);
            contentFrame.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().replace(R.id.contentFrame, new InfoFragment()).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void setDoctorJSON(JSONObject doctorJSON) {
        this.doctorJSON = doctorJSON;
        return;
    }


    public JSONObject getDoctorJSON() {
        return doctorJSON;
    }

    public String getDoctorInfo() {
        return doctorJSON.toString();
    }

    public class WorkScheduleTask extends AsyncTask<Void, Void, Boolean> {


        private final String doctorID;


        WorkScheduleTask(String doctorID) {
            this.doctorID = doctorID;
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            workScheduleListJSON = Connection.getWorkSchedule(doctorID);
            SharedPreferences sp = getSharedPreferences("workSchedule", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("workScheduleList", workScheduleListJSON.toString());
            editor.commit();


            return workScheduleListJSON != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {

            }
        }


    }


    private void homeHanle() {
        TextView homeContent = (TextView) findViewById(R.id.homeInfo);
        try {
            JSONObject specialityJSON = doctorJSON.getJSONObject("specialty");
            homeContent.setText("\nHọ tên: " + doctorJSON.getString("nameDoctor") + "\nSĐT: " + doctorJSON.getString("phone") + "\nEmail: " + doctorJSON.getString("email") + "\nĐịa chỉ: " + doctorJSON.getString("address") + "\nChuyên ngành: " + specialityJSON.getString("nameSpecialty") + "\nBằng cấp: " + doctorJSON.getString("degree") + "\nKinh nghiệm: " + doctorJSON.getString("experience"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void logout() {
        doctorJSON = null;
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("doctorJSON", "");
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private String getTime(int time) {
        int d = time / 86400;
        int h = (time % 86400) / 3600;
        int m = (time % 86400 % 3600) / 60;
        StringBuffer sb = new StringBuffer();
        if (d > 0) {
            sb.append(d + "d");
        }
        sb.append(h + "h");
        sb.append(m + "m");
        return sb.toString();
    }
}
