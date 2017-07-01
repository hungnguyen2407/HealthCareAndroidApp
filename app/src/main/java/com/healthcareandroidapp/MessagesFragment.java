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

    private void messagesHandle() {
        final Button patientA = (Button) messagesView.findViewById(R.id.messages_btn_patientA);
        final Button patientB = (Button) messagesView.findViewById(R.id.messages_btn_patientB);
        final Button patientC = (Button) messagesView.findViewById(R.id.messages_btn_patientC);
        final Button patientD = (Button) messagesView.findViewById(R.id.messages_btn_patientD);

        final ScrollView contentPatientA = (ScrollView) messagesView.findViewById(R.id.messgages_content_patientA);
        final ScrollView contentPatientB = (ScrollView) messagesView.findViewById(R.id.messgages_content_patientB);
        final ScrollView contentPatientC = (ScrollView) messagesView.findViewById(R.id.messgages_content_patientC);
        final ScrollView contentPatientD = (ScrollView) messagesView.findViewById(R.id.messgages_content_patientD);
        patientA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentPatientA.getVisibility() == View.VISIBLE) {
                    contentPatientA.setVisibility(View.GONE);
                    patientB.setVisibility(View.VISIBLE);
                    patientC.setVisibility(View.VISIBLE);
                    patientD.setVisibility(View.VISIBLE);
                } else {
                    contentPatientA.setVisibility(View.VISIBLE);
                    contentPatientB.setVisibility(View.GONE);
                    contentPatientC.setVisibility(View.GONE);
                    contentPatientD.setVisibility(View.GONE);
                    patientB.setVisibility(View.GONE);
                    patientC.setVisibility(View.GONE);
                    patientD.setVisibility(View.GONE);
                }
            }
        });
        patientB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentPatientB.getVisibility() == View.VISIBLE) {
                    contentPatientB.setVisibility(View.GONE);
                    patientA.setVisibility(View.VISIBLE);
                    patientC.setVisibility(View.VISIBLE);
                    patientD.setVisibility(View.VISIBLE);
                } else {
                    contentPatientA.setVisibility(View.GONE);
                    contentPatientB.setVisibility(View.VISIBLE);
                    contentPatientC.setVisibility(View.GONE);
                    contentPatientD.setVisibility(View.GONE);
                    patientA.setVisibility(View.GONE);
                    patientC.setVisibility(View.GONE);
                    patientD.setVisibility(View.GONE);
                }
            }
        });
        patientC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentPatientC.getVisibility() == View.VISIBLE) {
                    contentPatientC.setVisibility(View.GONE);
                    patientA.setVisibility(View.VISIBLE);
                    patientB.setVisibility(View.VISIBLE);
                    patientD.setVisibility(View.VISIBLE);
                } else {
                    contentPatientA.setVisibility(View.GONE);
                    contentPatientB.setVisibility(View.GONE);
                    contentPatientC.setVisibility(View.VISIBLE);
                    contentPatientD.setVisibility(View.GONE);
                    patientA.setVisibility(View.GONE);
                    patientB.setVisibility(View.GONE);
                    patientD.setVisibility(View.GONE);
                }
            }
        });
        patientD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentPatientD.getVisibility() == View.VISIBLE) {
                    contentPatientD.setVisibility(View.GONE);
                    patientB.setVisibility(View.VISIBLE);
                    patientC.setVisibility(View.VISIBLE);
                    patientA.setVisibility(View.VISIBLE);
                } else {
                    contentPatientA.setVisibility(View.GONE);
                    contentPatientB.setVisibility(View.GONE);
                    contentPatientC.setVisibility(View.GONE);
                    contentPatientD.setVisibility(View.VISIBLE);
                    patientB.setVisibility(View.GONE);
                    patientC.setVisibility(View.GONE);
                    patientA.setVisibility(View.GONE);
                }
            }
        });
    }
}
