package com.healthcareandroidapp;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by hungnguyen on 23/06/2017.
 */

public class SettingFragment extends Fragment {
    private View settingView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        settingView = inflater.inflate(R.layout.setting_layout, container, false);
        settingHandle();
        return settingView;
    }

    private void settingHandle() {

        final Button btnChangeInfo = (Button) settingView.findViewById(R.id.btn_change_info);

        btnChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScrollView scrollViewChangeInfo = (ScrollView) settingView.findViewById(R.id.setting_sv_change_info);
                if (scrollViewChangeInfo.getVisibility() == View.VISIBLE)
                    scrollViewChangeInfo.setVisibility(View.GONE);
                else
                    scrollViewChangeInfo.setVisibility(View.VISIBLE);

            }
        });

        Button btnChangePassword = (Button) settingView.findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scrollViewChangePassword = (ScrollView) settingView.findViewById(R.id.setting_sv_change_password);
                if (scrollViewChangePassword.getVisibility() == View.VISIBLE)
                    scrollViewChangePassword.setVisibility(View.GONE);
                else
                    scrollViewChangePassword.setVisibility(View.VISIBLE);
            }
        });

        Button btnConfirmChangePassword = (Button) settingView.findViewById(R.id.btn_confirm_change_password);
        btnConfirmChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtNewPassword = (EditText) settingView.findViewById(R.id.edtNewPassword);
                String newPassword = edtNewPassword.getText().toString();
                EditText edtConfirmNewPassword = (EditText) settingView.findViewById(R.id.edtConfirmNewPassword);
                String confirmNewPassword = edtNewPassword.getText().toString();
                EditText edtOldPassword = (EditText) settingView.findViewById(R.id.edtOldPassword);
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

        Button btnConfirmChangeInfo = (Button) settingView.findViewById(R.id.btn_confirm_change_info);
        btnConfirmChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtName = (EditText) settingView.findViewById(R.id.edtName);
                EditText edtEmail = (EditText) settingView.findViewById(R.id.edtEmail);
                EditText edtPhone = (EditText) settingView.findViewById(R.id.edtPhone);
                EditText edtAddress = (EditText) settingView.findViewById(R.id.edtAddress);
                EditText edtDegree = (EditText) settingView.findViewById(R.id.edtDegree);
                EditText edtExperience = (EditText) settingView.findViewById(R.id.edtExperience);
                EditText edtPassport = (EditText) settingView.findViewById(R.id.edtPassport);
            }
        });

    }
}
