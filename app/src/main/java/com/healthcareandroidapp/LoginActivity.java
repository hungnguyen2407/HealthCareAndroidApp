package com.healthcareandroidapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import org.json.*;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/passWord.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask authenTask = null;

    // UI references.
    private AutoCompleteTextView userName;
    private EditText password;
    private View progressBar;
    private View loginForm;
    private JSONObject doctorJSON = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Lay thong tin dang nhap trong may
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        String doctorInfo = sp.getString("doctorJSON", "");


        if (!doctorInfo.equals("")) {
            try {
                doctorJSON = new JSONObject(doctorInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            goMainActivity();
        }

        //Lay thong tu form dang nhap
        userName = (AutoCompleteTextView) findViewById(R.id.username);

        password = (EditText) findViewById(R.id.password_edit_text);


        Button loginButton = (Button) findViewById(R.id.submit_login_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        Button registerButton = (Button) findViewById(R.id.to_register_form_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        Button forgetButton = (Button) findViewById(R.id.btn_forget_password_action);
        forgetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                forget();
            }
        });
        loginForm = findViewById(R.id.sign_in_form);
        progressBar = findViewById(R.id.sign_in_progress);
        progressBar.setVisibility(View.GONE);
    }

    private void register() {
        InputMethodManager imm = (InputMethodManager) getSystemService(RegisterActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(loginForm.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
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


    /**
     * Attempts to sign in or RegisterActivity the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void login() {

        if (authenTask != null) {
            return;
        }

        // Reset errors.
        userName.setError(null);
        password.setError(null);

        // Store values at the time of the login attempt.
        String userName = this.userName.getText().toString();
        String password = this.password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid passWord, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            this.password.setError(getString(R.string.login_error_invalid_password));
            focusView = this.password;
            cancel = true;
        }

        // Check for a valid user name.
        if (TextUtils.isEmpty(userName)) {
            this.userName.setError(getString(R.string.error_field_required));
            focusView = this.userName;
            cancel = true;
        } else if (!isUserNameValid(userName)) {
            this.userName.setError(getString(R.string.login_error_invalid_user_name));
            focusView = this.userName;
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
            InputMethodManager imm = (InputMethodManager) getSystemService(RegisterActivity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(loginForm.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
            authenTask = new UserLoginTask(userName, password);
            authenTask.execute((Void) null);
        }
    }

    //Check username is valid
    private boolean isUserNameValid(String userName) {

        return userName.length() >= 6;
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4;

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

            loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            loginForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
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
            loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addUserNameToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addUserNameToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        userName.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {


        private final String userName;
        private final String passWord;


        UserLoginTask(String email, String password) {
            userName = email;
            this.passWord = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            doctorJSON = Connection.login(userName, passWord);

            return doctorJSON != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            authenTask = null;
            showProgress(false);

            if (success) {
                //Luu du lieu dang nhap vao may
                if (doctorJSON.toString().equals("{\"entity.Doctor\":null}")) {
                    LoginActivity.this.password.setError("Thông tin tài khoản không tồn tại");


                } else {
                    SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("doctorJSON", doctorJSON.toString());
                    editor.commit();

                    goMainActivity();

                    finish();
                }

            } else {
                LoginActivity.this.password.setError(getString(R.string.login_error_incorrect_password));
                LoginActivity.this.password.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            authenTask = null;
            showProgress(false);
        }


    }

    private void forget() {
        InputMethodManager imm = (InputMethodManager) getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(loginForm.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        Intent intent = new Intent(this, ForgetPassActivity.class);
        startActivity(intent);
    }

    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("doctorInfo", doctorJSON.toString());
        startActivity(intent);
    }
}

