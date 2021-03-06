package com.healthcare;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static JSONObject workScheduleListJSON = null;
    public JSONObject doctorJSON = null;
    public String fileName = "doctorInfo";

    public static JSONObject getWorkScheduleListJSON() {
        return workScheduleListJSON;
    }

    public void setWorkScheduleListJSON(JSONObject workScheduleListJSON) {
        this.workScheduleListJSON = workScheduleListJSON;
        return;
    }

    public static String getTime(int time) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_layout);

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

        TextView header = (TextView) findViewById(R.id.header);
        header.setText("Trang chủ");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentFrame, new HomeFragment()).commit();

        //Lay du lieu ve cac phong kham
        SharedPreferences sp = getSharedPreferences("ClinicList", Context.MODE_PRIVATE);
        String clinicInfo = sp.getString("info", "");
        if (clinicInfo.equals("")) {
            ClinicListTask clinicListTask = new ClinicListTask();
            clinicListTask.execute((Void) null);
        }

        //Lay du lieu lich truc
        String doctorID = null;
        try {
            doctorID = doctorJSON.getString("idDoctor");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sp = getSharedPreferences("workSchedule", MODE_PRIVATE);
        if (sp.getString("workScheduleList", "").equals("")) {
            WorkScheduleTask workScheduleTask = new WorkScheduleTask(doctorID);
            workScheduleTask.execute((Void) null);
        } else
            try {
                workScheduleListJSON = new JSONObject(sp.getString("workScheduleList", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        doctorInfoHandle();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

//       setSupportActionBar(toolbar);

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
        ScrollView doctorInfoView = (ScrollView) findViewById(R.id.doctorInfoView);
        FrameLayout contentFrame = (FrameLayout) findViewById(R.id.contentFrame);
        TextView header = (TextView) findViewById(R.id.header);
        //Xu ly su kien nav lich lam viec
        if (id == R.id.nav_work_schedule) {
            header.setText("Lịch làm việc");
            doctorInfoView.setVisibility(View.GONE);
            contentFrame.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().replace(R.id.contentFrame, new WorkScheduleFragment()).commit();
        }


        //Xu ly su kien nav danh sach benh nhan
        else if (id == R.id.nav_patient_list) {
            header.setText("Danh sách đặt khám");
            doctorInfoView.setVisibility(View.GONE);
            contentFrame.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().replace(R.id.contentFrame, new PatientListFragment()).commit();
        }


        //Xu ly su kien nav lien he
        else if (id == R.id.nav_communicate) {
            header.setText("Liên hệ");
            doctorInfoView.setVisibility(View.GONE);
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
            header.setText("Trang chủ");
            doctorInfoView.setVisibility(View.GONE);
            contentFrame.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().replace(R.id.contentFrame, new HomeFragment()).commit();
        }

        //Xy ly su kien nav cai dat
        else if (id == R.id.nav_setting) {
            header.setText("Cài đặt");
            doctorInfoView.setVisibility(View.GONE);
            contentFrame.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().replace(R.id.contentFrame, new SettingFragment()).commit();

        }
//        else if (id == R.id.nav_messages) {
//            doctorInfoView.setVisibility(View.GONE);
//            contentFrame.setVisibility(View.VISIBLE);
//            fragmentManager.beginTransaction().replace(R.id.contentFrame, new MessagesFragment()).commit();
//        }
        else if (id == R.id.nav_info) {
            header.setText("Thông tin");
            doctorInfoView.setVisibility(View.GONE);
            contentFrame.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().replace(R.id.contentFrame, new InfoFragment()).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public JSONObject getDoctorJSON() {
        return doctorJSON;
    }

    public void setDoctorJSON(JSONObject doctorJSON) {
        this.doctorJSON = doctorJSON;
        return;
    }

    public String getDoctorInfo() {
        return doctorJSON.toString();
    }

    private void doctorInfoHandle() {
        final ScrollView doctorInfoView = (ScrollView) findViewById(R.id.doctorInfoView);
        doctorInfoView.setVisibility(View.GONE);
        final TextView doctorInfoTV = (TextView) findViewById(R.id.doctorInfo);
        try {
            String address = "";
            JSONObject specialityJSON = doctorJSON.getJSONObject("specialty");
            if (TextUtils.isEmpty(doctorJSON.getString("address")) || doctorJSON.getString("address").equals("null") || doctorJSON.getString("address") == null) {
                address = "Không có";
            } else
                address = doctorJSON.getString("address");
            Date date = new Date(Long.parseLong(doctorJSON.getString("birthDate")));
            doctorInfoTV.setText("\nHọ tên: " + doctorJSON.getString("nameDoctor") + "\nSĐT: " + doctorJSON.getString("phone") + "\nEmail: " + doctorJSON.getString("email") + "\nNgày sinh: " + date.getDay() + " - " + date.getMonth() + " - " + (1900 + date.getYear()) + "\nĐịa chỉ: " + address + "\nChuyên ngành: " + specialityJSON.getString("nameSpecialty") + "\nBằng cấp: " + doctorJSON.getString("degree") + "\nKinh nghiệm: " + doctorJSON.getString("experience"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ImageButton doctorInfoToggle = (ImageButton) findViewById(R.id.doctorInfoToggle);
        final FrameLayout contentFrame = (FrameLayout) findViewById(R.id.contentFrame);
        doctorInfoToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (doctorInfoView.getVisibility() == View.GONE) {
                    doctorInfoView.setVisibility(View.VISIBLE);
                    contentFrame.setVisibility(View.GONE);
                } else {
                    doctorInfoView.setVisibility(View.GONE);
                    contentFrame.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void logout() {
        doctorJSON = null;
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("doctorJSON", "");
        editor.commit();
        sp = getSharedPreferences("workSchedule", MODE_PRIVATE);
        editor = sp.edit();
        editor.putString("workScheduleList", "");
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
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

    public class ClinicListTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            SharedPreferences sp = getSharedPreferences("ClinicList", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("info", Connection.getClinicList().toString());
            editor.commit();
            return true;
        }
    }
}
