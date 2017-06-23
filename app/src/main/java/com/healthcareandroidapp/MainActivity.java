package com.healthcareandroidapp;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Schedules;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private JSONObject doctorJSON, workScheduleJSON;
    private String fileName = "doctorInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //Lay du lieu tu file trong bo nho trong
//        try {
//            FileInputStream fis = openFileInput(fileName);
//            DataInputStream dis = new DataInputStream(fis);
//            String response = dis.readUTF();
//
//            Lay thong tin bac si
//            if (response != null) {
//                doctorJSON = new JSONObject(response);
//            } else {
//                Intent intent = getIntent();
//                String doctorInfo = intent.getStringExtra(fileName);
//                FileOutputStream fos = new FileOutputStream(fileName);
//                DataOutputStream dos = new DataOutputStream(fos);
//                try {
//                    if (doctorInfo != null) {
//                        doctorJSON = new JSONObject(doctorInfo);
//                        dos.writeUTF(doctorInfo);
//                    }else{
//                        Intent loginIntent = new Intent(this, LoginActivity.class);
//                        startActivity(loginIntent);
//                        Intent mainIntent = new Intent(this, MainActivity.class);
//                        stopService(mainIntent);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

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
        try {
            TextView header = (TextView) findViewById(R.id.header);
            header.setText("Xin chào bác sĩ \n" + doctorJSON.getString("nameDoctor") + ".\n");
            TextView homeContent = (TextView) findViewById(R.id.homeContent);
            homeContent.setText("Thông tin bác sĩ:\nHọ tên: " + doctorJSON.getString("nameDoctor") + "\nSố điện thoại: " + doctorJSON.getString("phone") + "\nĐịa chỉ: " + doctorJSON.getString("address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        TextView header = (TextView) findViewById(R.id.header);
        TextView homeContent = (TextView) findViewById(R.id.homeContent);
        FrameLayout contentFrame = (FrameLayout) findViewById(R.id.contentFrame);


        //Xu ly su kien nav lich lam viec
        if (id == R.id.nav_work_schedule) {
            header.setVisibility(View.GONE);
            homeContent.setVisibility(View.GONE);
            try {
                WorkScheduleTask workScheduleTask = new WorkScheduleTask(doctorJSON.getString("idDoctor"));
                workScheduleTask.execute((Void) null);

                TextView workScheduleTV = (TextView) findViewById(R.id.workScheduleTextView);
                JSONObject workScheduleObject;
                JSONArray workScheduleArray = workScheduleJSON.getJSONArray("scheduleList");
                for (int i = 0; i < workScheduleArray.length(); i++) {
                    workScheduleObject = (JSONObject) workScheduleArray.get(i);
                    Schedules schedule = new Schedules(workScheduleObject.getString("dates"), Integer.parseInt(workScheduleObject.getString("startTime")), Integer.parseInt(workScheduleObject.getString("stopTime")), workScheduleObject.getString("workspace"));
                    workScheduleTV.setText("Thứ " + schedule.getDates());
                    workScheduleTV.setText("Giờ làm việc từ " + schedule.getStartTimeClock() + " đến " + schedule.getStopTimeClock());
                    workScheduleTV.setText("Phòng làm việc " + schedule.getWorkspace());

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Thay content cho content_nav
            fragmentManager.beginTransaction().replace(R.id.contentFrame, new WorkScheduleFragment()).commit();
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
            doctorJSON = null;
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        //Xu ly su kien nav trang chu
        else if (id == R.id.nav_home) {
            header.setText("Thông tin bác sĩ.");
            header.setVisibility(View.GONE);
            homeContent.setVisibility(View.VISIBLE);
            contentFrame.setVisibility(View.GONE);
        }

        //Xy ly su kien nav cai dat
        else if (id == R.id.nav_setting) {
            header.setVisibility(View.GONE);
            homeContent.setVisibility(View.GONE);
            settingHandle();
            fragmentManager.beginTransaction().replace(R.id.contentFrame, new SettingFragment()).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void settingHandle() {
        Button btnChangePassword = (Button) findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvNewPassword = (TextView) findViewById(R.id.tvNewPassword);
                tvNewPassword.setVisibility(View.VISIBLE);

                EditText edtNewPassword = (EditText) findViewById(R.id.edtNewPassword);
                edtNewPassword.setVisibility(View.VISIBLE);

                TextView tvConfirmNewPassword = (TextView) findViewById(R.id.tvConfirmNewPassword);
                tvConfirmNewPassword.setVisibility(View.VISIBLE);

                EditText edtConfirmNewPassword = (EditText) findViewById(R.id.edtConfirmNewPassword);
                edtConfirmNewPassword.setVisibility(View.VISIBLE);

                TextView tvOldPassword = (TextView) findViewById(R.id.tvOldPassword);
                tvOldPassword.setVisibility(View.VISIBLE);

                EditText edtOldPassword = (EditText) findViewById(R.id.edtOldPassword);
                edtOldPassword.setVisibility(View.VISIBLE);
            }
        });

        Button btnConfirmChangePassword = (Button) findViewById(R.id.btn_confirm_change_password);
        btnConfirmChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtNewPassword = (EditText) findViewById(R.id.edtNewPassword);
                String newPassword = edtNewPassword.getText().toString();
                EditText edtConfirmNewPassword = (EditText) findViewById(R.id.edtConfirmNewPassword);
                String confirmNewPassword = edtNewPassword.getText().toString();
                EditText edtOldPassword = (EditText) findViewById(R.id.edtOldPassword);
                String oldPassword = edtNewPassword.getText().toString();

                EditText focusEdit = null;
                boolean cancel = false;
                if (TextUtils.isEmpty(oldPassword)) {
                    edtOldPassword.setError(getString(R.string.register_error_field_required));
                    focusEdit = edtOldPassword;
                    cancel = true;

                } else if (!Validation.isPasswordValid(oldPassword)) {
                    edtOldPassword.setError(getString(R.string.register_error_invalid_password));
                    focusEdit = edtOldPassword;
                    cancel = true;
                }

                if (TextUtils.isEmpty(newPassword)) {
                    edtNewPassword.setError(getString(R.string.register_error_field_required));
                    focusEdit = edtNewPassword;
                    cancel = true;
                } else if (!Validation.isPasswordValid(newPassword)) {
                    edtNewPassword.setError(getString(R.string.register_error_invalid_password));
                    focusEdit = edtNewPassword;
                    cancel = true;
                }

                if (TextUtils.isEmpty(confirmNewPassword)) {
                    edtConfirmNewPassword.setError(getString(R.string.register_error_field_required));
                    focusEdit = edtConfirmNewPassword;
                    cancel = true;
                } else if (!Validation.isPasswordValid(confirmNewPassword)) {
                    edtConfirmNewPassword.setError(getString(R.string.register_error_invalid_password));
                    focusEdit = edtConfirmNewPassword;
                    cancel = true;
                } else if (!confirmNewPassword.equals(newPassword)) {
                    edtConfirmNewPassword.setError(getString(R.string.register_error_invalid_confirm_password));
                    focusEdit = edtConfirmNewPassword;
                    cancel = true;
                }

            }
        });

        Button btnConfirmChangeInfo = (Button) findViewById(R.id.btn_confirm_change_info);
        btnConfirmChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtName = (EditText) findViewById(R.id.edtName);
                EditText edtEmail = (EditText) findViewById(R.id.edtEmail);
                EditText edtPhone = (EditText) findViewById(R.id.edtPhone);
                EditText edtAddress = (EditText) findViewById(R.id.edtAddress);
                EditText edtDegree = (EditText) findViewById(R.id.edtDegree);
                EditText edtExperience = (EditText) findViewById(R.id.edtExperience);
                EditText edtPassport = (EditText) findViewById(R.id.edtPassport);
            }
        });

    }


    public class WorkScheduleTask extends AsyncTask<Void, Void, Boolean> {


        private final String doctorID;


        WorkScheduleTask(String doctorID) {
            this.doctorID = doctorID;
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            workScheduleJSON = Connection.getWorkSchedule(doctorID);

            return workScheduleJSON != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {

                finish();

            }
        }


    }

    class UserChangePasswordTask extends AsyncTask<Void, Void, Boolean> {
        private final String oldPassword, newPassword;

        UserChangePasswordTask(String oldPassword, String newPassword) {
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            return Connection.changePassword(oldPassword, newPassword);

        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {

            }
        }
    }


    class UserChangeInfoTask extends AsyncTask<Void, Void, Boolean> {
        private final String doctorInfo;

        UserChangeInfoTask(String doctorInfo) {
            this.doctorInfo = doctorInfo;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            return Connection.changeInfo(doctorInfo);
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {

            }
        }
    }
}
