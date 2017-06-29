package com.healthcareandroidapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hungnguyen on 20/06/2017.
 */

public class PatientListFragment extends Fragment {
    private View patientListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        patientListView = inflater.inflate(R.layout.patient_list_layout, container, false);

        return patientListView;
    }


}
