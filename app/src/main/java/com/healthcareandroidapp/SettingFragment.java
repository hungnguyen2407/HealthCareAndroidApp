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
import android.util.Log;
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

import java.util.Date;

/**
 * Created by hungnguyen on 23/06/2017.
 */

public class SettingFragment extends Fragment {
    private View settingView;
    private JSONObject doctorJSON = null;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mainActivity = (MainActivity) getActivity();
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
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Thông Báo");
                alertDialog.setMessage("Xác nhận thay đổi mật khẩu");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (changePasswordHandle()) {
                                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog1.setTitle("Thông Báo");
                                    alertDialog1.setMessage("Thay đổi mật khẩu thành công");
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
                                    alertDialog1.setMessage("Thay đổi mật khẩu không thành công");
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

        Button btnConfirmChangeInfo = (Button) settingView.findViewById(R.id.btn_confirm_change_info);
        btnConfirmChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
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
                                    alertDialog1.setMessage("Thay đổi thông tin không thành công");
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

    //Still not optimized
    private boolean changePasswordHandle() {
        //Lay thong tin tu view
        EditText edtNewPassword = (EditText) settingView.findViewById(R.id.edtNewPassword);
        String newPassword = edtNewPassword.getText().toString();
        EditText edtConfirmNewPassword = (EditText) settingView.findViewById(R.id.edtConfirmNewPassword);
        String confirmNewPassword = edtConfirmNewPassword.getText().toString();
        EditText edtOldPassword = (EditText) settingView.findViewById(R.id.edtOldPassword);
        String oldPassword = edtOldPassword.getText().toString();
        String realPassword = "";
        try {
            realPassword = doctorJSON.getString("passwords");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        boolean cancel = false;

        if (TextUtils.isEmpty(oldPassword)) {
            edtOldPassword.setError(getString(R.string.register_error_field_required));
            cancel = true;

        } else if (!Validation.isPasswordValid(oldPassword)) {
            edtOldPassword.setError(getString(R.string.register_error_invalid_password));
            cancel = true;
        } else if (!oldPassword.equals(realPassword)) {
            edtOldPassword.setError("Sai mật khẩu");
            cancel = true;
        }

        if (TextUtils.isEmpty(newPassword)) {
            edtNewPassword.setError(getString(R.string.register_error_field_required));
            cancel = true;
        } else if (!Validation.isPasswordValid(newPassword)) {
            edtNewPassword.setError(getString(R.string.register_error_invalid_password));
            cancel = true;
        } else if (newPassword.equals(realPassword)) {
            edtNewPassword.setError("Trùng với mật khẩu cũ");
            cancel = true;
        } else if (newPassword.equals(oldPassword)) {
            edtNewPassword.setError("Trùng với mật khẩu xác nhận");
            cancel = true;
        }

        if (TextUtils.isEmpty(confirmNewPassword)) {
            edtConfirmNewPassword.setError(getString(R.string.register_error_field_required));
            cancel = true;
        } else if (!Validation.isPasswordValid(confirmNewPassword)) {
            edtConfirmNewPassword.setError(getString(R.string.register_error_invalid_password));
            cancel = true;
        } else if (!confirmNewPassword.equals(newPassword)) {
            edtConfirmNewPassword.setError(getString(R.string.register_error_invalid_confirm_password));
            cancel = true;
        }


        if (cancel)
            return false;
        //Gui thong tin cap nhat
        UserChangePasswordTask userPasswordTask = null;
        try {
            JSONObject specialty = doctorJSON.getJSONObject("specialty");
            userPasswordTask = new UserChangePasswordTask(doctorJSON.getString("idDoctor"), newPassword, doctorJSON.getString("nameDoctor"), specialty.getString("nameSpecialty"), doctorJSON.getString("degree"), doctorJSON.getString("experience"), doctorJSON.getString("email"), doctorJSON.getString("address"), doctorJSON.getString("phone"), doctorJSON.getString("passport"), doctorJSON.getString("birthDate"));
            userPasswordTask.execute((Void) null);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
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
        EditText edtBirthDateDate = (EditText) settingView.findViewById(R.id.edtBirthDateDate);
        EditText edtBirthDateMonth = (EditText) settingView.findViewById(R.id.edtBirthDateMonth);
        EditText edtBirthDateYear = (EditText) settingView.findViewById(R.id.edtBirthDateYear);
        try {
            String realPassword = "";
            try {
                realPassword = doctorJSON.getString("passwords");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            boolean cancel = false;
            String password = edtPassword.getText().toString();
            if (TextUtils.isEmpty(password)) {
                edtPassword.setError(getString(R.string.register_error_field_required));
                cancel = true;

            } else if (!Validation.isPasswordValid(password)) {
                edtPassword.setError(getString(R.string.register_error_invalid_password));
                cancel = true;
            } else if (!password.equals(realPassword)) {
                edtPassword.setError("Sai mật khẩu");
                cancel = true;
            }

            String id = doctorJSON.getString("idDoctor");
            String name = edtName.getText().toString();
            if (TextUtils.isEmpty(name)) {
                name = doctorJSON.getString("nameDoctor");

            }

            JSONObject specialtyJSON = doctorJSON.getJSONObject("specialty");
            String speciality = edtSpeciality.getText().toString();
            if (TextUtils.isEmpty(speciality)) {
                speciality = specialtyJSON.getString("nameSpecialty");
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

            String birthDateDate = edtBirthDateDate.getText().toString();

            String birthDateMonth = edtBirthDateMonth.getText().toString();

            String birthDateYear = edtBirthDateYear.getText().toString();
            if (!TextUtils.isEmpty(birthDateDate) || !TextUtils.isEmpty(birthDateMonth) || !TextUtils.isEmpty(birthDateYear))
                if (!Validation.isBirthDate(birthDateDate, birthDateMonth, birthDateYear)) {
                    edtBirthDateDate.setError("Ngày tháng không hợp lệ");
                    cancel = true;
                }
            else{

                }
            //Gui thong tin cap nhat

            if (cancel)
                return false;

            UserChangeInfoTask userChangeInfoTask = new UserChangeInfoTask(id, password, name, speciality, degree, experience, email, doctorAddress, phone, passport, birthDateDate, birthDateMonth, birthDateYear);
            userChangeInfoTask.execute((Void) null);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    class UserChangePasswordTask extends AsyncTask<Void, Void, Boolean> {

        private final String id, password, name, speciality, degree, experience, email, doctorAddress, phone, passport, birthDate;

        public UserChangePasswordTask(String id, String password, String name, String speciality, String degree, String experience, String email, String doctorAddress, String phone, String passport, String birthDate) {
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

            String doctorInfo = "{\"idDoctor\":\"" + id + "\",\"nameSpecialty\":\"" + speciality + "\",\"nameDoctor\":\"" + name + "\",\"password\":\"" + password + "\",\"email\":\"" + email + "\",\"phone\":\"" + phone + "\",\"passport\":\"" + passport + "\",\"degree\":\"" + degree + "\",\"experience\":\"" + experience + "\"}";
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
        private final String id, password, name, speciality, degree, experience, email, doctorAddress, phone, passport, birthDateDate, birthDateMonth, birthDateYear;

        public UserChangeInfoTask(String id, String password, String name, String speciality, String degree, String experience, String email, String doctorAddress, String phone, String passport, String birthDateDate, String birthDateMonth, String birthDateYear) {
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
            this.birthDateDate = birthDateDate;
            this.birthDateMonth = birthDateMonth;
            this.birthDateYear = birthDateYear;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
//            Date birthDate = new Date(Integer.parseInt(birthDateDate), Integer.parseInt(birthDateMonth), Integer.parseInt(birthDateYear));
            String doctorInfo = "{\"idDoctor\":\"" + id + "\",\"nameSpecialty\":\"" + speciality + "\",\"nameDoctor\":\"" + name + "\",\"password\":\"" + password + "\",\"email\":\"" + email + "\",\"phone\":\"" + phone + "\",\"passport\":\"" + passport + "\",\"degree\":\"" + degree + "\",\"experience\":\"" + experience + "\",\"doctorAddress\":\"" + doctorAddress + "\"}";
            //Gui thong tin cap nhat
            Connection.changeInfo(doctorInfo);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {

            }
        }
    }
}
