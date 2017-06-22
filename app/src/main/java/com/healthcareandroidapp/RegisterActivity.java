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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;

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
    private TextView userName, password, confirmPassword, name, clinicName, clinicAddress, speciality, experience, email;
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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(userName, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
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
        userName.setError(null);
        password.setError(null);
        confirmPassword.setError(null);
        name.setError(null);
        clinicName.setError(null);
        clinicAddress.setError(null);
        speciality.setError(null);
        experience.setError(null);
        email.setError(null);


        // Store values at the time of the login attempt.
        String userName = this.userName.getText().toString();
        String password = this.password.getText().toString();
        String confirmPassword = this.confirmPassword.getText().toString();
        String name = this.name.getText().toString();
        String clinicName = this.clinicName.getText().toString();
        String clinicAddress = this.clinicAddress.getText().toString();
        String speciality = this.speciality.getText().toString();
        String experience = this.experience.getText().toString();
        String email = this.email.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Kiem tra dinh dang mat khau
        if (!TextUtils.isEmpty(password)) {
            this.password.setError(getString(R.string.login_error_invalid_password));
            focusView = this.password;
            cancel = true;
        }else if(!isEmailValid(password))
        {
            this.password.setError(getString(R.string.register_error_invalid_password));
            focusView = this.password;
            cancel = true;
        }

        // Kiem tra dinh dang xac nhan mat khau
        if (!TextUtils.isEmpty(confirmPassword) && !isPasswordValid(confirmPassword)) {
            this.confirmPassword.setError(getString(R.string.register_error_field_required));
            focusView = this.confirmPassword;
            cancel = true;
        } else if (!confirmPassword.equals(password)) {
            this.confirmPassword.setError(getString(R.string.register_error_invalid_confirm_password));
            focusView = this.confirmPassword;
            cancel = true;
        }
        // Kiem tra dinh dang ten tai khoan
        if (TextUtils.isEmpty(userName)) {
            this.userName.setError(getString(R.string.error_field_required));
            focusView = this.userName;
            cancel = true;
        } else if (!isUserNameValid(userName)) {
            this.userName.setError(getString(R.string.login_error_invalid_user_name));
            focusView = this.userName;
            cancel = true;
        }

        //Kiem tra dinh dang email
        if (TextUtils.isEmpty(email)) {
            this.email.setError(getString(R.string.register_error_field_required));
            focusView = this.email;
            cancel = true;
        } else if (!isEmailValid(email)) {
            this.email.setError(getString(R.string.register_error_invalid_email));
            focusView = this.email;
            cancel = true;
        }

        // Kiem tra dinh dang ten
        if (TextUtils.isEmpty(name)) {
            this.name.setError(getString(R.string.register_error_field_required));
            focusView = this.name;
            cancel = true;
        }

        //Kiem tra dinh dang ten phong kham
        if (TextUtils.isEmpty(clinicName)) {
            this.clinicName.setError(getString(R.string.register_error_field_required));
            focusView = this.clinicName;
            cancel = true;
        }

        // Kiem tra dinh dang dia chi phong kham
        if (TextUtils.isEmpty(clinicAddress)) {
            this.clinicAddress.setError(getString(R.string.register_error_field_required));
            focusView = this.clinicAddress;
            cancel = true;
        }

        // Kiem tra dinh dang cua chuyen nganh
        if (TextUtils.isEmpty(speciality)) {
            this.speciality.setError(getString(R.string.register_error_field_required));
            focusView = this.speciality;
            cancel = true;
        }

        //Kiem tra dinh dang cua kinh nghiem
        if (TextUtils.isEmpty(experience)) {
            this.experience.setError(getString(R.string.register_error_field_required));
            focusView = this.experience;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            registerTask = new UserRegisterTask(userName, password, confirmPassword, name, clinicName, clinicAddress, speciality, experience, email);
            registerTask.execute((Void) null);
        }
    }

    //Check username is valid
    private boolean isUserNameValid(String userName) {

        return userName.length() > 8;
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4;

    }

    private boolean isEmailValid(String email) {
        if (email != null) {
            Pattern p = Pattern.compile("^[A-Za-z].*?@gmail\\.com$");
            Matcher m = p.matcher(email);
            return m.find();
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

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String userName, password, confirmPassword, name, clinicName, clinicAddress, speciality, experience, email;

        UserRegisterTask(String userName, String password, String confirmPassword, String name, String clinicName, String clinicAddress, String speciality, String experience, String email) {
            this.userName = userName;
            this.password = password;
            this.confirmPassword = confirmPassword;
            this.name = name;
            this.clinicName = clinicName;
            this.clinicAddress = clinicAddress;
            this.speciality = speciality;
            this.experience = experience;
            this.email = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            //TODO:Xu ly dang nhap voi server

            Connection.register(userName,password,email,clinicName,clinicAddress,speciality,experience);
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
                RegisterActivity.this.password.setError(getString(R.string.login_error_incorrect_password));
                RegisterActivity.this.password.requestFocus();
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
        intent.putExtra("messeges", "Đăng kí thành công. Khi tài khoản được sẽ được thông báo qua email.");
        startActivity(intent);
    }
}
