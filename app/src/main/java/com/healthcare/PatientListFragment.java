package com.healthcare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;


/**
 * Created by hungnguyen on 20/06/2017.
 */

public class PatientListFragment extends Fragment {
    private View patientListView;
    private Spinner spinner;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        patientListView = inflater.inflate(R.layout.patient_list_layout, container, false);
        spinner = (Spinner) patientListView.findViewById(R.id.spinnerPatientList);
        return patientListView;
    }


}
