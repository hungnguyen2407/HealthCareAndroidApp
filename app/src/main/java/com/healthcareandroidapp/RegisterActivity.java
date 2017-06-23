package com.healthcareandroidapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private TextView userNameView, passwordView, confirmPasswordView, nameView, clinicNameView, clinicAddressView, specialityView, degreeView, experienceView, emailView, doctorAddressView, phoneView, passportView;
    private JSONObject doctorJSON = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Button registerButton = (Button) findViewById(R.id.btnSubmit_SignUp);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Thuc hien qua trinh gui du lieu dang ki len server
                register();
            }
        });

        Button loginButton = (Button) findViewById(R.id.btnReturnLogin);
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
        clinicNameView = (TextView) findViewById(R.id.register_clinic_name);
        clinicAddressView = (TextView) findViewById(R.id.register_clinic_address);
        specialityView = (TextView) findViewById(R.id.register_speciality);
        degreeView = (TextView) findViewById(R.id.register_degree);
        experienceView = (TextView) findViewById(R.id.register_experience);
        doctorAddressView = (TextView) findViewById(R.id.register_doctor_address);
        phoneView = (TextView) findViewById(R.id.register_phone);
        passportView = (TextView) findViewById(R.id.register_passport);
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

    public void register() {
        progressBar.setVisibility(View.VISIBLE);

        if (registerTask != null) {
            return;
        }

        // Reset errors.
        userNameView.setError(null);
        passwordView.setError(null);
        confirmPasswordView.setError(null);
        nameView.setError(null);
        clinicNameView.setError(null);
        clinicAddressView.setError(null);
        specialityView.setError(null);
        degreeView.setError(null);
        experienceView.setError(null);
        emailView.setError(null);
        doctorAddressView.setError(null);
        phoneView.setError(null);
        passportView.setError(null);

        // Store values at the time of the login attempt.
        String userName = this.userNameView.getText().toString();
        String password = this.passwordView.getText().toString();
        String confirmPassword = this.confirmPasswordView.getText().toString();
        String name = this.nameView.getText().toString();
        String clinicName = this.clinicNameView.getText().toString();
        String clinicAddress = this.clinicAddressView.getText().toString();
        String speciality = this.specialityView.getText().toString();
        String degree = this.degreeView.getText().toString();
        String experience = this.experienceView.getText().toString();
        String email = this.emailView.getText().toString();
        String doctorAddress = this.doctorAddressView.getText().toString();
        String phone = this.phoneView.getText().toString();
        String passport = this.passportView.getText().toString();

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
        } else if (!confirmPassword.equals(confirmPassword)) {
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

        //Kiem tra dinh dang ten phong kham
        if (TextUtils.isEmpty(clinicName)) {
            this.clinicNameView.setError(getString(R.string.register_error_field_required));
            focusView = this.clinicNameView;
            cancel = true;
        }

        // Kiem tra dinh dang dia chi phong kham
        if (TextUtils.isEmpty(clinicAddress)) {
            this.clinicAddressView.setError(getString(R.string.register_error_field_required));
            focusView = this.clinicAddressView;
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
        } else if(!Validation.isExperienceValid(experience))
        {
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
        //Kiem tra xem co loi xay ra trong form dang ki
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            registerTask = new UserRegisterTask(userName, password, name, clinicName, clinicAddress, speciality, degree, experience, email, doctorAddress, phone, passport);
            registerTask.execute((Void) null);
        }
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

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String userName, password, name, clinicName, clinicAddress, speciality, degree, experience, email, doctorAddress, phone, passport;

        UserRegisterTask(String userName, String password, String name, String clinicName, String clinicAddress, String speciality, String degree, String experience, String email, String doctorAddress, String phone, String passport) {
            this.userName = userName;
            this.password = password;
            this.name = name;
            this.clinicName = clinicName;
            this.clinicAddress = clinicAddress;
            this.speciality = speciality;
            this.degree = degree;
            this.experience = experience;
            this.email = email;
            this.doctorAddress = doctorAddress;
            this.phone = phone;
            this.passport = passport;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            //TODO:Xu ly dang nhap voi server

            Connection.register(userName, password, name, email, clinicName, clinicAddress, speciality, degree, experience, doctorAddress, phone, passport);
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            registerTask = null;
            showProgress(false);

            if (success) {
                goLoginActivity();
                finish();

            } else {
                // Xu ly khi dang ki khong thanh cong
            }
        }

        @Override
        protected void onCancelled() {
            registerTask = null;
            showProgress(false);
        }


    }


    private void goLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("messeges", "Đăng kí thành công. Khi tài khoản được sẽ được thông báo qua emailView.");
        startActivity(intent);
    }
}
