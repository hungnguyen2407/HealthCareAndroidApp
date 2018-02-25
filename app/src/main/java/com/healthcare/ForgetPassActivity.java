package com.healthcare;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;

public class ForgetPassActivity extends AppCompatActivity {

    private EditText emailView;
    private View progressBar, forgetForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repass_layout);
        forgetForm = findViewById(R.id.forget_form);
        emailView = (EditText) findViewById(R.id.email_forget_pass);

        final Button btnSubmit = (Button) findViewById(R.id.submit_forget_password_button);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submit();
            }
        });
        ImageButton btnReturn = (ImageButton) findViewById(R.id.btnReturnLoginForgetPass);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnLogin();
            }
        });
    }

    private boolean submit() {

        //Kiem tra email
        String email = emailView.getText().toString();
        View focus = null;
        boolean cancel = false;
        if (TextUtils.isEmpty(email)) {
            this.emailView.setError("Hãy điền thông tin email");
            focus = emailView;
            cancel = true;
            return false;
        } else if (!Validation.isEmailValid(email)) {
            this.emailView.setError("Email không hợp lệ");
            focus = emailView;
            cancel = true;
            return false;
        }

        if (cancel) {
            focus.requestFocus();
        } else {
            progressBar = findViewById(R.id.forget_pass_progress);
            showProgress(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(ForgetPassActivity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(emailView.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
            ScrollView scrollView = (ScrollView) findViewById(R.id.forget_form);
            scrollView.setVisibility(View.GONE);
            UserForgetPassTask userForgetPassTask = new UserForgetPassTask(email);
            userForgetPassTask.execute((Void) null);
        }
        return true;
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

            forgetForm.setVisibility(show ? View.GONE : View.VISIBLE);
            forgetForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    forgetForm.setVisibility(show ? View.GONE : View.VISIBLE);
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
            forgetForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void returnLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public class UserForgetPassTask extends AsyncTask<Void, Void, Boolean> {
        private String email;

        public UserForgetPassTask(String email) {
            this.email = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Connection.resetPass(email);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            showProgress(false);

            if (success) {
                AlertDialog alertDialog = new AlertDialog.Builder(ForgetPassActivity.this).create();
                alertDialog.setTitle("Thông Báo");
                alertDialog.setMessage("Reset mật khẩu thành công. Hãy kiểm tra hộp thư lấy mật khẩu.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(ForgetPassActivity.this).create();
                alertDialog.setTitle("Thông Báo");
                alertDialog.setMessage("Reset mật khẩu không thành công.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
    }

}
