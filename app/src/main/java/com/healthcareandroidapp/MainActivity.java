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

        TextView header = (TextView) findViewById(R.id.welcome);
        homeHanle();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

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
        TextView workScheduleTV = (TextView) findViewById(R.id.workScheduleTextView);
        FrameLayout contentFrame = (FrameLayout) findViewById(R.id.contentFrame);


        //Xu ly su kien nav lich lam viec
        if (id == R.id.nav_work_schedule) {

            header.setVisibility(View.GONE);
            homeContent.setVisibility(View.GONE);
            ScrollView workScheduleView = (ScrollView) findViewById(R.id.scrollViewWorkSchedule);
            workScheduleView.setVisibility(View.VISIBLE);
            this.workScheduleTV = (TextView) findViewById(R.id.workScheduleTextView);
            String doctorID = null;
            try {
                doctorID = doctorJSON.getString("idDoctor");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            WorkScheduleTask workScheduleTask = new WorkScheduleTask(doctorID);
            workScheduleTask.execute((Void) null);
            SharedPreferences sp = getSharedPreferences("workSchedule", MODE_PRIVATE);
            try {
                workScheduleListJSON = new JSONObject(sp.getString("workScheduleList", ""));

                workScheduleListJSONArray = workScheduleListJSON.getJSONArray("scheduleList");
                for (int i = 0; i < workScheduleListJSONArray.length(); i++) {
                    JSONObject workScheduleJSON = (JSONObject) workScheduleListJSONArray.get(i);
                    setTextWorkSchedule(workScheduleJSON.getString("dates"), workScheduleJSON.getString("startTimeClock"), workScheduleJSON.getString("stopTimeClock"), workScheduleJSON.getString("workspace"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        //Xu ly su kien nav danh sach benh nhan
        else if (id == R.id.nav_patient_list) {
            header.setVisibility(View.GONE);
            homeContent.setVisibility(View.GONE);
            fragmentManager.beginTransaction().replace(R.id.contentFrame, new PatientListFragment()).commit();
        }


        //Xu ly su kien nav lien he
        else if (id == R.id.nav_communicate) {
            header.setVisibility(View.GONE);
            homeContent.setVisibility(View.GONE);
            fragmentManager.beginTransaction().replace(R.id.contentFrame, new CommunicationFragment()).commit();
        }

        //Xu ly su kien nav dang xuat
        else if (id == R.id.nav_logout) {
            //Xoa du lieu dang nhap trong may
            doctorJSON = null;
            SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("doctorJSON", "");
            editor.commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        //Xu ly su kien nav trang chu
        else if (id == R.id.nav_home) {
            homeHanle();
            header.setVisibility(View.GONE);
            homeContent.setVisibility(View.VISIBLE);
            contentFrame.setVisibility(View.GONE);
        }

        //Xy ly su kien nav cai dat
        else if (id == R.id.nav_setting) {
            header.setVisibility(View.GONE);
            homeContent.setVisibility(View.GONE);
            fragmentManager.beginTransaction().replace(R.id.contentFrame, new SettingFragment()).commit();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setDoctorJSON(JSONObject doctorJSON)
    {
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
            homeContent.setText("\nHọ tên: " + doctorJSON.getString("nameDoctor") + "\nSố điện thoại: " + doctorJSON.getString("phone") + "\nEmail: " + doctorJSON.getString("email") + "\nĐịa chỉ: " + doctorJSON.getString("address") + "\nChuyên ngành: " + specialityJSON.getString("nameSpecialty") + "\nBằng cấp: " + doctorJSON.getString("degree") + "\nKinh nghiệm: " + doctorJSON.getString("experience"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setTextWorkSchedule(String dates, String startTime, String stopTime, String workspace) {
        workScheduleTV.setText("Thứ " + dates);
        workScheduleTV.setText("Giờ làm việc từ " + startTime + " đến " + stopTime);
        workScheduleTV.setText("Phòng làm việc " + workspace);
        workScheduleTV.setText("\n\n");
    }
}
