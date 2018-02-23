package com.healthcare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungnguyen on 29/06/2017.
 */

public class RegisterWorkScheduleFragment extends Fragment {
    private final int MORNING_START_TIME = 7 * 3600, MORNING_STOP_TIME = 11 * 3600, AFTERNOON_START_TIME = 12 * 3600, AFTERNOON_STOP_TIME = 17 * 3600, NIGHT_START_TIME = 18 * 3600, NIGHT_STOP_TIME = 22 * 3600;
    public JSONObject clinicList, workScheduleListJSON;
    public JSONArray workScheduleListJSONArray = null;
    private View registerWorkScheduleView;
    private JSONObject doctorJSON = null;
    private int startTime, stopTime;
    private String dates, workspace;
    private MainActivity mainActivity;
    private boolean duplicate = false, result = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        registerWorkScheduleView = inflater.inflate(R.layout.register_workschedule_layout, container, false);
        mainActivity = (MainActivity) getActivity();
        SharedPreferences sp = mainActivity.getSharedPreferences("workSchedule", Context.MODE_PRIVATE);
        if (!sp.getString("workScheduleList", "").equals("")) {
            try {
                workScheduleListJSON = new JSONObject(sp.getString("workScheduleList", ""));
                workScheduleListJSONArray = workScheduleListJSON.getJSONArray("scheduleList");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        doctorJSON = mainActivity.getDoctorJSON();
        registerWorkScheduleHandle();
        return registerWorkScheduleView;
    }


    public void registerWorkScheduleHandle() {

        Spinner spinnerDates = (Spinner) registerWorkScheduleView.findViewById(R.id.dates_list);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(registerWorkScheduleView.getContext(), R.array.dates_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerDates.setAdapter(adapter);
        spinnerDates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                switch (selectedItem) {
                    case "Thứ 2":
                        dates = getString(R.string.monday);
                        break;
                    case "Thứ 3":
                        dates = getString(R.string.tuesday);
                        break;
                    case "Thứ 4":
                        dates = getString(R.string.wednesday);
                        break;
                    case "Thứ 5":
                        dates = getString(R.string.thursday);
                        break;
                    case "Thứ 6":
                        dates = getString(R.string.friday);
                        break;
                    case "Thứ 7":
                        dates = getString(R.string.saturday);
                        break;
                    case "Chủ Nhật":
                        dates = getString(R.string.sunday);
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spinner spinnerTimes = (Spinner) registerWorkScheduleView.findViewById(R.id.times_list);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(registerWorkScheduleView.getContext(), R.array.times_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerTimes.setAdapter(adapter2);
        spinnerTimes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                switch (selectedItem) {
                    case "Buổi Sáng (7h - 11h)":
                        startTime = MORNING_START_TIME;
                        stopTime = MORNING_STOP_TIME;
                        break;
                    case "Buổi Trưa (12h - 17h)":
                        startTime = AFTERNOON_START_TIME;
                        stopTime = AFTERNOON_STOP_TIME;
                        break;
                    case "Buổi Tối (18h - 22h)":
                        startTime = NIGHT_START_TIME;
                        stopTime = NIGHT_STOP_TIME;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        List<String> list = new ArrayList<>();


        SharedPreferences sp = getActivity().getSharedPreferences("ClinicList", Context.MODE_PRIVATE);
        String clinicInfo = sp.getString("info", "");
        if (!clinicInfo.equals("")) {
            try {
                clinicList = new JSONObject(clinicInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            JSONArray clinicArray = clinicList.getJSONArray("clinicList");
            for (int i = 0; i < clinicArray.length(); i++) {
                JSONObject clinic = clinicArray.getJSONObject(i);
                list.add(clinic.getString("nameClinic") + " - " + clinic.getString("address") + "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Spinner spinnerPlaces = (Spinner) registerWorkScheduleView.findViewById(R.id.places_list);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerPlaces.setAdapter(adapter3);
//        spinnerAdapter.add("DELHI");
//        spinnerAdapter.notifyDataSetChanged();


        spinnerPlaces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                workspace = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnRegister = (Button) registerWorkScheduleView.findViewById(R.id.btnAcceptRegisterWorkSchedule);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Thông Báo");
                alertDialog.setMessage("Xác nhận đăng kí lịch trực");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result = submit();
                                if (result) {
                                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog1.setTitle("Thông Báo");
                                    alertDialog1.setMessage("Đăng kí lịch thành công");
                                    alertDialog1.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog1.show();
                                }

                                if (duplicate) {
                                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog1.setTitle("Thông Báo");
                                    alertDialog1.setMessage("Lịch trực này đã được đăng kí");
                                    alertDialog1.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog1.show();
                                } else if (!result) {
                                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog1.setTitle("Thông Báo");
                                    alertDialog1.setMessage("Đăng kí lịch không thành công");
                                    alertDialog1.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog1.show();
                                }
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Huỷ bỏ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
        Button back = (Button) registerWorkScheduleView.findViewById(R.id.btnBackToWorkSchedule);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.contentFrame, new WorkScheduleFragment()).commit();
            }
        });
//        Button btnAddSchedule = (Button) registerWorkScheduleView.findViewById(R.id.addSchedule);
//        btnAddSchedule.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LinearLayout lm = (LinearLayout) registerWorkScheduleView.findViewById(R.id.scheduleListLayout);
//
//            }
//        });

    }

    private boolean submit() {

        UserCreateWorkScheduleTask userCreateWorkScheduleTask = new UserCreateWorkScheduleTask(dates, startTime, stopTime, workspace);
        userCreateWorkScheduleTask.execute((Void) null);
        result = !duplicate;
        return result;
    }


    public class UserCreateWorkScheduleTask extends AsyncTask<Void, Void, Boolean> {

        private String dates;
        private int startTime, stopTime;
        private String workspace;

        public UserCreateWorkScheduleTask(String dates, int startTime, int stopTime, String workspace) {
            this.dates = dates;
            this.startTime = startTime;
            this.stopTime = stopTime;
            this.workspace = workspace;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String scheduleListString = "";
            for (int i = 0; i < workScheduleListJSONArray.length(); i++) {
                try {
                    JSONObject jsonObject = (JSONObject) workScheduleListJSONArray.get(i);
                    if (jsonObject.getString("dates").equals(dates)) {
                        duplicate = true;
                        return false;
                    }
                    scheduleListString += jsonObject.toString() + ",";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String scheduleString = "{\"dates\":\"" + dates + "\",\"startTime\":\"" + startTime + "\",\"stopTime\":\"" + stopTime + "\",\"workspace\":\"" + workspace + "\"}";
            scheduleListString += scheduleString;


            String result = null;
            try {

                result = Connection.registWorkSchedule(doctorJSON.getString("idDoctor"), scheduleListString);
                SharedPreferences sp = mainActivity.getSharedPreferences("workSchedule", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                String scheduleListJSON = "{\"scheduleList\":[" + scheduleListString + "]}";

                editor.putString("workScheduleList", scheduleListJSON);
                editor.commit();
                mainActivity.setWorkScheduleListJSON(new JSONObject(scheduleListJSON));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}
