package com.healthcare;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hungnguyen on 23/02/2018.
 */

public class WorkScheduleFragment extends Fragment {
    public JSONObject workScheduleListJSON = MainActivity.getWorkScheduleListJSON();
    public TextView workScheduleTV;
    private View workScheduleView;
    private Button createWorkSchedule;
    private JSONArray workScheduleListJSONArray = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        workScheduleView = inflater.inflate(R.layout.workschedule_layout, container, false);
        createWorkSchedule = (Button) workScheduleView.findViewById(R.id.create_work_shedule);
        workScheduleTV = (TextView) workScheduleView.findViewById(R.id.workScheduleTV);
        workScheduleHandle();
        return workScheduleView;
    }

    private void workScheduleHandle() {
        createWorkSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workScheduleListJSONArray.length() > 7) {
                    AlertDialog alertDialog1 = new AlertDialog.Builder(workScheduleView.getContext()).create();
                    alertDialog1.setTitle("Thông Báo");
                    alertDialog1.setMessage("Vượt quá số buổi trực cho phép trong tuần");
                    alertDialog1.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog1.show();
                } else {
                    getFragmentManager().beginTransaction().replace(R.id.contentFrame, new RegisterWorkScheduleFragment()).commit();
                }
            }
        });


        try {

            workScheduleListJSONArray = workScheduleListJSON.getJSONArray("scheduleList");
            String text = "";
            for (int i = 0; i < workScheduleListJSONArray.length(); i++) {
                JSONObject workScheduleJSON = (JSONObject) workScheduleListJSONArray.get(i);
                String workspace = workScheduleJSON.getString("workspace");
                if (workScheduleJSON.getString("workspace").equals("") || workScheduleJSON.getString("workspace").equals("null")) {
                    workspace = "Không có";
                }
                String dates = "";
                switch (workScheduleJSON.getString("dates").toLowerCase()) {
                    case "monday":
                        dates = "thứ 2";
                        break;
                    case "tuesday":
                        dates = "thứ 3";
                        break;
                    case "wednesday":
                        dates = "thứ 4";
                        break;
                    case "thursday":
                        dates = "thứ 5";
                        break;
                    case "friday":
                        dates = "thứ 6";
                        break;
                    case "saturday":
                        dates = "thứ 7";
                        break;
                    case "sunday":
                        dates = "chủ nhật";
                        break;

                }
                text += "Ngày " + dates + "\nGiờ làm việc từ " + MainActivity.getTime(Integer.parseInt(workScheduleJSON.getString("startTime"))) + " đến " + MainActivity.getTime(Integer.parseInt(workScheduleJSON.getString("stopTime"))) + "\nPhòng làm việc " + workspace + "\n\n";
            }
            if (workScheduleListJSONArray.length() < 1) {
                workScheduleTV.setText("Chưa có lịch trực");
            } else
                workScheduleTV.setText(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
