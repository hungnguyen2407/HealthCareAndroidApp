package com.healthcareandroidapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hungnguyen on 23/06/2017.
 */

public class SettingFragment extends Fragment {
    private View settingView;
    private JSONObject doctorJSON = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        MainActivity mainActivity = (MainActivity) getActivity();
        doctorJSON = mainActivity.getDoctorJSON();
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
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Thông Báo");
                alertDialog.setMessage("Xác nhận thay đổi thông tin");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (changeInfoHandle()) {
                                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog1.setTitle("Thông Báo");
                                    alertDialog1.setMessage("Thay đổi thông tin thành công");
                                    alertDialog1.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog1.show();
                                } else {
                                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog1.setTitle("Thông Báo");
                                    alertDialog1.setMessage("Thay đổi không thông tin thành công");
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

    }

    private boolean changeInfoHandle() {
        //Lay thong tin tu view
        EditText edtName = (EditText) settingView.findViewById(R.id.edtName);
        EditText edtEmail = (EditText) settingView.findViewById(R.id.edtEmail);
        EditText edtPhone = (EditText) settingView.findViewById(R.id.edtPhone);
        EditText edtAddress = (EditText) settingView.findViewById(R.id.edtAddress);
        EditText edtDegree = (EditText) settingView.findViewById(R.id.edtDegree);
        EditText edtSpeciality = (EditText) settingView.findViewById(R.id.edtSpeciality);
        EditText edtExperience = (EditText) settingView.findViewById(R.id.edtExperience);
        EditText edtPassport = (EditText) settingView.findViewById(R.id.edtPassport);
        EditText edtPassword = (EditText) settingView.findViewById(R.id.edtPassword);
        EditText edtBirthDate = (EditText) settingView.findViewById(R.id.edtBirthDate);
        try {

            String password = edtPassword.getText().toString();
            if (!password.equals(doctorJSON.getString("passwords"))) {
                edtPassword.setError("Sai mật khẩu");
                return false;
            }
            String id = doctorJSON.getString("idDoctor");
            String name = edtName.getText().toString();
            if (TextUtils.isEmpty(name)) {
                name = doctorJSON.getString("name");
            }

            String speciality = edtSpeciality.getText().toString();
            if (TextUtils.isEmpty(speciality)) {
                speciality = doctorJSON.getString("nameSpecialty");
            }

            String degree = edtDegree.getText().toString();
            if (TextUtils.isEmpty(degree)) {
                degree = doctorJSON.getString("degree");
            }

            String experience = edtExperience.getText().toString();
            if (TextUtils.isEmpty(experience)) {
                experience = doctorJSON.getString("experience");
            }

            String email = edtEmail.getText().toString();
            if (TextUtils.isEmpty(email)) {
                email = doctorJSON.getString("email");
            }

            String doctorAddress = edtAddress.getText().toString();
            if (TextUtils.isEmpty(doctorAddress)) {
                doctorAddress = doctorJSON.getString("address");
            }

            String phone = edtPhone.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                phone = doctorJSON.getString("phone");
            }

            String passport = edtPassport.getText().toString();
            if (TextUtils.isEmpty(passport)) {
                passport = doctorJSON.getString("passport");
            }

            String birthDate = edtBirthDate.getText().toString();
            if (TextUtils.isEmpty(birthDate)) {
                birthDate = doctorJSON.getString("birthDate");
            }

            //Gui thong tin cap nhat
            UserChangeInfoTask userChangeInfoTask = new UserChangeInfoTask(id, password, name, speciality, degree, experience, email, doctorAddress, phone, passport, birthDate);
            userChangeInfoTask.execute((Void) null);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    class UserChangePasswordTask extends AsyncTask<Void, Void, Boolean> {

        private final String id, userName, password, name, speciality, degree, experience, email, doctorAddress, phone, passport, birthDate;

        UserChangePasswordTask(String id, String userName, String password, String name, String speciality, String degree, String experience, String email, String doctorAddress, String phone, String passport, String birthDate) {
            this.id = id;
            this.userName = userName;
            this.password = password;
            this.name = name;
            this.speciality = speciality;
            this.degree = degree;
            this.experience = experience;
            this.email = email;
            this.doctorAddress = doctorAddress;
            this.phone = phone;
            this.passport = passport;
            this.birthDate = birthDate;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String doctorInfo = "{\"id\":\"" + id + "\",\"nameSpecialty\":\"" + speciality + "\",\"username\":\"" + userName + "\",\"nameDoctor\":\"" + name + "\",\"password\":\"" + password + "\",\"email\":\"" + email + "\",\"phone\":\"" + phone + "\",\"passport\":\"" + passport + "\",\"degree\":\"" + degree + "\",\"experience\":\"" + experience + "\",\"doctorAddress\":\"" + doctorAddress + "\",\"birthDate\":\"" + birthDate + "\"}";
            //Gui thong tin cap nhat
            return Connection.changeInfo(doctorInfo);

        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {

            }
        }
    }


    class UserChangeInfoTask extends AsyncTask<Void, Void, Boolean> {
        private final String id, password, name, speciality, degree, experience, email, doctorAddress, phone, passport, birthDate;

        UserChangeInfoTask(String id, String password, String name, String speciality, String degree, String experience, String email, String doctorAddress, String phone, String passport, String birthDate) {
            this.id = id;

            this.password = password;
            this.name = name;
            this.speciality = speciality;
            this.degree = degree;
            this.experience = experience;
            this.email = email;
            this.doctorAddress = doctorAddress;
            this.phone = phone;
            this.passport = passport;
            this.birthDate = birthDate;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            return Connection.changeInfo(null); //TODO
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {

            }
        }
    }
}
