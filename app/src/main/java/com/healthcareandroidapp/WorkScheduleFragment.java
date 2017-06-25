package com.healthcareandroidapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by hungnguyen on 20/06/2017.
 */

public class WorkScheduleFragment extends Fragment {

    private View workScheduleView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        workScheduleView = inflater.inflate(R.layout.work_schedule_layout, container, false);

        return workScheduleView;

    }

    public TextView getWorkScheduleTextView()
    {
        return (TextView) workScheduleView.findViewById(R.id.workScheduleTextView);
    }
}
