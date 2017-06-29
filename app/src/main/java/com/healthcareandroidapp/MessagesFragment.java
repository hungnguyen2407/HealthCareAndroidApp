package com.healthcareandroidapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

/**
 * Created by hungnguyen on 27/06/2017.
 */

public class MessagesFragment extends Fragment {

    private View messagesView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        messagesView = inflater.inflate(R.layout.messages_layout, container, false);
        messagesHandle();
        return messagesView;
    }

    private void messagesHandle()
    {
        Button patientA = (Button) messagesView.findViewById(R.id.messages_btn_patientA);
        Button patientB = (Button) messagesView.findViewById(R.id.messages_btn_patientB);
        Button patientC = (Button) messagesView.findViewById(R.id.messages_btn_patientC);
        Button patientD = (Button) messagesView.findViewById(R.id.messages_btn_patientD);

        final ScrollView contentPatientA = (ScrollView) messagesView.findViewById(R.id.messgages_content_patientA);
        final ScrollView contentPatientB = (ScrollView) messagesView.findViewById(R.id.messgages_content_patientB);
        final ScrollView contentPatientC = (ScrollView) messagesView.findViewById(R.id.messgages_content_patientC);
        final ScrollView contentPatientD = (ScrollView) messagesView.findViewById(R.id.messgages_content_patientD);
        patientA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentPatientA.setVisibility(View.VISIBLE);
            }
        });
        patientB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentPatientB.setVisibility(View.VISIBLE);
            }
        });
        patientC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentPatientC.setVisibility(View.VISIBLE);
            }
        });
        patientD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentPatientD.setVisibility(View.VISIBLE);
            }
        });
    }
}
