package com.healthcareandroidapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by hungnguyen on 29/06/2017.
 */

public class RegisterWorkScheduleFragment extends Fragment {
    private View registerWorkScheduleView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        registerWorkScheduleView = inflater.inflate(R.layout.register_workschedule_layout, container, false);
        registerWorkScheduleHandle();
        return registerWorkScheduleView;
    }


    public void registerWorkScheduleHandle() {
        Spinner spinner = (Spinner) registerWorkScheduleView.findViewById(R.id.dates_list);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(registerWorkScheduleView.getContext(),
                R.array.dates_list, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }
}
