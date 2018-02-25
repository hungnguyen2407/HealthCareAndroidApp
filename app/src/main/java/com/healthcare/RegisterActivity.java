package com.healthcare;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;


public class RegisterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserRegisterTask registerTask = null;
    private View progressBar, registerForm;
    private TextView userNameView, passwordView, confirmPasswordView, nameView, specialityView, degreeView, experienceView, emailView, doctorAddressView, phoneView, passportView, birthDateDateView, birthDateMonthView, birthDateYearView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        final Button registerButton = (Button) findViewById(R.id.btnSubmit_SignUp);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(RegisterActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(registerButton.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
//                Thuc hien qua trinh gui du lieu dang ki len server
                AlertDialog alertDialog = new AlertDialog.Builder(getApplication()).create();
                alertDialog.setTitle("Thông Báo");
                alertDialog.setMessage("Xác nhận muốn đăng kí tài khoản");
                alertDialog.setContentView(R.layout.register_layout);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (register()) {
                                    AlertDialog alertDialog1 = new AlertDialog.Builder(getApplication()).create();
                                    alertDialog1.setTitle("Thông Báo");
                                    alertDialog1.setMessage("Đăng kí thành công");
                                    alertDialog1.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog1.show();
                                } else {
                                    AlertDialog alertDialog1 = new AlertDialog.Builder(getApplication()).create();
                                    alertDialog1.setTitle("Thông Báo");
                                    alertDialog1.setMessage("Đăng kí không thành công");
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
                register();
            }
        });

        ImageButton loginButton = (ImageButton) findViewById(R.id.btnReturnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Quay tro lai giao dien dang nhap
                login();
            }
        });

        //Lay cac component tu view
        userNameView = (TextView) findViewById(R.id.register_user_name);
        passwordView = (TextView) findViewById(R.id.register_password);
        confirmPasswordView = (TextView) findViewById(R.id.register_confirm_password);
        emailView = (TextView) findViewById(R.id.register_email);
        nameView = (TextView) findViewById(R.id.register_name);
        specialityView = (TextView) findViewById(R.id.register_speciality);
        degreeView = (TextView) findViewById(R.id.register_degree);
        experienceView = (TextView) findViewById(R.id.register_experience);
        doctorAddressView = (TextView) findViewById(R.id.register_doctor_address);
        phoneView = (TextView) findViewById(R.id.register_phone);
        passportView = (TextView) findViewById(R.id.register_passport);
        birthDateDateView = (TextView) findViewById(R.id.register_birth_date_date);
        birthDateMonthView = (TextView) findViewById(R.id.register_birth_date_month);
        birthDateYearView = (TextView) findViewById(R.id.register_birth_date_year);
        registerForm = findViewById(R.id.register_form);
        progressBar = findViewById(R.id.register_progress);
        progressBar.setVisibility(View.GONE);

    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(userNameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });
//        } else {
//            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//        }
        return false;
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void login() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    public boolean register() {

        if (registerTask != null) {
            return false;
        }

        // Reset errors.
        userNameView.setError(null);
        passwordView.setError(null);
        confirmPasswordView.setError(null);
        nameView.setError(null);
        specialityView.setError(null);
        degreeView.setError(null);
        experienceView.setError(null);
        emailView.setError(null);
        doctorAddressView.setError(null);
        phoneView.setError(null);
        passportView.setError(null);
        birthDateDateView.setError(null);

        // Store values at the time of the login attempt.
        String userName = this.userNameView.getText().toString();
        String password = this.passwordView.getText().toString();
        String confirmPassword = this.confirmPasswordView.getText().toString();
        String name = this.nameView.getText().toString();
        String speciality = this.specialityView.getText().toString();
        String degree = this.degreeView.getText().toString();
        String experience = this.experienceView.getText().toString();
        String email = this.emailView.getText().toString();
        String doctorAddress = this.doctorAddressView.getText().toString();
        String phone = this.phoneView.getText().toString();
        String passport = this.passportView.getText().toString();
        String birthDateDate = this.birthDateDateView.getText().toString();
        String birthDateMonth = this.birthDateMonthView.getText().toString();
        String birthDateYear = this.birthDateYearView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Kiem tra dinh dang mat khau
        if (TextUtils.isEmpty(password)) {
            this.passwordView.setError(getString(R.string.register_error_field_required));
            focusView = this.passwordView;
            cancel = true;
        } else if (!Validation.isPasswordValid(password)) {
            this.passwordView.setError(getString(R.string.register_error_invalid_password));
            focusView = this.passwordView;
            cancel = true;
        }

        // Kiem tra dinh dang xac nhan mat khau
        if (TextUtils.isEmpty(confirmPassword)) {
            this.confirmPasswordView.setError(getString(R.string.register_error_field_required));
            focusView = this.confirmPasswordView;
            cancel = true;
        } else if (!Validation.isPasswordValid(confirmPassword)) {
            this.confirmPasswordView.setError(getString(R.string.register_error_invalid_password));
            focusView = this.confirmPasswordView;
            cancel = true;
        } else if (!confirmPassword.equals(password)) {
            this.confirmPasswordView.setError(getString(R.string.register_error_invalid_confirm_password));
            focusView = this.confirmPasswordView;
            cancel = true;
        }

        // Kiem tra dinh dang ten tai khoan
        if (TextUtils.isEmpty(userName)) {
            this.userNameView.setError(getString(R.string.error_field_required));
            focusView = this.userNameView;
            cancel = true;
        } else if (!Validation.isUserNameValid(userName)) {
            this.userNameView.setError(getString(R.string.login_error_invalid_user_name));
            focusView = this.userNameView;
            cancel = true;
        }

        //Kiem tra dinh dang emailView
        if (TextUtils.isEmpty(email)) {
            this.emailView.setError(getString(R.string.register_error_field_required));
            focusView = this.emailView;
            cancel = true;
        } else if (!Validation.isEmailValid(email)) {
            this.emailView.setError(getString(R.string.register_error_invalid_email));
            focusView = this.emailView;
            cancel = true;
        }

        // Kiem tra dinh dang ten
        if (TextUtils.isEmpty(name)) {
            this.nameView.setError(getString(R.string.register_error_field_required));
            focusView = this.nameView;
            cancel = true;
        }

        // Kiem tra dinh dang cua chuyen nganh
        if (TextUtils.isEmpty(speciality)) {
            this.specialityView.setError(getString(R.string.register_error_field_required));
            focusView = this.specialityView;
            cancel = true;
        }

        // Kiem tra dinh dang cua bang cap
        if (TextUtils.isEmpty(degree)) {
            this.degreeView.setError(getString(R.string.register_error_field_required));
            focusView = this.degreeView;
            cancel = true;
        }

        //Kiem tra dinh dang cua kinh nghiem
        if (TextUtils.isEmpty(experience)) {
            this.experienceView.setError(getString(R.string.register_error_field_required));
            focusView = this.experienceView;
            cancel = true;
        } else if (!Validation.isExperienceValid(experience)) {
            this.experienceView.setError(getString(R.string.register_error_invalid_experience));
            focusView = this.experienceView;
            cancel = true;
        }

        //Kiem tra dinh dang dia chi thuong tru
        if (TextUtils.isEmpty(doctorAddress)) {
            this.doctorAddressView.setError(getString(R.string.register_error_field_required));
            focusView = this.doctorAddressView;
            cancel = true;
        }

        //Kiem tra dinh dang so dien thoai
        if (TextUtils.isEmpty(phone)) {
            this.phoneView.setError(getString(R.string.register_error_field_required));
            focusView = this.phoneView;
            cancel = true;
        } else if (!Validation.isPhoneValid(phone)) {
            this.phoneView.setError(getString(R.string.register_error_invalid_phone));
            focusView = this.phoneView;
            cancel = true;
        }

        //Kiem tra dinh dang ho chieu
        if (TextUtils.isEmpty(passport)) {
            this.passportView.setError(getString(R.string.register_error_field_required));
            focusView = this.passwordView;
            cancel = true;
        }

        //Kiem tra dinh dang ngay sinh
        if (TextUtils.isEmpty(birthDateDate)) {
            this.birthDateDateView.setError(getString(R.string.register_error_field_required));
            focusView = this.birthDateDateView;
            cancel = true;
        }
        if (TextUtils.isEmpty(birthDateMonth)) {
            this.birthDateMonthView.setError(getString(R.string.register_error_field_required));
            focusView = this.birthDateMonthView;
            cancel = true;
        }
        if (TextUtils.isEmpty(birthDateYear)) {
            this.birthDateYearView.setError(getString(R.string.register_error_field_required));
            focusView = this.birthDateYearView;
            cancel = true;
        }
        if (!Validation.isBirthDate(birthDateDate, birthDateMonth, birthDateYear)) {
            birthDateDateView.setError("Ngày tháng không hợp lệ");
            focusView = this.birthDateDateView;
            cancel = true;
        }
        //Kiem tra xem co loi xay ra trong form dang ki
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            registerTask = new UserRegisterTask(userName, password, name, speciality, degree, experience, email, doctorAddress, phone, passport, birthDateDate, birthDateMonth, birthDateYear);
            registerTask.execute((Void) null);
            return true;
        }
        return false;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
            registerForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void showTimePickerDialog(View v) {
        android.app.DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public static class RegisterStatusDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.register_success_messages)
                    .setPositiveButton(R.string.register_return_login_action, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(null, LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.register_close_dialog, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String userName, password, name, speciality, degree, experience, email, doctorAddress, phone, passport, birthDateDate, birthDateMonth, birthDateYear;

        public UserRegisterTask(String userName, String password, String name, String speciality, String degree, String experience, String email, String doctorAddress, String phone, String passport, String birthDateDate, String birthDateMonth, String birthDateYear) {
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
            this.birthDateDate = birthDateDate;
            this.birthDateMonth = birthDateMonth;
            this.birthDateYear = birthDateYear;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Date birthDate = new Date(Integer.parseInt(birthDateDate), Integer.parseInt(birthDateMonth), Integer.parseInt(birthDateYear) - 1900);
            String doctorInfo = "{\"specialty\":\"" + speciality + "\",\"userName\":\"" + userName + "\",\"name\":\"" + name + "\",\"password\":\"" + password + "\",\"email\":\"" + email + "\",\"phone\":\"" + phone + "\",\"passport\":\"" + passport + "\",\"degree\":\"" + degree + "\",\"experience\":\"" + experience + "\",\"doctorAddress\":\"" + doctorAddress + "\",\"birthDate\":\"" + birthDate.getTime() + "\"}";

            //TODO
            return Connection.register(doctorInfo);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            registerTask = null;
            showProgress(false);

            if (success) {
                AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                alertDialog.setTitle("Thông Báo");
                alertDialog.setMessage("Đăng kí thành công");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                alertDialog.setTitle("Thông Báo");
                alertDialog.setMessage("Đăng kí không thành công");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }

        @Override
        protected void onCancelled() {
            registerTask = null;
            showProgress(false);
        }


    }
}
